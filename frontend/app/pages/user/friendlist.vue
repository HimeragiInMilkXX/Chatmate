<script setup lang="ts">

  import type { FriendshipData } from '~/exports/types';

  const friendships = ref<FriendshipData[] | null>(null);

  definePageMeta({ middleware: ['authenticate'] } );

  onMounted( async () => {

    friendships.value = (await useFriend().getFriendships()).data;

  })


</script>

<template>
  <UContainer>
    <UCard
      class="relative -translate-y-1/10 sm:-translate-y-1/4 md:-translate-y-1/3 shadow-[0_4px_8px_0_#00000025] flex flex-col bg-[#EDEDED] p-4 px-6 pb-8 rounded-xs"
      :ui="{ body: 'p-0! border-t border-t-black/30', header: '!border-b-0 !border-none !ring-0 !shadow-none' }">
      <template #header>
        <div class="flex items-center gap-2">
          <h2 class="text-4xl font-bold"> All Friends({{ friendships?.length }}) </h2>
        </div>
      </template>
      <template #default>

        <section class="flex flex-col pt-2 h-[50vh] overflow-y-scroll">

          <FriendItem v-for="data in friendships" :key="data.id" :data="data" @update="async ( updatedList: FriendshipData[] ) => friendships = updatedList"/>

        </section>

      </template>
    </UCard>
  </UContainer>
</template>

<style scoped>

</style>
