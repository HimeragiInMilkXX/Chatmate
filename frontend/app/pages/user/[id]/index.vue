<script setup lang="ts">

  import type { FriendshipReturn, UserData } from '~/exports/types';
  import { useUserData } from '~/states/authStates';

  const { id } = useRoute().params;

  definePageMeta( { middleware: [ 'authenticate' ] } )

  const userData = ref<UserData | null | undefined>();
  const friendshipData = ref<FriendshipReturn | undefined>();

  onMounted( async () => {

    if( useUserData().value?.id === parseInt( id as string ) ) {
      navigateTo( "/user/mypage");
    } else {
      userData.value = await useProfile().getUserProfileById( id as string );
      friendshipData.value =  await useFriend().getFriendshipStatus( id as string );
    }

  })


</script>

<template>

  <UContainer v-if="userData">

    <UserProfile :user-data="userData" :friendship-data="friendshipData?.data">

      <div class="flex gap-2 sm:gap-4 lg:gap-6 max-sm:flex-col justify-end">
        <UButton type="button" class="gap-2 items-center rounded-sm w-full sm:min-w-36 hover:bg-[#ec8484] flex justify-center text-center! text-lg! font-medium bg-[#FFA9A9] disabled:bg-[#FFA9A950]! text-black">
          <Icon name="mdi:ban" /> Report & Block
        </UButton>
        <UButton :disabled="!(friendshipData?.data.status === 'ACCEPTED')" type="button" class="gap-2 items-center rounded-sm w-full sm:min-w-36 hover:bg-[#81f592] flex justify-center text-center! text-lg! font-medium bg-[#9EFFAD] text-black" @click="navigateTo(`/chat/${friendshipData?.data.id}`)" >
          <Icon name="uiw:message"/> Message
        </UButton>
        <ButtonAddFriend :friend-ship-status="friendshipData?.data" @update="async () => { friendshipData = await useFriend().getFriendshipStatus( id as string );}"/>
      </div>

    </UserProfile>

  </UContainer>

  <UContainer v-else>
    <UContainer class="items-center justify-center text-center text-4xl h-[85vh] relative -translate-y-1/18 sm:-translate-y-1/8 md:-translate-y-1/4 shadow-[0_4px_8px_0_#00000025] flex flex-col gap-4 sm:gap-6 lg:gap-8 bg-[#EDEDED] p-4 sm:p-6 lg:p-8 rounded-xs" disabled>

      FETCHING USER DATA...
    </UContainer>
  </UContainer>

</template>

<style scoped>

</style>
