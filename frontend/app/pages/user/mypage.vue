<script setup lang="ts">
  import { useLoggedIn } from '~/states/authStates';

  definePageMeta( { middleware: [ 'authenticate' ] } )

</script>

<template>
  <UContainer class="">

    <FormProfile v-if="useLoggedIn().value"/>

  </UContainer>
</template>

<style scoped>

</style>
