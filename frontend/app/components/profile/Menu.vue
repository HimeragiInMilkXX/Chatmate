<script setup lang="ts">
import type { DropdownMenuItem } from '@nuxt/ui'
import { useLoggedIn, useUserData } from '~/states/authStates';

  const imageSrc = computed( () => useRuntimeConfig().public.apiBase + useUserData().value?.avatarUrl || "/default.jpg")

  const loggedItems = ref<DropdownMenuItem[][]>([
    [
      {
        label: useUserData().value?.username,
        avatar: {
          src: imageSrc.value,
          loading: 'lazy'
        },
        type: 'label'
      }
    ],
    [
      {
        label: 'Profile',
        icon: 'i-lucide-user',
        type: 'link',
        onSelect: () => { navigateTo( "/user/mypage" ) }
      },
      {
        label: 'Settings',
        icon: 'i-lucide-cog',
      },
    ],
    [
      {
        label: 'Logout',
        icon: 'i-lucide-log-out',
        color: 'error',
        onSelect: async () => { await useAuth().logout(); }
      }
    ]
  ])

  const unloggedItems = ref<DropdownMenuItem[][]>([
    [
      {
        label: 'Login',
        icon: 'i-lucide-log-in',
        type: 'link',
        onSelect: () => { navigateTo( "/auth/login" ) }
      },
      {
        label: 'Register',
        icon: 'i-lucide-circle-plus',
        type: 'link',
        onSelect: () => { navigateTo( "/auth/register" ) }
      },
    ],
  ])

</script>

<template>
  <UDropdownMenu :items="useLoggedIn().value ? loggedItems : unloggedItems" class="hover:bg-transparent!">
    <UButton
      :avatar="{
        src: useUserData().value?.avatarUrl ? imageSrc : '/default.jpg',
        loading: 'lazy'
      }"
      class="p-0 m-2 [&>span]:size-10 object-fit"
      size="lg"
      color="neutral"
      variant="ghost"
    />
  </UDropdownMenu>
</template>
