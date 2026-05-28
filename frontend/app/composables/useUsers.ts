import type { ApiResponse, PageResponse, UserData, UserDataWithFriendCount } from "~/exports/types";

export const useUsers = () => {

  const { $api } = useNuxtApp();

  function buildEndpoint( path: string ): string { return "/user" + path; }

  const search = async ( keyword: string, page: number = 0, item: number = 10 ) => {

    const res: ApiResponse<PageResponse<UserData>> = await $api.apiFetch_Token( buildEndpoint(`/search/${keyword}?page=${page}&item=${item}`), { method: "GET" } );
    return res;

  }

  const getRecentLogin = async ( max: number = 10 ) => {

    const res: ApiResponse<UserData[]> = await $api.apiFetch_Token( buildEndpoint(`/rank/lastlogin/${max}`), { method: "GET" } );
    return res;

  }

  const getRankFriends = async ( page: number = 0, item: number = 10 ) => {

    const res: ApiResponse<PageResponse<UserDataWithFriendCount>> = await $api.apiFetch_Token( buildEndpoint( `/rank/friendships?page=${page}&item=${item}` ), { method: "GET" } );
    return res;

  }

  return { search, getRecentLogin, getRankFriends }

}
