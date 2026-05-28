<script setup lang="ts">

  import type { FriendshipData, UserData } from '~/exports/types';
  import { useUserData } from '~/states/authStates';

  const userData = useUserData();

  const { friendshipData } = defineProps<{ friendshipData: FriendshipData }>();

  const { id, username } = getChatPartnerData( friendshipData, userData.value?.id );

  const res = await useProfile().getUserProfileById( id.toString() );
  const PartnerData = ref<UserData>( res );

  const imageSrc = computed( () => PartnerData.value?.avatarUrl || "/default.jpg")

</script>

<template>

  <div class="absolute w-full h-full inset-0 px-4 sm:px-6 lg:px-8 flex items-center">
    <UCard
      v-if="PartnerData"
      class="flex flex-col items-center relative top-4 box-border mx-auto w-full max-w-(--ui-container) h-[80vh] shadow-[0_4px_8px_0_#00000025] bg-[#EDEDED] rounded-xs p-0!"
      :ui="{
        header: 'w-full bg-[#e1e3ee] py-4 px-6 flex items-center gap-4',
        body: 'w-full py-4 px-6 flex flex-col items-end gap-8 grow overflow-y-scroll',
        footer: 'w-full bg-[#e1e3ee] py-4 px-6 flex items-center gap-4' }">
      <template #header>
        <ULink :to="`/user/${PartnerData.id}`">
          <UAvatar :src="PartnerData.avatarUrl ? useProfile().getProfilePhoto(imageSrc) : imageSrc" />
        </ULink>
        <h2 class="text-xl"> {{ res.username }} </h2>
      </template>

      <template #default>

        <slot name="messages"/>

      </template>

      <template #footer>
        <slot name="footer"/>
      </template>
    </UCard>
  </div>

</template>

<style scoped>

</style>
