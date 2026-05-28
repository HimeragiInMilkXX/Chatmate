<script setup lang="ts">

  const { title = false } = defineProps<{ title?: boolean }>();

</script>

<template>
  <UApp>
    <LayoutsHeader/>
    <div class="w-full relative">
      <NuxtImg src="/hero.jpg" class="object-cover aspect-72/31 w-full opacity-55"/>
      <h1 v-if="title" class="absolute lg:top-1/3 xl:top-2/5 left-1/2 transform -translate-x-1/2 -translate-y-1/2 lg:block hidden lg:text-6xl xl:text-8xl font-extrabold text-black text-center">Meet new<br>friends!</h1>
    </div>
    <slot/>
    <LayoutsFooter/>
  </UApp>
</template>


