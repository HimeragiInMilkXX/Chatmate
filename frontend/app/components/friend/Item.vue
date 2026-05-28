<script setup lang="ts">

  import type { FriendshipData, UserData } from '~/exports/types';
  import { useUserData } from '~/states/authStates';

  const { data } = defineProps<{ data: FriendshipData }>();
  const emits = defineEmits(['update'])

  const userData = useUserData();

  const { id, username } = getChatPartnerData( data, userData.value?.id );

  const res = await useProfile().getUserProfileById( id.toString() );
  const partnerData = ref<UserData>( res );

  const imageSrc = computed( () => partnerData.value?.avatarUrl || "/default.jpg")

  const onDelete = async () => {

    const res = (await useFriend().removeFriendship(data.id)).data;
    emits( "update", res );

  }

</script>

<template>
  <div class="flex items-center justify-between">
    <a class="flex justify-between items-center p-2 py-4 rounded-sm relative cursor-pointer" :href="`/user/${id}`">
      <div class="flex gap-4 items-center">
        <UAvatar :src="partnerData.avatarUrl ? useProfile().getProfilePhoto(imageSrc) : imageSrc" />
        <p class="text-xl"> {{ username }} </p>
      </div>
    </a>
    <div class="flex gap-2 items-center">
      <ULink :to="`/chat/${data.id}`">
        <UButton type="button" class="gap-2 items-center rounded-sm hover:bg-[#81f592] flex justify-center text-center! font-medium bg-[#9EFFAD] active:bg-[#89ec98] text-black">
          <Icon name="uiw:message"/>
        </UButton>
      </ULink>
      <UModal :ui="{ header: 'border-0! border-none! ring-0! pt-6 pb-4!', footer: 'pt-0! flex gap-2 pb-6!', body: 'hidden' }">
        <UButton variant="ghost" class="gap-2 items-center rounded-sm flex justify-center text-center! font-medium hover:opacity-80 hover:bg-[#FFA9A9] bg-[#FFA9A9] active:bg-[#f89d9d]"> <Icon name="i-lucide-trash"/></UButton>
        <template #title> Are you sure to remove {{ username }} from your friend list? </template>
        <template #description> All your messages will be permanently deleted. This action can't be reverted </template>
        <template #footer="{ close }">
          <UButton class="basis-0 grow flex justify-center" @click="close"> Cancel </UButton>
          <UButton class="basis-0 grow flex justify-center bg-red-400 hover:bg-red-400/80 focus-visible:bg-red-400 active:bg-red-500" @click="onDelete()"> Remove </UButton>
        </template>
      </UModal>
    </div>
  </div>
</template>

<style scoped>

</style>
