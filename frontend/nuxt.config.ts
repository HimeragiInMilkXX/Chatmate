import tailwindcss from '@tailwindcss/vite'

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  colorMode: {
    preference: 'light',
    fallback: 'light'
  },

  modules: [
    '@nuxt/eslint',
    '@nuxt/ui',
    '@nuxtjs/google-fonts',
    'v-gsap-nuxt',
    '@nuxt/icon',
    '@nuxt/image',
    'nuxt-lucide-icons',
    'shadcn-nuxt'
  ],

  googleFonts: {

    families: { Inter: '200..900' },
    inject: true

  },

  devtools: {
    enabled: true
  },

  css: ['~/assets/css/main.css'],

  routeRules: {
    '/': { prerender: true }
  },

  compatibilityDate: '2025-01-15',

  vite: {
    plugins: [
      tailwindcss()
    ]
  },

  eslint: {
    config: {
      stylistic: false,
    }
  },

  shadcn: {
    /**
       * Prefix for all the imported component.
       * @default "Ui"
       */
    prefix: '',
    /**
       * Directory that the component lives in.
       * Will respect the Nuxt aliases.
       * @link https://nuxt.com/docs/api/nuxt-config#alias
       * @default "@/components/ui"
       */
    componentDir: '@/components/ui'
  },
  runtimeConfig: {

    public: {

      apiBase: "http://localhost:8081"

    }

  },
})
