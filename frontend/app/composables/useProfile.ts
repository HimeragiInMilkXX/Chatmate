import type { ApiResponse, AuthReturn, ProfileData, UserData } from "~/exports/types";
import { useUserData } from "~/states/authStates";

export const useProfile = () => {

  const { $api } = useNuxtApp();

  const uploadAvatar = async ( file: File ) => {

    const formData = new FormData();
    formData.append( "file", file );

    const res: AuthReturn = await $api.apiFetch_Token( `/auth/avatar`, { method: "POST", body: formData } );

    $api.updateStates( res );
    return res;

  }

  const updateProfile = async ( profile: ProfileData, username?: string ) => {

    const data: ProfileData & { username?: string } = { ...profile };
    if( username ) data.username = username;

    const res: { success: boolean, message: string, data: UserData} = await $api.apiFetch_Token( `/auth/update`, { method: "PATCH", body: data } );

    if( res.success ) useUserData().value = res.data || null;

    return res;

  }

  const getProfilePhoto = ( url: string ): string => {

    return useRuntimeConfig().public.apiBase + url;

  }

  const getUserProfileById = async ( id: string ) => {

    const res: ApiResponse<UserData> = await $api.apiFetch_noToken( `/user/get/${id}`, { method: "GET" } )

    return res.data as UserData;

  }

  return { uploadAvatar, updateProfile, getUserProfileById, getProfilePhoto };

}
