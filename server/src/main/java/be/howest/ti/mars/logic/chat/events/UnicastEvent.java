package be.howest.ti.mars.logic.chat.events;

public class UnicastEvent extends OutgoingEvent{

    private String recipient;

    public UnicastEvent(String recipient, String message) {
        super(EventType.UNICAST, message);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}
