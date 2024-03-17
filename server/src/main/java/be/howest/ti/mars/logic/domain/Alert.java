package be.howest.ti.mars.logic.domain;

public class Alert {
    private final int alertID;

    private final AlertType type;
    private final String name;
    private final String description;
    private final int urgency;
    private static final int MIN_URGENCY = 1;
    private static final int MAX_URGENCY = 5;
    private final String location;

    public enum AlertType{
        EMERGENCY, NEW_FRIEND
    }

    public Alert(int alertID, AlertType type, String name, String description, int urgency, String location) {
        this.alertID = alertID;
        this.type = type;
        this.name = name;
        this.description = description;
        this.location = location;
        if (urgency > MAX_URGENCY || urgency < MIN_URGENCY) throw new IllegalArgumentException("Not a valid urgency input!");
        this.urgency = urgency;
    }

    public int getAlertID() {
        return alertID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getUrgency() {
        return urgency;
    }

    public String getLocation() {
        return location;
    }

    public AlertType getType() {
        return type;
    }
}
