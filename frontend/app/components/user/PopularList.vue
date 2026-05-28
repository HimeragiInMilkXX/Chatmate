<script setup lang="ts">

  import type { UserDataWithFriendCount } from '~/exports/types';

  const res = await useUsers().getRankFriends( 0, 5 );

  const userData = ref<UserDataWithFriendCount[]>( res.data.content )

</script>

<template>
  <UCard
    class="bg-transparent p-0 flex flex-col gap-2 border-b-0! border-none! ring-0! shadow-none!"
    :ui="{
      header: 'p-0! bg-transparent !border-b-0 !border-none !ring-0 !shadow-none',
      body: 'flex flex-col gap-2 sm:h-65 overflow-y-scroll p-0! !border-b-0 !border-none !ring-0 !shadow-none'
    }">

    <template #header>
      <h2 class="text-3xl"> Popular Users </h2>
    </template>

    <template #default>

      <UserItem v-for="user in userData" :key="user.user.id" :data="user.user">

        {{ user.friendshipCount }} friends

      </UserItem>

    </template>

  </UCard>
</template>

<style scoped>

</style>
