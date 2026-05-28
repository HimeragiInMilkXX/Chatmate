<script setup lang="ts">

  import type { NotificationData } from '~/exports/types';

  const notifications = ref<NotificationData[] | null>(null);

  onMounted( async () => {

    notifications.value = (await useNotifications().getNotifications()).data;

    console.log( notifications.value )

  })

  const hasUnread = (): boolean => {

    notifications.value?.forEach(element => {

      if( !element.confirmed ) return true;

    });

    return false;

  }

</script>

<template>

  <UDropdownMenu class="hover:bg-transparent!">

    <div class="hover:bg-transparent! relative">
      <UButton
        v-if="notifications"
        icon="i-lucide-mail"
        class="p-0 m-2"
        size="lg"
        variant="ghost"
      />
      <Icon v-if="hasUnread()" name="i-lucide-dot" class="size-12 absolute left-0 bottom-0 select-none pointer-events-none" style="color: red" />
    </div>

    <template #content-top>

      <h2 class="text-center text-2xl font-medium py-4"> Unread Notifications </h2>

      <div class="flex flex-col gap-0.5 max-h-100 no-scrollbar overflow-y-scroll w-80 box-border px-4">
        <LazyNotificationItem v-for="data in notifications" :key="data.id" :data="data" @update="async () => notifications = (await useNotifications().getNotifications()).data" />
      </div>

      <h2 class="text-center text-xs font-light opacity-80 py-4"> You don't have any new notifications </h2>

    </template>

    <template #content-bottom>

      <a class="text-center py-4 text-sm underline text-blue-400" href="/user/notifications"> View All Notifications </a>

    </template>

  </UDropdownMenu>
</template>
