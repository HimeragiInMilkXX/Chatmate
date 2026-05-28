<script setup lang="ts">

  import type { FriendshipData } from '~/exports/types';
  import { useUserData } from '~/states/authStates';

  const { id } = useRoute().params;

  const { friendShipStatus } = defineProps<{ friendShipStatus: FriendshipData }>();
  const emits = defineEmits(['update']);

  const userId = ref<number | undefined>( useUserData().value?.id );

  const onSend = async () => {

    console.log( await useFriend().sendFriendRequest( id as string ) )
    emits("update")

  }

  const onAccept = async () => {

    console.log( await useFriend().acceptFriendRequest( id as string ) );
    emits("update")

  }

</script>

<template>
  <UButton
    v-if="!friendShipStatus"
    type="button"
    class="gap-2 items-center rounded-sm w-full sm:min-w-36 hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF] text-black"
    @click="onSend()">

    <Icon name="weui:add-friends-outlined" class="text-xl"/> Add Friend
  </UButton>
  <UButton
    v-else-if="friendShipStatus.status === 'PENDING' && friendShipStatus.senderId === userId"
    disabled
    type="button"
    class="gap-2 items-center rounded-sm w-full sm:min-w-36 hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF]">
    <Icon name="weui:add-friends-outlined" class="text-xl"/> Pending
  </UButton>
  <UButton
    v-else-if="friendShipStatus.status === 'PENDING' && friendShipStatus.receiverId === userId"
    type="button"
    class="gap-2 items-center rounded-sm w-full sm:min-w-36 hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF]"
    @click="onAccept()">
    <Icon name="weui:add-friends-outlined" class="text-xl"/> Accept
  </UButton>
  <UButton
    v-else-if="friendShipStatus.status === 'ACCEPTED'"
    type="button"
    disabled
    class="gap-2 items-center rounded-sm w-full sm:min-w-36 hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF]"
    @click="onAccept()">
    <Icon name="weui:add-friends-outlined" class="text-xl"/> Good Friend!
  </UButton>
</template>

<style scoped>

</style>
