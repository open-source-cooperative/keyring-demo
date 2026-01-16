<script lang="ts">
	import { Alert, Button } from 'flowbite-svelte';
	import SelectStoreButtons from '$lib/components/SelectStoreButtons.svelte';
	import SampleDataButton from '$lib/components/SampleDataButton.svelte';
	import type { Writable } from 'svelte/store';
	import { type HistoryEntry, releaseStore } from '$lib/commands';
	import NewEntryButton from '$lib/components/NewEntryButton.svelte';
	import SearchButton from '$lib/components/SearchButton.svelte';

	let {
		store = $bindable(),
		history,
		selected = $bindable()
	}: {
		store: string;
		history: Writable<HistoryEntry[]>;
		selected: HistoryEntry | undefined;
	} = $props();

	let error = $state('');
	let message: string = $derived(store == 'none' ? 'No current credential store' : '');
	$effect(() => {
		if (selected !== undefined) {
			message = ''
			error = ''
		}
	})

	function release() {
		error = '';
		message = '';
		releaseStore((result) => {
			if (result.error) {
				error = result.error;
			} else {
				store = 'none';
				history.set([]);
				selected = undefined;
			}
		});
	}
</script>

<div class="w-full p-4">
	<div class="grid grid-cols-2 gap-4 sm:grid-cols-4">
		{#if store === 'none'}
			<SelectStoreButtons bind:store {history} bind:selected bind:error bind:message />
		{:else}
			<NewEntryButton {history} bind:selected bind:error />
			<SearchButton {history} bind:selected bind:error bind:message />
			<SampleDataButton {history} bind:error bind:message />
			<Button color="red" onclick={release}>Release Store</Button>
		{/if}
	</div>
	{#if message}
		<div class="w-full p-4 pb-0">
			<Alert onclick={() => (message = '')} dismissable>{message}</Alert>
		</div>
	{/if}
	{#if error}
		<div class="w-full p-4 pb-0">
			<Alert color="red" onclick={() => (error = '')} dismissable>{error}</Alert>
		</div>
	{/if}
</div>
