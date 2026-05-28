<script setup lang="ts">

  import type { MessageData } from '~/exports/types';

  const { data, isSent } = defineProps<{ data: MessageData, isSent: boolean }>()

  const timeParts = splitDate( data.createdAt )[1]!.split(":");
  const time = `${timeParts[0]}:${timeParts[1]}`

</script>

<template>
  <div class="flex gap-2 max-w-4/5 items-end" :class="isSent ? 'self-end' : 'self-start'">
    <div :class="isSent ? 'bg-[#9EC5FF]' : 'self-start'" class="rounded-xs p-2 shadow break-all" >
      {{ data.content }}
    </div>
    <span class="text-[0.6rem] opacity-50"> {{ time }} </span>
  </div>
</template>


<style scoped>

</style>
