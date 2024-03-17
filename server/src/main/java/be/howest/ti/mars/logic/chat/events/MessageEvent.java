package be.howest.ti.mars.logic.chat.events;

public class MessageEvent extends IncomingEvent{

    private String message;

    public MessageEvent(String clientId, String message) {
        super(EventType.MESSAGE, clientId);
        this.message = message;
    }

    public MessageEvent(EventType t, String clientId, String message) {
        super(t, clientId);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
