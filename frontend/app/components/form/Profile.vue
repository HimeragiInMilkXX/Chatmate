<script setup lang="ts">

  import type AvatarUpload from '~/components/profile/AvatarUpload.vue';
  import type { FormSubmitEvent } from "@nuxt/ui"
  import type z from "zod";
  import { ProfileSchema } from "~/exports/schemas";
  import type { UserData } from "~/exports/types";
  import { useUserData } from "~/states/authStates";

  type Schema = z.output<typeof ProfileSchema>

  const userData = ref<UserData | null>(useUserData().value);

  const getDefaultState = (): Partial<Schema> => {
    return {
      username: userData.value?.username,
      age: userData.value?.profile.age,
      bio: userData.value?.profile.bio,
      birthday: userData.value?.profile.birthday,
      nationality: userData.value?.profile.nationality,
      region: userData.value?.profile.region,
      interest: userData.value?.profile.interest
    }
  }

  const state = reactive<Partial<Schema>> (getDefaultState())

  const avatarUploadRef = ref<InstanceType<typeof AvatarUpload> | null>(null);
  async function onSubmit( event: FormSubmitEvent<Schema> ) {

    if( avatarUploadRef.value?.fileUploaded ) {
      await useProfile().uploadAvatar( avatarUploadRef.value.fileUploaded )
      avatarUploadRef.value?.resetFile()
    }

    const data = event.data;

    if( !data.username ) delete data.username;

    const res = await useProfile().updateProfile( data );

    console.log( res );

    isEditing.value = false;

    useToast().add({
      title: 'Profile Successfully Updated',
      description: `Changes may take time to reflect`,
      icon: 'mdi:information-outline'
    })

  }

  const isEditing = ref( false );

  const form = useTemplateRef( "form" );

  const resetForm = () => {

    Object.assign( state, getDefaultState() );

    form.value?.clear();

  }

</script>

<template>

  <UForm ref="form" :schema="ProfileSchema" class="relative -translate-y-1/18 sm:-translate-y-1/8 md:-translate-y-1/4 shadow-[0_4px_8px_0_#00000025] flex flex-col gap-4 sm:gap-6 lg:gap-8 bg-[#EDEDED] p-4 sm:p-6 lg:p-8 rounded-xs" :state="state" :disabled="!isEditing" @submit="onSubmit">

    <div class="flex gap-4 sm:gap-6 lg:gap-8 max-[556px]:flex-col">
      <div class="basis-0 grow relative"><ProfileAvatarUpload ref="avatarUploadRef" :is-editing="isEditing"/></div>
      <div class="grow-[2.5] flex flex-col justify-between basis-0 gap-4 sm:gap-6 lg:gap-8">
        <div class="flex flex-col gap-4 sm:gap-6 lg:gap-8">
          <UFormField label="Username" name="username" :ui="{ label: 'relative left-1' }">
            <UInput v-model="state.username" required class="w-full" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
          </UFormField>
          <UFormField label="Email" name="email" :ui="{ label: 'relative left-1' }">
            <UInput :default-value="userData?.email" disabled class="w-full" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
          </UFormField>
        </div>
        <h1 class="self-end text-2xl text-center min-[400px]:text-3xl md:text-4xl font-semibold relative right-2"> <span class="hidden max-[1540]:inline">Edit your Profile / </span> Account Management </h1>
      </div>
    </div>

    <div class="flex gap-4 sm:gap-6 lg:gap-8 w-full max-sm:flex-col">
      <div class="basis-0 grow flex flex-col gap-4 sm:gap-6 lg:gap-8 items-stretch">
        <UFormField label="Age" name="age" placeholder="Enter your age" :ui="{ label: 'relative left-1' }">
          <UInput v-model="state.age" type="number" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}"/>
        </UFormField>
        <UFormField label="Birthday" name="birthday" placeholder="Enter your birthday" :ui="{ label: 'relative left-1' }">
          <UInput v-model="state.birthday" type="date" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}"/>
        </UFormField>
        <UFormField label="Nationality" name="nationality" placeholder="Enter your nationality" :ui="{ label: 'relative left-1' }">
          <UInput v-model="state.nationality" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}" />
        </UFormField>
        <UFormField label="Interest" name="interest" placeholder="Enter your interest" :ui="{ label: 'relative left-1' }">
          <UInput v-model="state.interest" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}" />
        </UFormField>
      </div>
      <div class="basis-0 grow flex flex-col gap-4 sm:gap-6 lg:gap-8 items-stretch">
        <UFormField label="Region" name="region" placeholder="Enter your region" :ui="{ label: 'relative left-1' }">
          <UInput v-model="state.region" class="w-full" :ui="{ base: 'text-lg! font-light bg-[#d9d9d9] rounded-none'}" />
        </UFormField>
        <UFormField label="Bio" name="bio" placeholder="Enter your bio" class="grow flex flex-col" :ui="{ container: 'h-full', label: 'relative left-1' }">
          <UTextarea v-model="state.bio" :ui="{ root: 'no-scrollbar h-full', base: 'resize-none no-scrollbar h-full bg-[#d9d9d9] rounded-none text-lg! font-light' }" class="w-full"/>
        </UFormField>
      </div>
    </div>

    <div class="flex justify-between w-full max-sm:flex-col gap-2 sm:gap-4 lg:gap-6">
      <ProfileResetPasswordModal/>
      <div class="flex gap-2 sm:gap-4 lg:gap-6 max-sm:flex-col">
        <UButton v-if="!isEditing" type="button" class="rounded-sm w-full sm:w-36 hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF] text-black" @click="isEditing = true">
          Edit
        </UButton>
        <UButton v-else type="button" class="rounded-sm w-full sm:w-36 hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF] text-black" @click="isEditing = false; resetForm()">
          Cancel
        </UButton>
        <UButton type="submit" :disabled="!isEditing" class="rounded-sm w-full sm:w-36 hover:bg-[#ec8484] flex justify-center text-center! text-lg! font-medium bg-[#FFA9A9] disabled:bg-[#FFA9A950]! text-black">
          Update
        </UButton>
      </div>
    </div>
  </UForm>
</template>
