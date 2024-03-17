package be.howest.ti.mars.logic.util;

import be.howest.ti.mars.logic.domain.data.measurements.BloodMeasurement;
import be.howest.ti.mars.logic.domain.data.measurements.HeartRateMeasurement;
import be.howest.ti.mars.logic.domain.data.measurements.Measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MedicalCalculator {

    public static final int MAX_SCORE = 100;

    private MedicalCalculator(){

    }
    public static final int HEALTHY_HEART_RATE = 60;
    public static final int HEALTHY_BLOOD_PRESSURE = 120;
    public static final int HEALTHY_BLOOD_SUGAR_LEVEL = 99;

    public static float calculateStressScore(List<Double> input){
        return toPercentage(100 - calculateAverage(input));
    }

    public static float calculateHormoneScore(List<Double> input){
        return toPercentage(calculateAverage(input));
    }

    public static float calculateHeartRateScore(List<Double> input){
        return calculateInverse(calculateAverage(input)/ HEALTHY_HEART_RATE);
    }

    public static float calculateBloodPressureScore(List<Double> input){
        return calculateInverse(calculateAverage(input)/ HEALTHY_BLOOD_PRESSURE);
    }

    public static float calculateBloodSugarLevelScore(List<Double> input){
        return calculateInverse(calculateAverage(input)/ HEALTHY_BLOOD_SUGAR_LEVEL);
    }

    private static float calculateAverage(List<Double> input){
        return (float) input.stream().mapToDouble(e -> e).average().orElse(0);
    }

    public static float calculateHealthScore(List<Measurement> measurements){
        if (measurements.isEmpty()){
            return 0;
        }
        List<Double> bloodPressureMeasurements = getBloodPressureMeasurements(measurements);
        List<Double> bloodSugarLevelMeasurements = getBloodSugarLevelMeasurements(measurements);
        List<Double> stressMeasurements = getStressMeasurements(measurements);
        List<Double> hormoneMeasurements = getHormoneMeasurements(measurements);
        List<Double> heartRateMeasurement = getHeartRateMeasurements(measurements);

        double bloodPressureScore = calculateBloodPressureScore(bloodPressureMeasurements);
        double bloodSugarLevelScore = calculateBloodSugarLevelScore(bloodSugarLevelMeasurements);
        double stressScore = calculateStressScore(stressMeasurements);
        double hormoneScore = calculateHormoneScore(hormoneMeasurements);
        double heartRateScore = calculateHeartRateScore(heartRateMeasurement);

        List<Double> scores = new ArrayList<>(List.of(bloodPressureScore, bloodSugarLevelScore, stressScore, hormoneScore, heartRateScore));
        if (calculateAverage(scores) > MAX_SCORE){
            return MAX_SCORE;
        }
        return calculateAverage(scores) * 100;
    }

    private static List<Double> getBloodPressureMeasurements(List<Measurement> measurements){
        return measurements.stream()
                .filter(e -> Objects.equals(e.getType(), "BloodMeasurement"))
                .map(BloodMeasurement.class::cast)
                .mapToDouble(BloodMeasurement::getBloodPressure).boxed().collect(Collectors.toList());
    }

    private static List<Double> getHeartRateMeasurements(List<Measurement> measurements){
        return measurements.stream()
                .filter(e -> Objects.equals(e.getType(), "HeartRateMeasurement"))
                .map(HeartRateMeasurement.class::cast)
                .mapToDouble(HeartRateMeasurement::getValue).boxed().collect(Collectors.toList());
    }
    private static List<Double> getBloodSugarLevelMeasurements(List<Measurement> measurements){
        return measurements.stream()
                .filter(e -> Objects.equals(e.getType(), "BloodMeasurement"))
                .map(BloodMeasurement.class::cast)
                .mapToDouble(BloodMeasurement::getBloodSugarLevel).boxed().collect(Collectors.toList());
    }
    private static List<Double> getHormoneMeasurements(List<Measurement> measurements){
        return measurements.stream()
                .filter(e -> Objects.equals(e.getType(), "HormoneMeasurement"))
                .mapToDouble(Measurement::getValue)
                .boxed()
                .collect(Collectors.toList());
    }
    private static List<Double> getStressMeasurements(List<Measurement> measurements){
        return measurements.stream()
                .filter(e -> Objects.equals(e.getType(), "StressMeasurement"))
                .mapToDouble(Measurement::getValue)
                .boxed()
                .collect(Collectors.toList());
    }
    private static float calculateInverse(float input){
        return 1 / input;
    }

    private static float toPercentage(float input){
        return input / 100;
    }
}
