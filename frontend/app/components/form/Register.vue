<script setup lang="ts">

  import type { FormSubmitEvent } from "@nuxt/ui"
  import type z from "zod";
  import { RegisterSchema } from "~/exports/schemas";

  type Schema = z.output<typeof RegisterSchema>

  const state = reactive<Partial<Schema>> ( {
    email: undefined,
    username: undefined,
    password: undefined,
    confirmPassword: undefined
  } )

  async function onSubmit( event: FormSubmitEvent<Schema> ) {

    const res = await useAuth().register( event.data );

    console.log( res );

  }

</script>

<template>
  <UForm :schema="RegisterSchema" :state="state" class="flex flex-col gap-4 bg-[#EDEDED65] shadow-md rounded-lg items-center px-8 md:px-14 py-20 max-w-200" @submit="onSubmit">

    <h2 class="text-2xl text-center min-[400px]:text-3xl md:text-4xl font-semibold mb-4"> Welcome </h2>

    <UFormField label="Email" name="email" placeholder="Enter your email" :ui="{ label: 'relative left-1' }">
      <UInput v-model="state.email" type="email" required class="w-full shadow-md" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }" />
    </UFormField>

    <UFormField label="Username" name="username" placeholder="Enter your username" :ui="{ label: 'relative left-1' }">
      <UInput v-model="state.username" required class="w-full shadow-md" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }" />
    </UFormField>

    <UFormField label="Password" name="password" placeholder="Enter your password" :ui="{ label: 'relative left-1' }">
      <UInput v-model="state.password" type="password" required class="w-full shadow-md" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
    </UFormField>

    <UFormField label="Confirm Password" name="confirmPassword" :ui="{ label: 'relative left-1' }" placeholder="Confirm your password">
      <UInput v-model="state.confirmPassword" type="password" required class="w-full shadow-md" :ui="{ base: 'text-2xl! font-medium bg-[#d9d9d9] rounded-none' }"/>
    </UFormField>

    <UButton type="submit" class="mt-2 py-2 rounded-sm w-full hover:bg-[#72a3ec] flex justify-center text-center! text-lg! font-medium bg-[#9EC5FF] text-black">
      Register
    </UButton>

    <small class="text-sm font-light"> Already have an account? <a href="/auth/login" class="text-[#0066FF]"> Login </a> </small>
  </UForm>
</template>

<style scoped>

</style>
