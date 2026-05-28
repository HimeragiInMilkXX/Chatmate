<script setup lang="ts">

</script>

<template>
  <UApp>
    <UHeader class="border-b-0 absolute inset-0 bg-transparent backdrop-blur-none! h-20" :toggle="false">

      <template #title>
        <h1 class="text-4xl italic font-bold">Pingmate</h1>
      </template>

    </UHeader>

    <AuthCarousel class="opacity-15"/>

    <slot/>
  </UApp>
</template>


