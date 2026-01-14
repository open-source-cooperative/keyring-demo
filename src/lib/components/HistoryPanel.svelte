<script lang="ts">
	import type { HistoryEntry } from '../commands';
	import { VirtualList } from 'flowbite-svelte';
	import type { Readable } from 'svelte/store';

	let {
		history,
		selected = $bindable()
	}: {
		history: Readable<HistoryEntry[]>;
		selected: HistoryEntry | undefined;
	} = $props();

	interface ListItem {
		name: string;
		cls: string;
		current: boolean;
		onclick: () => void;
	}

	const base_class = "w-full border-b p-2 text-left"
	let entries: ListItem[] = $derived(
		$history.toReversed().map((entry) => {
			let name = `Entry ${entry.id}`;
			if (entry.is_specifier) {
				name += ` (${entry.service}, ${entry.user})`;
			} else {
				name += ` [wrapper]`
			}
			let current = entry.id === selected?.id;
			let cls = current ? base_class + " bg-primary-300 hover:bg-primary-400" : base_class +" hover:bg-primary-100"
			let onclick = () => {
				if (current) {
					selected = undefined;
				} else {
					selected = entry;
				}
			};
			return { name, cls, current, onclick } as ListItem;
		})
	);
</script>

{#if entries.length}
	<div class="w-full pr-4 pl-4">
		<VirtualList items={entries}>
			{#snippet children(entry)}
				<button class={entry.cls} onclick={entry.onclick}>{entry.name}</button>
			{/snippet}
		</VirtualList>
	</div>
{:else}
	<p class="text-center italic">No history</p>
{/if}
