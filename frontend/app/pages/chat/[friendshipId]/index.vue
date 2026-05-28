<script setup lang="ts">

  import { useChat } from '~/composables/useChat';
  import type { FriendshipData, FriendshipReturn } from '~/exports/types';
  import { useUserData } from '~/states/authStates';

  const { friendshipId } = useRoute().params;

  const userData = useUserData();

  definePageMeta( { layout: "auth", middleware: [ 'authenticate' ] } )

  const res: FriendshipReturn = await useFriend().getFriendshipById( friendshipId );

  const friendshipData = ref<FriendshipData>( res.data );

  const { messages, sendMessage } = useChat( Number(friendshipData.value.id) );

</script>

<template>

  <ChatWindow :friendship-data="friendshipData">

    <template #messages>

      <ChatMessage v-for="data in messages" :key="data.id" :data="data" :is-sent="data.senderId === userData?.id" />

    </template>

    <template #footer>

      <ChatInput @send-message="( inputContent: string ) => sendMessage(inputContent)" />

    </template>

  </ChatWindow>

</template>


<style scoped>

</style>
