package be.howest.ti.mars.logic.chat.events;

public abstract class OutgoingEvent extends Event{

    private String message;

    public OutgoingEvent(EventType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
