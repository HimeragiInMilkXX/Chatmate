export type AuthReturn = ApiResponse<{ token: string, user: UserData } | null>

export type FriendshipReturn = ApiResponse<FriendshipData>;

export type FriendshipData = {

  friendSince: string;
  id: string;
  receiverId: number;
  receiverUsername: string;
  senderId: number;
  senderUsername: string;
  status: "PENDING" | "ACCEPTED";

}

export type NotificationData = {

  id: string;
  userId: number;
  content: string;
  notificationAt: string;
  confirmed: boolean;
  link: string;

}

export type MessageData = {

  id: number;
  friendshipId: number;
  senderId: number;
  senderName: string;
  content: string;
  createdAt: string;

}

export type ApiResponse<T> = {

  success: boolean;
  message: string
  data: T;

}

export type UserData = {

  avatarUrl: string;
  createdAt: string;
  email: string;
  id: number;
  profile: ProfileData;
  updatedAt: string;
  username: string;
  lastLogin: string;

}

export type UserDataWithFriendCount = {

  user: UserData;
  friendshipCount: number;

}

export type ProfileData = {

  age?: undefined | number;
  bio?: undefined | string;
  birthday?: undefined | string;
  nationality?: undefined | string;
  interest: undefined | string;
  region: undefined | string;

}

export type SortInfo = {

  empty: boolean
  sorted: boolean
  unsorted: boolean

}

export type PageableInfo = {

  pageNumber: number
  pageSize: number
  sort: SortInfo
  offset: number
  paged: boolean
  unpaged: boolean

}

export type PageResponse<T> = {

  content: T[]
  pageable: PageableInfo
  totalElements: number
  totalPages: number
  last: boolean
  size: number
  number: number
  sort: SortInfo
  numberOfElements: number
  first: boolean
  empty: boolean

}
