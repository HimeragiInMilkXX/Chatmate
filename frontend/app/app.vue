<script setup>
	useHead({
    meta: [{ name: "viewport", content: "width=device-width, initial-scale=1" }],
    link: [{ rel: "icon", href: "/icon.png" }],
    htmlAttrs: {
      lang: "en",
    },
	});

	const title = "Pingmate";

	useSeoMeta({
    title,
    ogTitle: title,
	});

</script>

<template>

    <NuxtLayout>

      <UMain class="min-h-0!">
        <NuxtPage />
      </UMain>

    </NuxtLayout>

		<!-- <UError
      :error="{
        statusCode: 404,
        statusMessage: 'Page not found',
        message: 'The page you are looking for does not exist.',
      }"
		/> -->

</template>
