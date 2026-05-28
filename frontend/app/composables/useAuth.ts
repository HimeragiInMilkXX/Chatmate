import type { AuthReturn } from "~/exports/types";
import { useLoggedIn, useUserData } from "~/states/authStates";

type LoginData = { usernameOrEmail: string; password: string };
type RegisterData = { username: string, email: string, password: string, confirmPassword: string }
type ResetPasswordData = { oldPassword: string, newPassword: string, confirmPassword: string }


export const useAuth = () => {

  const { $api } = useNuxtApp();
  const satoken = useCookie("satoken");
  const loggedIn = useLoggedIn();
  const userData = useUserData();

  const login = async ( data: LoginData ) => {

    const res: AuthReturn = await $api.apiFetch_noToken( `/auth/login`, { method: "POST", body: data } );

    if( res.success ) satoken.value = null;

    $api.updateStates( res );
    return res;

  }

  const register = async ( data: RegisterData ) => {

    const res: AuthReturn = await $api.apiFetch_noToken( `/auth/register`, { method: "POST", body: data } );

    if( res.success ) satoken.value = null;

    $api.updateStates( res );
    return res;

  }

  const logout = async () => {

    navigateTo("/auth/login")

    const res: AuthReturn = await $api.apiFetch_Token( `/auth/logout`, { method: "POST" } );

    if( res.success ) {

      satoken.value = null;
      loggedIn.value = false;
      userData.value = null;

    }

  }

  const resetPassword = async ( data: ResetPasswordData ) => {

    const res: AuthReturn = await $api.apiFetch_Token( `/auth/resetPassword`, { method: "POST", body: data } );

    if( res.success ) alert( "Password reset successful, please login again!" );

    $api.updateStates( res );
    return res;

  }

  const me = async () => {

    const res: AuthReturn = await $api.apiFetch_Token( `/auth/me`, { method: "GET" } );

    $api.updateStates( res );
    return res;

  }

  return { login, register, logout, resetPassword, me }

}
