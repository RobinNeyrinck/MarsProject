package be.howest.ti.mars.logic.domain;

public class Message {
    private final int receiverID;
    private final int senderID;
    private final String messageContent;
    private final String timestamp;

    public Message(int receiverID, int senderID, String message, String timestamp) {
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.messageContent = message;
        this.timestamp = timestamp;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public int getSenderID() {
        return senderID;
    }

    public String getMessage() {
        return messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
