package com.example.websocket.client;

import com.example.websocket.Message;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private String username;
    private MessageListener messageListener;

    public  MyStompSessionHandler(MessageListener messageListener, String username){
        this.username = username;
        this.messageListener = messageListener;
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("client connected");


        session.subscribe("/topic/users", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return new ArrayList<String>().getClass();
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {

                try {

                    if(payload instanceof ArrayList){
                        ArrayList<String> activeUsers = (ArrayList<String>) payload;
                        System.out.println("??" + payload);
                        messageListener.onActiveUsersUpdated(activeUsers);
                        System.out.println("Received Active Users: " + activeUsers);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof Message){

                        Message message = (Message) payload;
                        messageListener.onMessageReceived(message);
                        System.out.println("Received message: " + message.getUser() + ": " + message.getMessage());
                    }

                    else {
                        System.out.println("Unexpected payload: " + payload.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        session.send("/app/connect", username);
        session.send("/app/request-users", "");
        System.out.println("suscribed to topic/users");
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
            exception.printStackTrace();
    }

}
