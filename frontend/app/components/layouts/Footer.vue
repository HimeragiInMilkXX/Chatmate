<script setup lang="ts">

  import type { NavigationMenuItem } from '@nuxt/ui'

  const projects: NavigationMenuItem[] = [
    {
      label: 'Portfolio',
    },
    {
      label: 'Dansharin',
    },
    {
      label: 'Catonese',
    },
    {
      label: 'Grinote',
    }
  ]

  const sns: NavigationMenuItem[] = [
    {
      icon: 'mdi:instagram',
      target: "_blank"
    },
    {
      icon: 'ri:twitter-x-fill',
      target: "_blank"
    },
    {
      icon: 'mdi:youtube',
      target: "_blank"
    },
    {
      icon: 'mdi:github-box',
      target: "_blank"
    }
  ]

</script>

<template>

  <UFooter
      class="bg-[url(/footer.jpg)] bg-no-repeat bg-cover bg-background/70 bg-blend-lighten bg-center"
      :ui="{
        top: 'flex justify-between items-center max-w-(--ui-container) mx-auto px-4 sm:px-6 lg:px-8 py-6 lg:py-8',
        container: 'hidden!',
        bottom: 'flex justify-between items-center max-w-(--ui-container) mx-auto px-4 sm:px-6 lg:px-8 py-6 lg:py-8'}">
    <template #top>

      <h1 class="text-3xl italic font-bold">Pingmate</h1>

    </template>
    <template #bottom>

      <small class="text-sm text-black">
        Copyright © Pingmate {{ new Date().getFullYear() }} Oscar - All Rights Reserved
      </small>

      <div class="flex items-center gap-6">
        <UNavigationMenu :items="projects" variant="link" :ui="{ link: 'text-black' }" class="max-lg:hidden"/>
        <UNavigationMenu :items="sns" variant="link" :ui="{ link: 'text-black', linkLeadingIcon: 'text-black' }"/>
      </div>

    </template>
  </UFooter>

</template>
