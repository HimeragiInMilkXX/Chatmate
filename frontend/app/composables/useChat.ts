import type { ApiResponse, MessageData } from "~/exports/types";
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

export const useChat = (friendshipId: number) => {

  const buildEndpoint = ( path: string ): string => { return `/chats${path}` }

  const { $api } = useNuxtApp();
  const satoken = useCookie("satoken")
  const messages = ref<MessageData[]>();

  let stompClient: Client;

  onMounted( async () => {

    // GET HISTORY
    const res: ApiResponse<MessageData[]> = await $api.apiFetch_Token( buildEndpoint( `/${friendshipId}/messages` ), { method: "GET" } );
    messages.value = res.data;

    // ESTABLISH CONNECTION TO WEBSOCKET
    stompClient = new Client( {

      webSocketFactory: () => new SockJS( useRuntimeConfig().public.apiBase + "/ws-chat" ),
      connectHeaders: { satoken: satoken.value },
      onConnect: () => {

        stompClient.subscribe( `/topic/chat/${friendshipId}`, ( message ) => {

          const receivedMessage: MessageData = JSON.parse( message.body );
          messages.value?.push( receivedMessage );

        } )

      }

    } )

    stompClient.activate();

  } )

  onBeforeUnmount( () => {

    if( stompClient ) stompClient.deactivate();

  })

  const sendMessage = ( content: string ) => {

    if( !content.trim() ) return;

    stompClient.publish( {

      destination: '/app/chat.send',
      body: JSON.stringify({

        friendshipId: Number(friendshipId),
        content: content

      })

    })

  }

  return { messages, sendMessage }

}
