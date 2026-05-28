import type { FriendshipData } from "~/exports/types";

export const splitDate = ( date: string | undefined ): string[] => {

  if( !date ) return [];

  const dateParts = date.split( "T" );

  return dateParts;

}

export const getChatPartnerData = ( friendshipData: FriendshipData, selfId: number ): { id: number, username: string } => {

  if( friendshipData.senderId === selfId ) return { id: friendshipData.receiverId, username: friendshipData.receiverUsername };
  else return { id: friendshipData.senderId, username: friendshipData.senderUsername };

}
