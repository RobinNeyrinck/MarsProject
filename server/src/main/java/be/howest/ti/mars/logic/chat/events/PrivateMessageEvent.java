package be.howest.ti.mars.logic.chat.events;

public class PrivateMessageEvent extends IncomingEvent {
        private String message;
        private String recipient;

        public PrivateMessageEvent(String clientId, String message, String recipient) {
            super(EventType.PRIVATE_MESSAGE, clientId);
            this.message = message;
            this.recipient = recipient;
        }

        public PrivateMessageEvent(EventType type, String clientId, String message, String recipient) {
            super(type, clientId);
            this.message = message;
            this.recipient = recipient;
        }

        public String getMessage() {
            return message;
        }

        public String getRecipient() {
            return recipient;
        }
    }
