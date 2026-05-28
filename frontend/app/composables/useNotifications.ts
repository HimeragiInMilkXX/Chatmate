import type { ApiResponse, NotificationData } from "~/exports/types";
import { useUserData } from "~/states/authStates";

export const useNotifications = () => {

  const { $api } = useNuxtApp();

  function buildEndpoint( path: string ): string {

    return "/notification" + path;
  }

  const sendNotificationTo = async ( userId: string, content: string, link: string ) => {

    const body = {

      userId: userId,
      content: content,
      link: link,

    }

    const res: ApiResponse<NotificationData> = await $api.apiFetch_noToken( buildEndpoint('/create'), { method: 'POST', body: body } )

    return res;

  }

  const getNotifications = async () => {

    const loggedUserId = useUserData().value?.id;

    const res: ApiResponse<NotificationData[]> = await $api.apiFetch_Token( buildEndpoint(`/user/${loggedUserId}`), { method: "GET" } );

    return res;

  }

  const confirmNotification = async ( notificationId: string ) => {

    const res: ApiResponse<NotificationData> = await $api.apiFetch_Token( buildEndpoint(`/confirm/${notificationId}`), { method: "PATCH" } );

    return res;

  }

  const deleteNotification = async ( notificationId: string ) => {

    const res: ApiResponse<NotificationData[]> = await $api.apiFetch_Token( buildEndpoint(`/delete/${notificationId}`), { method: "DELETE" } );

    return res;

  }

  return { sendNotificationTo, getNotifications, confirmNotification, deleteNotification };

}
