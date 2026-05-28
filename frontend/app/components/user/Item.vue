<script setup lang="ts">

  import type { UserData } from '~/exports/types';

  const { data } = defineProps<{ data: UserData }>();

  const imageSrc = computed( () => data.avatarUrl || "/default.jpg")

</script>

<template>
  <div class="flex items-center justify-between">
    <a class="flex justify-between items-center p-2 rounded-sm relative cursor-pointer" :href="`/user/${data.id}`">
      <div class="flex gap-4 items-center">
        <UAvatar :src="data.avatarUrl ? useProfile().getProfilePhoto(imageSrc) : imageSrc" />
        <p class="text-xl truncate min-w-20"> {{ data.username }} </p>
      </div>
    </a>
    <div class="flex gap-2 items-center text-xs lg:text-sm">
      <slot/>
    </div>
  </div>
</template>

<style scoped>

</style>
