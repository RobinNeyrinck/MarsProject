package be.howest.ti.mars.logic.chat.events;

public class BroadcastEvent extends OutgoingEvent{

    public BroadcastEvent(String message) {
        super(EventType.BROADCAST, message);
    }
}
