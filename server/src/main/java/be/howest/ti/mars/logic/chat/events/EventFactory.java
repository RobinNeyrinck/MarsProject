package be.howest.ti.mars.logic.chat.events;

import io.vertx.core.json.JsonObject;

public class EventFactory {

    private static final EventFactory instance = new EventFactory();

    public static EventFactory getInstance() {
        return instance;
    }

    public IncomingEvent createIncomingEvent(JsonObject json) {
        EventType eventType = EventType.fromString(json.getString("type"));
        String clientId = json.getString("clientId");
        IncomingEvent event = new DiscardEvent(clientId);
        switch (eventType) {
            case MESSAGE:
                event = new MessageEvent(clientId, json.getString("message"));
                break;
            case PRIVATE_MESSAGE:
                event = new PrivateMessageEvent(clientId, json.getString("message"), json.getString("recipient"));
                break;
            case JOIN:
                event = new JoinEvent(clientId, json.getString("username"));
                break;
            default:
                break;
        }
        return event;
    }

    public BroadcastEvent createBroadcastEvent(String msg) {
        return new BroadcastEvent(msg);
    }

    public UnicastEvent createUnicastEvent(String recipient, String msg) {
        return new UnicastEvent(recipient, msg);
    }

    public MulticastEvent createMulticastEvent(String sender, String recipient, String msg) {
        return new MulticastEvent(recipient, sender, msg);
    }
}
