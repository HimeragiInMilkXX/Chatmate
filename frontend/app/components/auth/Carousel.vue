<script setup lang="ts">

  const items = [
    '/authCarousel/1.jpg',
    '/authCarousel/2.jpg',
    '/authCarousel/3.jpg',
  ]

</script>

<template>

  <UCarousel v-slot="{ item }" :items="items" class="w-full mx-auto" loop :autoplay="{ delay: 5000 }" :ui="{ item: 'p-0! w-dvw h-dvh' }">
    <img :src="item" class="object-cover w-full h-full" loading="lazy">
  </UCarousel>

</template>


