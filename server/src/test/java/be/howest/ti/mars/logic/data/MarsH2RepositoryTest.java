package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Alert;
import be.howest.ti.mars.logic.domain.Appointment;
import be.howest.ti.mars.logic.domain.data.measurements.StressMeasurement;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MarsH2RepositoryTest {
    private static final String URL = "jdbc:h2:./db-17";

    @BeforeEach
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @Test
    void getUsers() {
        Assertions.assertTrue(Repositories.getH2Repo().getUsers().size() > 0);
    }

    @Test
    void getUserByIDSuccessfully() {
        int userID = 1;
        Assertions.assertEquals("bruh", Repositories.getH2Repo().getUser(userID).getFirstname());
    }

    @Test
    void getUserByNameSuccessfully() {
        String name = "bruh-bruh";
        Assertions.assertEquals("bruh", Repositories.getH2Repo().getUser(name).getFirstname());
    }

    @Test
    void getUserAppointmentsSuccessfully() {
        int userID = 2;
        Assertions.assertTrue(Repositories.getH2Repo().getUserAppointments(userID).size() > 0);
    }

    @Test
    void createAppointmentSuccessfully() {
        int userID = 1;
        Assertions.assertEquals(0, Repositories.getH2Repo().getUserAppointments(userID).size());
        Repositories.getH2Repo().createAppointment(userID, new Appointment(-1, "Testing out our appointments!", "Right now!", "Right here!"));
        Assertions.assertEquals(1, Repositories.getH2Repo().getUserAppointments(userID).size());
    }

    @Test
    void getUserAlerts() {
        int userID = 2;
        Assertions.assertTrue(Repositories.getH2Repo().getUserAlerts(userID).size() > 0);
    }

    @Test
    void sendAlertToUserSuccessfully() {
        int userID = 1;
        Assertions.assertEquals(1, Repositories.getH2Repo().getUserAlerts(userID).size());
        Repositories.getH2Repo().sendAlertToUser(userID, new Alert(-1, Alert.AlertType.EMERGENCY, "Raf", "Servers liggen plat!", 5, "Howest"));
        Assertions.assertEquals(2, Repositories.getH2Repo().getUserAlerts(userID).size());
    }

    @Test
    void getUserRecommendations() {
        int userID = 2;
        Assertions.assertTrue(Repositories.getH2Repo().getUserRecommendations(userID).size() > 0);
    }

    @Test
    void getUserFriends() {
        int userID = 1;
        int friendID = 2;
        Assertions.assertEquals(4, Repositories.getH2Repo().getUserFriends(userID).size());
    }

    @Test
    void addFriend() {
        int userID = 4;
        int friendID = 5;
        Assertions.assertFalse(Repositories.getH2Repo().getUserFriends(userID).size() > 0);
        Repositories.getH2Repo().addFriend(userID, friendID);
        Assertions.assertTrue(Repositories.getH2Repo().getUserFriends(userID).size() > 0);
    }

    @Test
    void removeFriend() {
        int userID = 4;
        int friendID = 5;
        Assertions.assertFalse(Repositories.getH2Repo().getUserFriends(userID).size() > 0);
        Repositories.getH2Repo().addFriend(userID, friendID);
        Assertions.assertTrue(Repositories.getH2Repo().getUserFriends(userID).size() > 0);
        Repositories.getH2Repo().removeFriend(userID, friendID);
        Assertions.assertFalse(Repositories.getH2Repo().getUserFriends(userID).size() > 0);
    }

    @Test
    void getUserMedical() {
        int userID = 1;
        Assertions.assertEquals(180, Repositories.getH2Repo().getUserMedical(userID).getHeight(), 0.01);
    }

    @Test
    void getUserWithoutMedical() {
        int userID = 5;
        Assertions.assertNull(Repositories.getH2Repo().getUserMedical(userID));
    }

    @Test
    void createMeasurement() {
        int userID = 1;
        Assertions.assertEquals(0, Repositories.getH2Repo().getUserMedical(userID).getMeasurements().size());
        Repositories.getH2Repo().createMeasurement(userID, new StressMeasurement("StressMeasurement", "01-01-2023 00:00:00", 50));
        Assertions.assertEquals(1, Repositories.getH2Repo().getUserMedical(userID).getMeasurements().size());
    }

    @Test
    void sendMessage() {
        int userID = 1;
        int friendID = 2;
        String msg = "what da dog doing?";

        Assertions.assertEquals(3, Repositories.getH2Repo().getUserMessages(userID, friendID).size());
        Repositories.getH2Repo().sendMessage(userID, friendID, msg);
        Assertions.assertEquals(4, Repositories.getH2Repo().getUserMessages(userID, friendID).size());
    }

    @Test
    void getUserMessages() {
        int userID = 1;
        int friendID = 2;

        Assertions.assertNotNull(Repositories.getH2Repo().getUserMessages(userID, friendID));
    }

    @Test
    void getHealthScores() {
        int userID = 1;
        Assertions.assertEquals(0, Repositories.getH2Repo().getHealthScores(userID).size());
    }

    @Test
    void addHealthScores() {
        int userID = 1;
        Assertions.assertEquals(0, Repositories.getH2Repo().getHealthScores(userID).size());
        Repositories.getH2Repo().addHealthScore(userID, 75);
        Assertions.assertEquals(1, Repositories.getH2Repo().getHealthScores(userID).size());

    }

    @Test
    void getAllMedical() {
        Assertions.assertTrue(Repositories.getH2Repo().getAllMedical().size() > 0);
    }
}
