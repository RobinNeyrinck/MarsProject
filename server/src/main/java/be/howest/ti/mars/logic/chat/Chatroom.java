package be.howest.ti.mars.logic.chat;

import be.howest.ti.mars.logic.chat.events.*;

import java.util.*;

public class Chatroom {

    private Map<String, String> users = new HashMap<>();

    public OutgoingEvent handleEvent(IncomingEvent e) {
        OutgoingEvent result = null;
        EventType eventType = e.getType();
        switch (eventType) {
            case MESSAGE:
                result = handleMessageEvent((MessageEvent) e);
                break;
            case PRIVATE_MESSAGE:
                result = handlePrivateMessageEvent((PrivateMessageEvent) e);
                break;
            default:
                break;
        }
        return result;
    }

    private OutgoingEvent handleMessageEvent(MessageEvent e) {
        String outgoingMessage = e.getMessage();
        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);
    }
    private OutgoingEvent handlePrivateMessageEvent(PrivateMessageEvent e) {
        String outgoingMessage = "";
        String senderId = e.getClientId();
        String sender = users.get(senderId);
        String recipient = e.getRecipient();
        String recipientId = "";

        for (Map.Entry<String, String> entry : users.entrySet()) {
            if (entry.getValue().equals(recipient)) {
                recipientId = entry.getKey();
            }
        }

        outgoingMessage = String.format("<em>From %s to %s </em>: %s", sender, recipient, e.getMessage());
        return EventFactory.getInstance().createMulticastEvent(recipientId, senderId, outgoingMessage);
    }

}
