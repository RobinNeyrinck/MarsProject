package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.data.MedicalData;
import be.howest.ti.mars.logic.domain.data.measurements.Measurement;

import java.util.ArrayList;
import java.util.List;

public class MockMarsController implements MarsController {

    @Override
    public List<User> getUsers() {
        return new ArrayList<>();
    }

    @Override
    public List<Appointment> getUserAppointments(int userID) {
        return new ArrayList<>();
    }

    @Override
    public List<Alert> getUserAlerts(int userID) {
        return new ArrayList<>();
    }

    @Override
    public void addFriend(int userID, int friendID) {

    }

    @Override
    public User getUser(int userID) {
        return new User("Adam", "Eve");
    }

    @Override
    public User getUser(String username) {
        return new User("Adam", "Eve");
    }

    @Override
    public List<User> getUserFriends(int userID) {
        return new ArrayList<>();
    }

    @Override
    public User getUserFriendByID(int userID, int friendID) {
        return new User("bruh", "bruh");
    }

    @Override
    public MedicalData getUserMedical(int userID) {
        return new MedicalData();
    }
    @Override
    public void removeFriend(int userID, int friendID) {


    }

    @Override
    public void createMeasurement(int userID, Measurement measurement) {

    }

    @Override
    public void sendAlertToUser(int userID, Alert alert) {

    }
    
    @Override
    public void createAppointment(int userID, Appointment appointment) {

    }

    @Override
    public void sendMessage(int userID, int friendID, String msg) {

    }

    @Override
    public List<Message> getUserMessages(int userID, int friendID) {
        return new ArrayList<>();
    }

    @Override
    public List<MedicalData> getAllMedical() {
        return new ArrayList<>();
    }

    @Override
    public List<Recommendation> getRecommendations(int userID) {
        return new ArrayList<>();
    }

}
