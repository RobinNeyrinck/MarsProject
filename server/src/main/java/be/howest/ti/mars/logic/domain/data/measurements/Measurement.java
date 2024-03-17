package be.howest.ti.mars.logic.domain.data.measurements;

public class Measurement {
    private String type;
    private String datetime;
    private float value;

    public Measurement(String type, String datetime, float value) {
        this.type = type;
        this.datetime = datetime;
        this.value = value;
    }

    public String getDatetime() {
        return datetime;
    }

    public float getValue() {
        return value;
    }
    public String getType() {
        return type;
    }
}
