import type { AuthReturn } from "~/exports/types";
import { useLoggedIn, useUserData } from "~/states/authStates";

export default defineNuxtPlugin( () => {

  const satoken = useCookie("satoken");
  const loggedIn = useLoggedIn();
  const userData = useUserData();
  const nuxtApp = useNuxtApp();
  const toast = useToast();

  const apiFetch_noToken = $fetch.create({

      baseURL: useRuntimeConfig().public.apiBase,

      onResponseError({response}) {

          console.log( response );

      }

  })

  const apiFetch_Token = $fetch.create({

      baseURL: useRuntimeConfig().public.apiBase,

      onRequest({options}) {

          if( satoken.value ) {
              const headers = new Headers(options.headers);
              headers.set( "satoken", satoken.value );
              options.headers = headers;
          }

      },

      onResponseError({response}) {

          if( response.status === 401 ) {

              satoken.value = null;

          }

      }

  })

  const me = async () => {

    const res: AuthReturn = await apiFetch_Token( `/auth/me`, { method: "GET" } );

    updateStates( res );
    return res;

  }

  const updateStates = ( res: AuthReturn ) => {

  if( res.success ) {

    if( !satoken.value ) {
      console.log( satoken.value )
      satoken.value = res.data?.token;
    }

    loggedIn.value = true;
    userData.value = res.data?.user || null;

  }

  }

  return { provide: { api: { apiFetch_noToken, apiFetch_Token, me, updateStates } }}

})
