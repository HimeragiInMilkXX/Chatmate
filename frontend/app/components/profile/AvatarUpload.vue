<script setup lang="ts">

  import { useUserData } from '~/states/authStates';

  const fileUploaded = ref<File | null>(null)

  const { isEditing } = defineProps<{
    isEditing: boolean
  }>();

  defineExpose({
    fileUploaded,
    resetFile: () => { fileUploaded.value = null }
  })

  const imageSrc = computed( () => useUserData().value?.avatarUrl || "/default.jpg")

</script>

<template>
  <div class="md:absolute bottom-0 shadow-[0_4px_8px_0_#00000025] bg-[#EDEDED] p-4 sm:p-6 lg:p-8 rounded-xs aspect-2/3 hover:[&>.originalImage]:opacity-30 w-full" :class="{ 'pointer-events-none': !isEditing }">
    <div
      class="originalImage absolute
        lg:w-[calc(100%-2rem*2)] lg:h-[calc(100%-2rem*2)]
        sm:w-[calc(100%-1.5rem*2)] sm:h-[calc(100%-1.5rem*2)]
        w-[calc(100%-1rem*2)] h-[calc(100%-1rem*2)]
        z-1 transition-all pointer-events-none overflow-hidden left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 rounded-xs">
      <img :src="useUserData().value?.avatarUrl ? useRuntimeConfig().public.apiBase + imageSrc : imageSrc" alt="" class="object-cover w-full h-full rounded-xs">
    </div>
    <UFileUpload v-model="fileUploaded" class="h-full" label="Upload new Image" accept="image/*" :ui="{ root: 'rounded-xs' }"/>
  </div>
</template>

<style scoped>

</style>
