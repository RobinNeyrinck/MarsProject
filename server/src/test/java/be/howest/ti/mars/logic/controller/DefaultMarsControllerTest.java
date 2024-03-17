package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Alert;
import be.howest.ti.mars.logic.domain.Appointment;
import be.howest.ti.mars.logic.domain.data.measurements.Measurement;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:./db-17";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:./db-17",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }


    @Test
    void getUsersSuccess() {
        MarsController sut = new DefaultMarsController();

        assertEquals("bruh", sut.getUsers().get(0).getFirstname());
    }

    @Test
    void getUserByIDSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;

        assertEquals("bruh", sut.getUser(userID).getFirstname());
    }

    @Test
    void getUserByIDFail() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;

        assertNull(sut.getUser(userID));
    }

    @Test
    void getAlertsSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;

        assertEquals(1, sut.getUserAlerts(userID).size());
    }

    @Test
    void getAlertsFail() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;
        assertThrows(NoSuchElementException.class, () -> sut.getUserAlerts(userID));
    }

    @Test
    void getRecommendationsSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;

        assertEquals(0, sut.getRecommendations(userID).size());
    }

    @Test
    void getRecommendationsFail() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.getRecommendations(userID));
    }

    @Test
    void getFriendsSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;

        assertEquals(4, sut.getUserFriends(userID).size());
    }

    @Test
    void getFriendsFail() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.getUserFriends(userID));
    }

    @Test
    void addFriendsSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 4;
        int friendID = 5;
        assertEquals(0, sut.getUserFriends(userID).size());
        sut.addFriend(userID, friendID);
        assertEquals(1, sut.getUserFriends(userID).size());
    }

    @Test
    void addFriendFailed() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = 2;
        Assertions.assertThrows(NoSuchElementException.class, () -> sut.addFriend(userID, friendID));

    }

    @Test
    void addNonExistingFriend() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;
        int friendID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.addFriend(userID, friendID));
    }

    @Test
    void getMedicalSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;

        assertNotNull(sut.getUserMedical(userID));
    }

    @Test
    void getMedicalFail() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.getUserMedical(userID));
    }

    @Test
    void measureSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;

        assertEquals(0, sut.getUserMedical(userID).getMeasurements().size());
        sut.createMeasurement(userID, new Measurement("StressMeasurement", "00-00-00 00:00:00", 50));
        assertEquals(1, sut.getUserMedical(userID).getMeasurements().size());
    }

    @Test
    void measureFailUserDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.createMeasurement(userID, new Measurement("StressMeasurement", "00-00-00 00:00:00", 50)));
    }

    @Test
    void measureFailMedicalDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = 5;

        assertThrows(NoSuchElementException.class, () -> sut.createMeasurement(userID, new Measurement("StressMeasurement", "00-00-00 00:00:00", 50)));
    }

    @Test
    void getUserMessagesFailUserDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;
        int friendID = 2;

        assertThrows(NoSuchElementException.class, () -> sut.getUserMessages(userID, friendID));
    }

    @Test
    void getUserMessagesFailFriendDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.getUserMessages(userID, friendID));
    }

    @Test
    void sendMessagesFailUserDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;
        int friendID = 2;

        assertThrows(NoSuchElementException.class, () -> sut.sendMessage(userID, friendID, "ORA ORA ORA!"));
    }

    @Test
    void sendMessagesFailFriendDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.sendMessage(userID, friendID, "ORA ORA ORA!"));
    }

    @Test
    void sendMessagesFailMessageBlank() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = 2;

        assertThrows(IllegalArgumentException.class, () -> sut.sendMessage(userID, friendID, ""));
    }

    @Test
    void getUserMessagesSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = 2;

        assertNotNull(sut.getUserMessages(userID, friendID));
    }

    @Test
    void sendMessageSuccess() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = 2;
        String msg = "Daga kotowaru!";

        assertEquals(3, sut.getUserMessages(userID, friendID).size());
        sut.sendMessage(1, 2, msg);
        assertEquals(4, sut.getUserMessages(userID, friendID).size());
    }

    @Test
    void getAppointmentsSuccessful() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        assertEquals(0, sut.getUserAppointments(userID).size());
    }

    @Test
    void getAppointmentsFailed() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;
        assertThrows(NoSuchElementException.class, () -> sut.getUserAppointments(userID));
    }

    @Test
    void getUser() {
        MarsController sut = new DefaultMarsController();
        String name = "bruh-bruh";
        assertEquals("bruh", sut.getUser(name).getFirstname());
    }

    @Test
    void getFriendByIDSuccessfully() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = 2;
        assertEquals("Denji", sut.getUserFriendByID(userID, friendID).getFirstname());
    }

    @Test
    void getFriendByIDUserDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;
        int friendID = 2;
        assertThrows(NoSuchElementException.class, () -> sut.getUserFriendByID(userID, friendID));
    }

    @Test
    void getFriendByIDFriendDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = -1;
        assertThrows(NoSuchElementException.class, () -> sut.getUserFriendByID(userID, friendID));
    }

    @Test
    void removeFriendSuccessfully() {
        MarsController sut = new DefaultMarsController();
        int userID = 5;
        int friendID = 4;
        assertEquals(0, sut.getUserFriends(userID).size());
        sut.addFriend(userID, friendID);
        assertEquals(1, sut.getUserFriends(userID).size());
        sut.removeFriend(userID, friendID);
        assertEquals(0, sut.getUserFriends(userID).size());
    }

    @Test
    void removeFriendUserDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;
        int friendID = 2;
        assertThrows(NoSuchElementException.class, () -> sut.removeFriend(userID, friendID));
    }

    @Test
    void removeFriendFriendDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;
        int friendID = -1;
        assertThrows(NoSuchElementException.class, () -> sut.removeFriend(userID, friendID));
    }

    @Test
    void sendAlertSuccessfully() {
        MarsController sut = new DefaultMarsController();
        int userID = 5;

        assertEquals(0, sut.getUserAlerts(userID).size());
        sut.sendAlertToUser(userID, new Alert(1, Alert.AlertType.EMERGENCY, "Nice", "Emergency", 1, "here"));
        assertEquals(1, sut.getUserAlerts(userID).size());
    }

    @Test
    void sendAlertFailedUserDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.sendAlertToUser(userID, new Alert(1, Alert.AlertType.EMERGENCY, "Nice", "Emergency", 1, "here")));
    }

    @Test
    void createAppointmentSuccessfully() {
        MarsController sut = new DefaultMarsController();
        int userID = 1;

        assertEquals(0, sut.getUserAppointments(userID).size());
        sut.createAppointment(userID, new Appointment(1, "something", "now", "here"));
        assertEquals(1, sut.getUserAppointments(userID).size());
    }

    @Test
    void createAppointmentFailedUserDoesNotExist() {
        MarsController sut = new DefaultMarsController();
        int userID = -1;

        assertThrows(NoSuchElementException.class, () -> sut.createAppointment(userID, new Appointment(1, "something", "now", "here")));
    }

    @Test
    void getAllMedicalDataSucces() {
        MarsController sut = new DefaultMarsController();

        assertNotNull(sut.getAllMedical());
    }
}
