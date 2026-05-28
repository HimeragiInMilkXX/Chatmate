<script setup lang="ts">

  import type { FormSubmitEvent } from "@nuxt/ui"
  import type z from "zod";
  import { ResetPasswordSchema } from "~/exports/schemas";

  type Schema = z.output<typeof ResetPasswordSchema>

  const state = reactive<Partial<Schema>> ( {
    oldPassword: undefined,
    newPassword: undefined,
    confirmPassword: undefined
  } )

  async function onSubmit( event: FormSubmitEvent<Schema> ) {

    const res = await useAuth().resetPassword( event.data );

    console.log( res );

  }

</script>

<template>
  <UForm :schema="ResetPasswordSchema" :state="state" class="space-y-4 p-8" @submit="onSubmit">
    <UFormField label="Current Password" name="oldPassword" placeholder="Enter your current password">
      <UInput v-model="state.oldPassword" type="password" required class="w-full"/>
    </UFormField>

    <UFormField label="New Password" name="newPassword" placeholder="Enter your new password">
      <UInput v-model="state.newPassword" type="password" required class="w-full"/>
    </UFormField>

    <UFormField label="Confirm Password" name="confirmPassword" placeholder="Confirm your password">
      <UInput v-model="state.confirmPassword" type="password" required class="w-full"/>
    </UFormField>

    <UButton type="submit">
      Reset Password
    </UButton>
  </UForm>
</template>

<style scoped>

</style>
