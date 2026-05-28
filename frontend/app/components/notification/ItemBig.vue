<script setup lang="ts">

  import type { NotificationData } from '~/exports/types';

  const { data } = defineProps<{ data: NotificationData }>();
  const emits = defineEmits(['update'])

  const onConfirm = async () => {

    console.log( await useNotifications().confirmNotification( data.id ) );
    emits("update")

  }

  const onDelete = async () => {

    await useNotifications().deleteNotification( data.id );
    emits( "update" );

  }

</script>

<template>
  <div class="relative">
    <a class="flex justify-between items-center p-2 py-4 rounded-sm hover:bg-gray-200 transition-all relative" :href="data.link" @click="() => {if(!data.confirmed)onConfirm()}">
      <div class="flex gap-1">
        <Icon name="i-lucide-dot" class="text-4xl" :style="{ color: data?.confirmed ? 'grey' : 'red' }"/>
        <div class="flex flex-col">
          <UTooltip :text="data.content"> <span>{{ data.content }}</span> </UTooltip>
          <p class="p-0"> {{ splitDate(data.notificationAt)[0] }} {{ splitDate(data.notificationAt)[1] }} </p>
        </div>
      </div>
    </a>
    <UModal :ui="{ header: 'border-0! border-none! ring-0! pt-6 pb-4!', footer: 'pt-0! flex gap-2 pb-6!', body: 'hidden' }">

      <UButton variant="ghost" class="absolute right-0 top-1/2 -translate-y-1/2 z-50"><Icon name="i-lucide-trash" class="text-2xl"/></UButton>

      <template #title> Are you sure to delete this notification? </template>
      <template #description> This action can't be reverted </template>

      <template #footer="{ close }">

        <UButton class="basis-0 grow flex justify-center" @click="close"> Cancel </UButton>
        <UButton class="basis-0 grow flex justify-center bg-red-400 hover:bg-red-400/80 focus-visible:bg-red-400 active:bg-red-500" @click="onDelete()"> Delete </UButton>

      </template>

    </UModal>
  </div>
</template>

<style scoped>

</style>
