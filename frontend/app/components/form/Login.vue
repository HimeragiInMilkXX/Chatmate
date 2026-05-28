<script setup lang="ts">

  import type { FormSubmitEvent } from "@nuxt/ui"
  import type z from "zod";
  import { LoginSchema } from "~/exports/schemas";

  type Schema = z.output<typeof LoginSchema>

  const state = reactive<Partial<Schema>> ( {
    usernameOrEmail: undefined,
    password: undefined
  } )

  async function onSubmit( event: FormSubmitEvent<Schema> ) {

    await useAuth().login( event.data );
    navigateTo( "/user/mypage" )

  }

</script>

<template>
  <UForm :schema="LoginSchema" :state="state" class="flex flex-col gap-4 bg-[#EDEDED65] shadow-md rounded-lg items-center px-8 md:px-14 py-20 max-w-200" @submit="onSubmit">

    <h2 class="text-2xl text-center min-[400px]:text-3xl md:text-4xl font-semibold mb-4"> Welcome back! </h2>

    <UFormField label="Username or Email" name="usernameOrEmail" :ui="{ label: 'relative left-1' }">
      <UInput v-model="state.usernameOrEmail" required class="w-full shadow-md" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
    </UFormField>

    <UFormField label="Password" name="password" :ui="{ label: 'relative left-1' }">
      <UInput v-model="state.password" required class="w-full shadow-md" type="password" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
    </UFormField>

    <UButton type="submit" class="mt-2 py-2 rounded-sm w-full hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF] text-black">
      Login
    </UButton>

    <small class="text-sm font-light"> Don't have an account? <a href="/auth/register" class="text-[#0066FF]"> Register </a> </small>

  </UForm>
</template>

<style scoped>

</style>
