package be.howest.ti.mars.logic.domain.data.measurements;

public class CalorieMeasurement extends Measurement{
    private int footsteps;
    private int caloriesBurnt;
    public CalorieMeasurement(String type, String datetime, float value, int footsteps, int caloriesBurnt) {
        super(type, datetime, value);
        this.footsteps = footsteps;
        this.caloriesBurnt = caloriesBurnt;
    }

    public int getFootsteps() {
        return footsteps;
    }

    public int getCaloriesBurnt() {
        return caloriesBurnt;
    }
}
