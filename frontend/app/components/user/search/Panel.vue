<script setup lang="ts">

  import type { UserData } from '~/exports/types';

  const searchResults = ref<UserData[]>([]);

  const firstSearch = ref(true);

  const handleSearch = async ( keyword: string ) => {

    const res = (await useUsers().search(keyword));
    searchResults.value = res.data.content;
    firstSearch.value = false;

  }

</script>

<template>
  <UCard
    class="bg-transparent p-0 flex flex-col gap-2 border-b-0! border-none! ring-0! shadow-none!"
    :ui="{
      header: 'p-0! bg-transparent !border-b-0 !border-none !ring-0 !shadow-none',
      body: 'flex flex-col gap-2 overflow-y-scroll p-0! !border-b-0 !border-none !ring-0 !shadow-none grow max-md:h-65'
    }">

    <template #header>
      <UserSearchBar @search="(keyword) => handleSearch(keyword)"/>
    </template>

    <template #default>

      <UserItem v-for="user in searchResults" :key="user.id" :data="user"></UserItem>

      <div v-if="searchResults.length === 0 && !firstSearch" class="h-full w-full flex justify-center items-center text-4xl font-bold opacity-20"> No Users found </div>
      <div v-else-if="firstSearch" class="h-full w-full flex justify-center items-center text-4xl font-bold opacity-20 text-center"> Type to search </div>

    </template>

  </UCard>
</template>

<style scoped>

</style>
