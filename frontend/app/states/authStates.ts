import type { UserData } from "~/exports/types"

export const useLoggedIn = () => useState<boolean>('loggedIn', () => false )
export const useUserData = () => useState<UserData | null>('userData', () => null )
