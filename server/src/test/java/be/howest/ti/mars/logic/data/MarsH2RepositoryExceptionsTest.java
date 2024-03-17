package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Alert;
import be.howest.ti.mars.logic.domain.Appointment;
import be.howest.ti.mars.logic.domain.data.measurements.BloodMeasurement;
import be.howest.ti.mars.logic.domain.data.measurements.CalorieMeasurement;
import be.howest.ti.mars.logic.domain.data.measurements.Measurement;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MarsH2RepositoryExceptionsTest {

    private static final String URL = "jdbc:h2:./db-17";

    @Test
    void getH2RepoWithNoDbFails() {
        // Arrange
        Repositories.shutdown();

        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, Repositories::getH2Repo);
    }

    @Test
    void functionsWithSQLExceptionFailsNicely() {
        // Arrange
        int id = 1;
        int falseFriendId = -1;
        String name = "Alice";
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.shutdown();
        Repositories.configure(dbProperties);
        MarsH2Repository repo = Repositories.getH2Repo();
        repo.cleanUp();

        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUser(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUserAlerts(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.sendAlertToUser(falseFriendId, new Alert(1, Alert.AlertType.EMERGENCY, "Something", "He...", 1, "here")));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUserMedical(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUserAppointments(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.createAppointment(id, new Appointment(1, "appointment", "now", "here")));
        Assertions.assertThrows(RepositoryException.class, () -> repo.addFriend(id, id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUserRecommendations(id));
        Assertions.assertThrows(RepositoryException.class, repo::getUsers);
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUserFriends(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUser(name));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUserFriendByID(id, falseFriendId));
        Assertions.assertThrows(RepositoryException.class, () -> repo.removeFriend(id, falseFriendId));
        Assertions.assertThrows(RepositoryException.class, () -> repo.createMeasurement(falseFriendId, new Measurement("SomeMeasurement", "date", 50)));
        Assertions.assertThrows(RepositoryException.class, () -> repo.createBloodMeasurement(falseFriendId, new BloodMeasurement("SomeMeasurement", "date", 50, 50, 50)));
        Assertions.assertThrows(RepositoryException.class, () -> repo.createCalorieMeasurement(falseFriendId, new CalorieMeasurement("SomeMeasurement", "date", 50, 50, 50)));
        Assertions.assertThrows(RepositoryException.class, () -> repo.createSimpleMeasurement(falseFriendId, new Measurement("SomeMeasurement", "date", 50)));
        Assertions.assertThrows(RepositoryException.class, repo::getAllMedical);
    }


}
