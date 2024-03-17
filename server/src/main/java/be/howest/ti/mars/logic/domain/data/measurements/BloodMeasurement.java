package be.howest.ti.mars.logic.domain.data.measurements;

public class BloodMeasurement extends Measurement{
    private float bloodSugarLevel;
    private float bloodPressure;
    public BloodMeasurement(String type, String datetime, float value, float bloodSugarLevel, float bloodPressure) {
        super(type, datetime, value);
        this.bloodSugarLevel = bloodSugarLevel;
        this.bloodPressure = bloodPressure;
    }

    public float getBloodSugarLevel() {
        return bloodSugarLevel;
    }

    public float getBloodPressure() {
        return bloodPressure;
    }
}
