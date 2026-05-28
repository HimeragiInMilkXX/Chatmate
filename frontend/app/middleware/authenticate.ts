export default defineNuxtRouteMiddleware( async () => {

    const { $api } = useNuxtApp();

    try { await $api.me() } catch { return navigateTo('/auth/login') }

})
