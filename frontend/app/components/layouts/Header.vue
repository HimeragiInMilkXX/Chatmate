<script setup lang="ts">
import { useLoggedIn } from '~/states/authStates';


</script>

<template>
  <UHeader class="border-b-0 absolute inset-0 bg-transparent backdrop-blur-none! h-20" :toggle="false">

    <template #title>
      <h1 class="text-4xl italic font-bold">Pingmate</h1>
    </template>

    <template #right>

      <NotificationList v-if="useLoggedIn().value" />

      <ULink to="/user/friendlist">
        <UButton
          v-if="useLoggedIn().value"
          icon="la:user-friends"
          class="p-0 m-2"
          size="xl"
          variant="ghost"
        />
      </ULink>

      <ProfileMenu/>
    </template>

  </UHeader>
</template>


