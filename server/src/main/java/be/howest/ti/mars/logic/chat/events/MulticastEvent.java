package be.howest.ti.mars.logic.chat.events;

public class MulticastEvent extends OutgoingEvent{
    private String sender;
    private String recipient;

    public MulticastEvent(String recipient, String sender, String message) {
        super(EventType.MULTICAST, message);
        this.recipient = recipient;
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }
    public String getSender() {
        return sender;
    }
}
