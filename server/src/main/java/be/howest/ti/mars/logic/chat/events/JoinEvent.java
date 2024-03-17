package be.howest.ti.mars.logic.chat.events;

public class JoinEvent extends IncomingEvent {
    String username;

    public JoinEvent(String clientId, String username) {
        super(EventType.JOIN, clientId);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
