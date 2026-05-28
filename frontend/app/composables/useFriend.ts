import type { ApiResponse, FriendshipData, FriendshipReturn } from "~/exports/types";

export const useFriend = () => {

  const { $api } = useNuxtApp();

  const sendFriendRequest = async ( id: string ) => {

    const res: FriendshipReturn = await $api.apiFetch_Token( `/friendship/request/${id}`, { method: "POST" } );

    return res;

  }

  const acceptFriendRequest = async ( id: string ) => {

    const res: FriendshipReturn = await $api.apiFetch_Token( `/friendship/accept/${id}`, { method: "PATCH" } );

    return res;

  }

  const getFriendshipStatus = async ( id: string ) => {

    const res: FriendshipReturn = await $api.apiFetch_Token( `/friendship/get/${id}`, { method: "GET" } );

    return res;

  }

  const getFriendshipById = async ( id: string ) => {

    const res: FriendshipReturn = await $api.apiFetch_Token( `/friendship/getship/${id}`, { method: "GET" } );

    return res;

  }

  const getFriendships = async () => {

    const res: ApiResponse<FriendshipData[]> = await $api.apiFetch_Token( `/friendship/get/accepted`, { method: "GET" } );

    return res;

  }

  const removeFriendship = async ( id: string ) => {

    const res: ApiResponse<FriendshipData[]> = await $api.apiFetch_Token( `/friendship/delete/${id}`, { method: "DELETE" } );

    return res;

  }

  return { sendFriendRequest, acceptFriendRequest, getFriendshipStatus, getFriendshipById, getFriendships, removeFriendship };

}
