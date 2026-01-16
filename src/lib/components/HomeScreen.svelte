<script lang="ts">
	import { writable } from 'svelte/store';
	import { type HistoryEntry } from '$lib/commands';
	import { P } from 'flowbite-svelte';
	import HistoryPanel from '$lib/components/HistoryPanel.svelte';
	import InfoPanel from '$lib/components/InfoPanel.svelte';
	import TopBar from '$lib/components/TopBar.svelte';

	let store = $state('none');
	let history = writable<HistoryEntry[]>([]);
	let selected = $state<HistoryEntry | undefined>(undefined);
</script>

<TopBar bind:store {history} bind:selected />
{#if store !== 'none'}
	<div class="grid w-full grid-cols-1 gap-4 sm:grid-cols-2">
		<div class="col-span-1">
			<InfoPanel {selected} />
		</div>
		<div class="col-span-1 space-y-2">
			<div class="flex justify-center">
				<P size="lg" weight="bold">Entry History</P>
			</div>
			<div class="flex justify-center">
				<HistoryPanel {history} bind:selected />
			</div>
		</div>
	</div>
{/if}
