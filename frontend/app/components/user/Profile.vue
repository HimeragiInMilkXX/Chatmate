<script setup lang="ts">

  import type { FriendshipData, UserData } from '~/exports/types';

  const { userData, friendshipData } = defineProps<{ userData: UserData, friendshipData: FriendshipData | undefined }>();

  const imageSrc = computed( () => userData?.avatarUrl || "/default.jpg")

</script>

<template>

  <UForm class="relative -translate-y-1/18 sm:-translate-y-1/8 md:-translate-y-1/4 shadow-[0_4px_8px_0_#00000025] flex flex-col gap-4 sm:gap-6 lg:gap-8 bg-[#EDEDED] p-4 sm:p-6 lg:p-8 rounded-xs" disabled>

    <div class="flex gap-4 sm:gap-6 lg:gap-8 max-[556px]:flex-col">

      <div class="basis-0 grow relative">

        <div class="md:absolute bottom-0 shadow-[0_4px_8px_0_#00000025] bg-[#EDEDED] p-4 sm:p-6 lg:p-8 rounded-xs aspect-2/3 hover:[&>.originalImage]:opacity-30 w-full">
          <div
            class="originalImage absolute
              lg:w-[calc(100%-2rem*2)] lg:h-[calc(100%-2rem*2)]
              sm:w-[calc(100%-1.5rem*2)] sm:h-[calc(100%-1.5rem*2)]
              w-[calc(100%-1rem*2)] h-[calc(100%-1rem*2)]
              z-1 transition-all pointer-events-none overflow-hidden left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 rounded-xs">
            <img :src="userData.avatarUrl ? useRuntimeConfig().public.apiBase + imageSrc : imageSrc" alt="" class="object-cover w-full h-full rounded-xs">
          </div>
        </div>

      </div>

      <div class="grow-[2.5] flex flex-col justify-between basis-0 gap-4 sm:gap-6 lg:gap-8">
        <div class="flex flex-col gap-4 sm:gap-6 lg:gap-8">
          <UFormField label="Username" name="username" :ui="{ label: 'relative left-1' }">
            <UInput :default-value="userData?.username" disabled required class="w-full" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
          </UFormField>
          <div class="flex gap-4 sm:gap-6 lg:gap-8">
            <UFormField label="Member Since" name="memberSince" :ui="{ label: 'relative left-1' }" class="basis-0 grow">
              <UInput :default-value="splitDate( userData?.createdAt )[0]" disabled class="w-full" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
            </UFormField>
            <UFormField v-if="friendshipData" label="Friend Since" name="friendSince" :ui="{ label: 'relative left-1' }" class="basis-0 grow">
              <UInput :default-value="splitDate( friendshipData?.friendSince )[0]" disabled class="w-full" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
            </UFormField>
          </div>
        </div>

        <slot/>
      </div>
    </div>

    <div class="flex gap-4 sm:gap-6 lg:gap-8 w-full max-sm:flex-col">
      <div class="basis-0 grow flex flex-col gap-4 sm:gap-6 lg:gap-8 items-stretch">
        <UFormField label="Age" name="age" placeholder="Enter your age" :ui="{ label: 'relative left-1' }">
          <UInput :default-value="userData?.profile.age" type="number" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}"/>
        </UFormField>
        <UFormField label="Birthday" name="birthday" placeholder="Enter your birthday" :ui="{ label: 'relative left-1' }">
          <UInput :default-value="userData?.profile.birthday" type="date" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}"/>
        </UFormField>
        <UFormField label="Nationality" name="nationality" placeholder="Enter your nationality" :ui="{ label: 'relative left-1' }">
          <UInput :default-value="userData?.profile.nationality" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}" />
        </UFormField>
        <UFormField label="Interest" name="interest" placeholder="Enter your interest" :ui="{ label: 'relative left-1' }">
          <UInput :default-value="userData?.profile.interest" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}" />
        </UFormField>
      </div>
      <div class="basis-0 grow flex flex-col gap-4 sm:gap-6 lg:gap-8 items-stretch">
        <UFormField label="Region" name="region" placeholder="Enter your region" :ui="{ label: 'relative left-1' }">
          <UInput :default-value="userData?.profile.region" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}" />
        </UFormField>
        <UFormField label="Bio" name="bio" placeholder="Enter your bio" class="grow flex flex-col" :ui="{ container: 'h-full', label: 'relative left-1' }">
          <UTextarea :default-value="userData?.profile.bio" :ui="{ root: 'no-scrollbar h-full', base: 'resize-none no-scrollbar h-full bg-[#d9d9d9] rounded-none text-lg! font-light' }" class="w-full"/>
        </UFormField>
      </div>
    </div>
  </UForm>
</template>
