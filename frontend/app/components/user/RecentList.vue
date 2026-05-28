<script setup lang="ts">

  import type { UserData } from '~/exports/types';

  const res = await useUsers().getRecentLogin( 5 );

  const userData = ref<UserData[]>( res.data )

  const constructTime = ( dateTime: string ) => {

    const [ date, time ] = splitDate( dateTime );
    return `${date} ${time}`

  }

</script>

<template>
  <UCard
    class="bg-transparent p-0 flex flex-col gap-2 border-b-0! border-none! ring-0! shadow-none!"
    :ui="{
      header: 'p-0! bg-transparent !border-b-0 !border-none !ring-0 !shadow-none',
      body: 'flex flex-col gap-2 sm:h-65 overflow-y-scroll p-0! !border-b-0 !border-none !ring-0 !shadow-none'
    }">

    <template #header>
      <h2 class="text-3xl"> Active Users </h2>
    </template>

    <template #default>

      <UserItem v-for="user in userData" :key="user.id" :data="user">

        <p>{{ constructTime( user.lastLogin ) }}</p>

      </UserItem>

    </template>

  </UCard>
</template>

<style scoped>

</style>
