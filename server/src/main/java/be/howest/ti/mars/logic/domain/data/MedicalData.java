package be.howest.ti.mars.logic.domain.data;

import be.howest.ti.mars.logic.domain.data.measurements.Measurement;

import java.util.ArrayList;
import java.util.List;

public class MedicalData {

    public static final int NOT_ASSIGNED = -1;
    private int userID;
    private String chronicDiseases;
    private String geneticDiseases;
    private float height;
    private float weight;
    private BloodType bloodType;
    private boolean pregnant;
    private String gender;
    private List<Integer> healthScores;

    private List<Measurement> measurements;


    private String allergies;

    private int age;

    private String birthdate;

    public MedicalData(int userID, String chronicDiseases, String geneticDiseases, float height, float weight, BloodType bloodType, boolean pregnant, String gender, List<Integer> healthScores, List<Measurement> measurements, String allergies, int age, String birthdate) {
        this.userID = userID;
        this.chronicDiseases = chronicDiseases;
        this.geneticDiseases = geneticDiseases;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
        this.pregnant = pregnant;
        this.gender = gender;
        this.healthScores = healthScores;
        this.measurements = measurements;
        this.allergies = allergies;
        this.age = age;
        this.birthdate = birthdate;
    }

    public MedicalData(){
        this(NOT_ASSIGNED, null, null, NOT_ASSIGNED, NOT_ASSIGNED, BloodType.A_POS, false, "male", new ArrayList<>(), new ArrayList<>(), null, NOT_ASSIGNED, null);
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public String getGeneticDiseases() {
        return geneticDiseases;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public boolean isPregnant() {
        return pregnant;
    }

    public String getGender() {
        return gender;
    }

    public List<Integer> getHealthScores() {
        return healthScores;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public int getUserID() {
        return userID;
    }

    public String getAllergies() {
        return allergies;
    }

    public int getAge() {
        return age;
    }

    public String getBirthdate() {
        return birthdate;
    }
}

