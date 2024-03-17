package be.howest.ti.mars.logic.chat.events;

public abstract class Event {

    private EventType type;

    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
