<script setup lang="ts">

  const emits = defineEmits(['search']);
  const search = ref('');

  const handleSearch = () => {

    if( search.value.trim() === '' ) return;
    emits('search', search.value.trim());

  }

</script>

<template>
  <div class="flex gap-4">

    <UInput v-model="search" icon="i-lucide-search" size="md" variant="outline" placeholder="Search a user..." class="basis-0 grow-8" :ui="{ base: 'bg-[#D9D9D972] text-xl! p-2! pl-12!', leadingIcon: 'px-4', trailing: 'pe-1' }">
      <template v-if="search?.length" #trailing>
        <UButton
          color="neutral"
          variant="link"
          size="sm"
          icon="i-lucide-circle-x"
          aria-label="Clear input"
          @click="search = ''"
        />
      </template>
    </UInput>
    <UButton class="basis-0 grow bg-[#9DC4FF] flex justify-center items-center text-black! text-xl hover:bg-[#9DC4FF]! hover:opacity-85 active:bg-[#86ace4]!" @click="handleSearch"> Search </UButton>

  </div>
</template>
