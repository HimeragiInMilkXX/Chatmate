<script setup lang="ts">

  import type { NotificationData } from '~/exports/types';

  const { data } = defineProps<{ data: NotificationData }>();
  const emits = defineEmits(['update'])

  const onConfirm = async () => {

    console.log( await useNotifications().confirmNotification( data.id ) );
    emits("update")

  }

</script>

<template>
  <a v-if="!data.confirmed" class="flex gap-1 p-2 rounded-sm hover:bg-gray-200 transition-all" :href="data.link" @click="onConfirm()">

    <Icon name="i-lucide-dot" class="text-4xl" style="color: red"/>
    <div class="flex flex-col">
      <UTooltip :text="data.content"> <span>{{ data.content }}</span> </UTooltip>
      <p class="p-0"> {{ splitDate(data.notificationAt)[0] }} {{ splitDate(data.notificationAt)[1] }} </p>
    </div>

  </a>
</template>

<style scoped>

</style>
