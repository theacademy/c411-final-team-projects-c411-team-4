<script>
    import { Search, Button } from 'flowbite-svelte';
    import { SearchOutline } from 'flowbite-svelte-icons';
  </script>
  
  <div class="flex justify-center items-center gap-4 mt-6">

    <span class="font-semibold text-lg text-sky-800 whitespace-nowrap">Search your flight</span>
  
    <div class="flex items-center gap-2">
      <input 
        type="text"
        placeholder="Search Flight Number"
        class="rounded-lg border border-gray-300 p-2.5 w-60 focus:ring-blue-500 focus:border-blue-500"
      />
      <Button class="p-2.5 bg-sky-700 text-white rounded-lg">
        <SearchOutline class="w-5 h-5" />
      </Button>
    </div>
  </div>