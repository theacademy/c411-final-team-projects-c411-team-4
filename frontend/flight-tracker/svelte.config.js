import adapter from '@sveltejs/adapter-auto';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/kit').Config} */
const config = {
    // Consult https://svelte.dev/docs/kit/integrations
    // for more information about preprocessors
    preprocess: vitePreprocess(),

    kit: {
        alias: {
            "@/*": "./path/to/lib/*",
        },
        adapter: adapter()
    },
    compilerOptions: {
        compatibility: {
            componentApi: 5
        }
    }
};

export default config;
