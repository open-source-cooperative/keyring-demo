<script lang="ts">
	import {Button} from 'flowbite-svelte';
	import {platform} from '@tauri-apps/plugin-os';
	import {getStoreInfo, type HistoryEntry, useNamedStore, type VoidResult} from '$lib/commands';
	import type {Writable} from 'svelte/store';

	let {
		store = $bindable(),
		history,
		selected = $bindable(),
		error = $bindable(),
		message = $bindable()
	}: {
		store: string;
		history: Writable<HistoryEntry[]>;
		selected: HistoryEntry | undefined;
		error: string;
		message: string;
	} = $props();

	let availableStores = ['sample'];
	switch (platform()) {
		case 'android':
			availableStores.push('android');
			break;
		case 'ios':
			availableStores.push('protected');
			break;
		case 'linux':
			availableStores.push('keyutils', 'secret-service');
			break;
		case 'macos':
			availableStores.push('keychain', 'protected');
			break;
		case 'windows':
			availableStores.push('windows');
			break;
		default:
			availableStores.push('secret-service');
			break;
	}

	function setStore(name: string) {
		error = '';
		message = '';
		const handler = (result: VoidResult) => {
			if (result.error) {
				error = result.error;
			} else {
				store = name;
				history.set([]);
				selected = undefined;
				getStoreInfo((s) => {
					message = s;
				});
			}
		};
		useNamedStore(name, handler);
	}

	let storeLabels: Record<string, string> = {
		sample: 'Use Sample',
		android: 'Use Shared Prefs',
		protected: 'Use Protected Data',
		keychain: 'Use Keychain',
		keyutils: 'Use Keyutils',
		'secret-service': 'Use Secret Service',
		windows: 'Use Credential Manager'
	};
</script>

{#each availableStores as choice}
	<Button onclick={() => setStore(choice)}>
		{storeLabels[choice]}
	</Button>
{/each}
