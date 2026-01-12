<script lang="ts">
	import './layout.css';

	let { children } = $props();
</script>

<main class="h-screen w-screen p-safe">
	{@render children()}
</main>
