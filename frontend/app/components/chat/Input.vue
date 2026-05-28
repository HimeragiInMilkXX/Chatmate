<script setup lang="ts">

  const inputContent = ref<string>("");

  const emits = defineEmits(['sendMessage'])

  const send = () => {
    emits('sendMessage', inputContent.value);
    inputContent.value = '';
  }

</script>

<template>
  <div class="flex gap-2 w-full items-center" @keydown.enter="send">

    <UInput
      v-model="inputContent"
      class="grow rounded-sm"
      :ui="{ base: 'border-0 shadow-0 h-12 bg-white shadow text-black font-light outline-0! ring-0! focus-visible:ring-0! focus-visible:outline-0!' }"
      placeholder="Enter your message"/>

    <UButton
      class="w-25 text-center"
      :ui="{ base: 'rounded-sm leading-0 flex items-center justify-center h-12 py-0 relative top-[0.01px] bg-[#9EC5FF] hover:bg-[#7CB0FF] active:opacity-80 active:bg-[#7CB0FF] text-black', label: 'leading-none'}"
      @click="send">

      <Icon name="i-lucide-send"/>
      Send

    </UButton>

  </div>
</template>

<style scoped>

</style>
