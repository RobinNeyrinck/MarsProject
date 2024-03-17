package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.data.MedicalData;
import be.howest.ti.mars.logic.domain.data.measurements.Measurement;

import java.util.List;

public interface MarsController {

    List<User> getUsers();
    List<Appointment> getUserAppointments(int userID);

    List<Alert> getUserAlerts(int userID);

    void addFriend(int userID, int friendID);

    List<Recommendation> getRecommendations(int userID);

    User getUser(int userID);

    User getUser(String username);

    List<User> getUserFriends(int userID);

    User getUserFriendByID(int userID, int friendID);


    MedicalData getUserMedical(int userID);

    void removeFriend(int userID, int friendID);

    void createMeasurement(int userID, Measurement measurement);

    void sendAlertToUser(int userID, Alert alert);

    void createAppointment(int userID, Appointment appointment);

    void sendMessage(int userID, int friendID, String msg);

    List<Message> getUserMessages(int userID, int friendID);

    List<MedicalData> getAllMedical();
}
