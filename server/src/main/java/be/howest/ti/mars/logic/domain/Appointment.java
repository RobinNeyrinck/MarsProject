package be.howest.ti.mars.logic.domain;

public class Appointment {
    private final int appointmentID;
    private final String description;
    private final String date;
    private final String location;

    public Appointment(int appointmentID, String description, String date, String location) {
        this.appointmentID = appointmentID;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
