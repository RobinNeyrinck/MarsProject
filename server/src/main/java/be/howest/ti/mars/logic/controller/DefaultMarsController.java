package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.data.MedicalData;
import be.howest.ti.mars.logic.domain.data.measurements.Measurement;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * The controller shouldn't even know that it is used in an API context..
 *
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Keep libraries and frameworks out of the logic packages as much as possible.
 * Do not be afraid to create your own Java classes if needed.
 */
public class DefaultMarsController implements MarsController {
    private static final String MSG_USER_UNKNOWN = "No user with id: %d";

    @Override
    public List<User> getUsers() {
        List<User> users = Repositories.getH2Repo().getUsers();
        if (users.isEmpty()){
            throw new NoSuchElementException("No users found");
        }
        return users;
    }

    @Override
    public List<Appointment> getUserAppointments(int userID) {
        if (userDoesNotExist(userID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        return Repositories.getH2Repo().getUserAppointments(userID);
    }

    @Override
    public List<Alert> getUserAlerts(int userID) {
        if (userDoesNotExist(userID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        return Repositories.getH2Repo().getUserAlerts(userID);
    }

    @Override
    public void addFriend(int userID, int friendID) {
        if (userDoesNotExist(userID) || userDoesNotExist(friendID) || userID == friendID){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        Repositories.getH2Repo().addFriend(userID, friendID);
    }

    @Override
    public User getUser(int userID) {
        return Repositories.getH2Repo().getUser(userID);
    }

    @Override
    public User getUser(String username) {
        return Repositories.getH2Repo().getUser(username);
    }

    @Override
    public List<User> getUserFriends(int userID) {
        if (userDoesNotExist(userID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        return Repositories.getH2Repo().getUserFriends(userID);
    }

    @Override
    public User getUserFriendByID(int userID, int friendID) {
        if (userDoesNotExist(userID) || userDoesNotExist(friendID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        return Repositories.getH2Repo().getUserFriendByID(userID, friendID);
    }

    @Override
    public MedicalData getUserMedical(int userID) {
        if (userDoesNotExist(userID)) {
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        return Repositories.getH2Repo().getUserMedical(userID);
    }

    @Override
    public List<MedicalData> getAllMedical() {
        List<MedicalData> medicalData = Repositories.getH2Repo().getAllMedical();
        if (medicalData.isEmpty()){
            throw new NoSuchElementException("No medical data found");
        }
        return medicalData;
    }

    public void removeFriend(int userID, int friendID) {
        if (userDoesNotExist(userID) || userDoesNotExist(friendID)) {
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        Repositories.getH2Repo().removeFriend(userID, friendID);
    }

    @Override
    public void createMeasurement(int userID, Measurement measurement) {
        if (userDoesNotExist(userID) || userMedicalDoesNotExist(userID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        Repositories.getH2Repo().createMeasurement(userID, measurement);
    }

    @Override
    public void sendAlertToUser(int userID, Alert alert) {
        if (userDoesNotExist(userID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        Repositories.getH2Repo().sendAlertToUser(userID, alert);
    }
    @Override
    public void createAppointment(int userID, Appointment appointment) {
        if (userDoesNotExist(userID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        Repositories.getH2Repo().createAppointment(userID, appointment);
    }

    @Override
    public void sendMessage(int userID, int friendID, String msg) {
        if (userDoesNotExist(userID) || userDoesNotExist(friendID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        if (msg.isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        Repositories.getH2Repo().sendMessage(userID, friendID, msg);
    }

    @Override
    public List<Message> getUserMessages(int userID, int friendID) {
        if (userDoesNotExist(userID) || userDoesNotExist(friendID)){
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        return Repositories.getH2Repo().getUserMessages(userID, friendID);
    }

    private boolean userDoesNotExist(int userID) {
        return getUser(userID) == null;
    }

    private boolean userMedicalDoesNotExist(int userID) {
        return getUserMedical(userID) == null;
    }

    @Override
    public List<Recommendation> getRecommendations(int userID) {
        if (userDoesNotExist(userID)) {
            throw new NoSuchElementException(String.format(MSG_USER_UNKNOWN, userID));
        }
        return Repositories.getH2Repo().getUserRecommendations(userID);
    }
}