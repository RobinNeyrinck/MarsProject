package be.howest.ti.mars.logic.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MedicalCalculatorTest {
    @Test
    void calculateStressScore(){
        List<Double> measurements = new ArrayList<>();
        measurements.add(50.);
        measurements.add(60.);
        measurements.add(70.);

        assertEquals(.4f, MedicalCalculator.calculateStressScore(measurements), 0.01);
    }

    @Test
    void calculateHormoneScore(){
        List<Double> measurements = new ArrayList<>();
        measurements.add(50.);
        measurements.add(60.);
        measurements.add(70.);

        assertEquals(.6f, MedicalCalculator.calculateHormoneScore(measurements), 0.01);
    }

    @Test
    void calculateHeartRateScore(){
        List<Double> measurements = new ArrayList<>();
        measurements.add(50.);
        measurements.add(60.);
        measurements.add(70.);

        assertEquals(1, MedicalCalculator.calculateHeartRateScore(measurements), 0.01);
    }

    @Test
    void calculateBloodPressureScore(){
        List<Double> measurements = new ArrayList<>();
        measurements.add(120.);
        measurements.add(130.);
        measurements.add(120.);

        assertEquals(.97f, MedicalCalculator.calculateBloodPressureScore(measurements), 0.01);
    }

    @Test
    void calculateBloodSugarLevelScore(){
        List<Double> measurements = new ArrayList<>();
        measurements.add(99.);
        measurements.add(90.);
        measurements.add(95.);

        assertEquals(1.04f, MedicalCalculator.calculateBloodSugarLevelScore(measurements), 0.01);
    }
}
