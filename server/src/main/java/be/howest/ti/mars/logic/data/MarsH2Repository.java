package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.data.BloodType;
import be.howest.ti.mars.logic.domain.data.MedicalData;
import be.howest.ti.mars.logic.domain.data.measurements.*;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import be.howest.ti.mars.logic.util.MedicalCalculator;
import io.vertx.codegen.annotations.Nullable;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start project there was no need for a Java interface MarsRepository.
Please always use interfaces when needed.

To make this class useful, please complete it with the topics seen in the module OOA & SD
 */

public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());
    private static final String SQL_GET_USERS = "select * from users";

    private static final String SQL_APPOINTMENTS_BY_ID = "select * from appointments where userID = ?";

    private static final String SQL_ALERTS_BY_ID = "select * from alerts where userID = ?";

    private static final String SQL_ADD_FRIEND = "insert into friends (userID, friendID) values (?, ?)";

    private static final String SQL_REMOVE_FRIEND = "delete from friends where userID = ? and friendID = ?";
    private static final String SQL_GET_USER = "select * from users where userID = ?";

    private static final String SQL_GET_USER_MEDICAL = "select * from medicaldata where userID = ?";

    private static final String SQL_GET_USER_MEASUREMENTS = "select * from measurements where userID = ?";
    private static final String SQL_GET_USER_FRIENDS = "select  * from friends where userID = ?";
    private static final String SQL_RECOMMENDATIONS_BY_ID = "select * from recommendations where userID = ?";
    private static final String SQL_GET_USER_BY_NAME = "select * from users where firstname = ? and lastname = ?";

    private static final String SQL_CREATE_SIMPLE_MEASUREMENT = "insert into measurements (userID, measurementtype, datetime, value) values (?,?,?,?)";

    private static final String SQL_CREATE_CALORIE_MEASUREMENT = "insert into measurements (userID, measurementtype, datetime, value, footsteps, caloriesburnt) values (?,?,?,?,?,?)";

    private static final String SQL_CREATE_BLOOD_MEASUREMENT = "insert into measurements (userID, measurementtype, datetime, value, bloodsugarlevel, bloodpressure) values (?,?,?,?,?,?)";
    private static final String SQL_CREATE_APPOINTMENT = "insert into appointments (userID, description, datetime, location) values (?,?,?,?)";
    public static final String FAILED_TO_CREATE_MEASUREMENT = "Failed to create measurement.";

    private static final String SQL_SEND_ALERT = "insert into alerts (userID, name, type, urgency, description, location) values (?,?,?,?,?,?)";
    private static final String FAILED_TO_SEND_ALERT = "Failed to send alert";

    private static final String FAILED_TO_CREATE_APPOINTMENT = "Failed to create appointment.";
    public static final String DATETIME_COLUMN = "datetime";
    public static final String LOCATION_COLUMN = "location";
    public static final String DESCRIPTION_COLUMN = "description";
    private static final String SQL_GET_USER_MESSAGES = "select * from messages where (receiverID = ? and senderID = ?) or (receiverID = ? and senderID = ?)";
    private static final String SQL_SEND_MESSAGE = "insert into messages (receiverID, senderID, message, timestamp) values (?,?,?,?)";
    private static final String SQL_GET_USER_HEALTHSCORES = "select * from healthscores where userID = ?";
    private static final String SQL_INSERT_HEALTHSCORE = "insert into healthscores (userID, healthscore) values (?,?)";
    private static final String SQL_GET_ALL_MEDICAL = "select * from medicaldata";

    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    private static final String GET_VALUE_COLUMN = "value";
    private static final String GET_DATETIME_COLUMN = "datetime";

    private static final String GET_TYPE_COLUMN = "measurementtype";

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database web console started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    public List<Appointment> getUserAppointments(int id) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_APPOINTMENTS_BY_ID)
        ) {
            stmt.setInt(1, id);
            List<Appointment> appointments = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(rs.getInt("appointmentID"), rs.getString(DESCRIPTION_COLUMN), rs.getString(DATETIME_COLUMN), rs.getString(LOCATION_COLUMN)));
                }
                return appointments;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get appointments.", ex);
            throw new RepositoryException("Could not get appointments.");
        }
    }

    public List<Alert> getUserAlerts(int id) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALERTS_BY_ID)
        ) {
            stmt.setInt(1, id);
            List<Alert> alerts = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alerts.add(new Alert(rs.getInt("alertID"), Alert.AlertType.valueOf(rs.getString("type")), rs.getString("name"), rs.getString(DESCRIPTION_COLUMN), rs.getInt("urgency"), rs.getString(LOCATION_COLUMN)));
                }
                return alerts;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get alerts.", ex);
            throw new RepositoryException("Could not get alerts.");
        }
    }

    public List<Recommendation> getUserRecommendations(int userID) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_RECOMMENDATIONS_BY_ID)
        ) {
            stmt.setInt(1, userID);
            List<Recommendation> recommendations = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recommendations.add(new Recommendation(rs.getInt("recommendationID"), rs.getString("activityname")));
                }
                return recommendations;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get recommendations", ex);
            throw new RepositoryException("Could not get recommendations");
        }
    }

    public List<User> getUsers() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USERS)
        ) {
            List<User> users = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getInt("userID"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("avatar"));
                    users.add(user);

                }
                return users;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get users.", ex);
            throw new RepositoryException("Could not get users.");
        }
    }


    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();

        try {
            Files.deleteIfExists(Path.of("./db-17.mv.db"));
            Files.deleteIfExists(Path.of("./db-17.trace.db"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Database cleanup failed.", e);
            throw new RepositoryException("Database cleanup failed.");
        }
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql)
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void addFriend(int userID, int friendID) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ADD_FRIEND)
        ) {
            if (checkIfFriendExists(userID, friendID)) {
                throw new NoSuchElementException("This friend has already been added");
            }
            stmt.setInt(1, userID);
            stmt.setInt(2, friendID);
            stmt.execute();
            stmt.setInt(2, userID);
            stmt.setInt(1, friendID);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to add friend.", ex);
            throw new RepositoryException("Could not add friend.");
        }
    }

    private boolean checkIfFriendExists(int userID, int friendID) {
        return getUserFriends(userID).stream().anyMatch(e -> e.getUserID() == friendID);
    }

    public User getUser(int userID) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER)
        ) {
            stmt.setInt(1, userID);
            return getUserFromDatabase(stmt);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get user.", ex);
            throw new RepositoryException("Could not get user.");
        }
    }

    public User getUser(String username) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER_BY_NAME)
        ) {
            String[] name = username.split("-");

            String firstname = name[0];
            String lastname = name[1];

            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            return getUserFromDatabase(stmt);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get user", ex);
            throw new RepositoryException("Could not get user");
        }
    }

    @Nullable
    private User getUserFromDatabase(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                User user = new User(rs.getString("firstname"), rs.getString("lastname"), rs.getString("avatar"));
                user.setUserID(rs.getInt("userID"));
                return user;
            } else {
                return null;
            }
        }
    }

    public List<User> getUserFriends(int userID) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER_FRIENDS)
        ) {
            stmt.setInt(1, userID);
            List<User> friends = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    friends.add(new User(getUser(rs.getInt("friendID"))));
                }
                return friends;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get user friends.", ex);
            throw new RepositoryException("Could not get user friends.");
        }
    }

    public User getUserFriendByID(int userID, int friendID) {
        if (!checkIfFriendExists(userID, friendID)) {
            throw new NoSuchElementException("You are not friends with this person!");
        }
        return getUser(friendID);
    }

    public MedicalData getUserMedical(int userID) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER_MEDICAL)
        ) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new MedicalData(rs.getInt("userid"), rs.getString("chronicdiseases"), rs.getString("geneticdiseases"), rs.getFloat("height"), rs.getFloat("weight"), BloodType.valueOf(rs.getString("bloodtype")), rs.getBoolean("pregnant"), rs.getString("gender"), getHealthScores(userID), getUserMeasurements(userID), rs.getString("allergies"), rs.getInt("age"), rs.getString("birthdate"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get medical data.", ex);
            throw new RepositoryException("Could not get medical data.");
        }
    }
    private List<Measurement> getUserMeasurements(int userID){
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER_MEASUREMENTS)
        ) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Measurement> measurements = new ArrayList<>();
                while (rs.next()) {
                    switch (rs.getString(GET_TYPE_COLUMN)) {
                        case "BloodMeasurement":
                            measurements.add(new BloodMeasurement(rs.getString(GET_TYPE_COLUMN), rs.getString(GET_DATETIME_COLUMN), rs.getFloat(GET_VALUE_COLUMN), rs.getFloat("bloodsugarlevel"), rs.getFloat("bloodpressure")));
                            break;
                        case "CalorieMeasurement":
                            measurements.add(new CalorieMeasurement(rs.getString(GET_TYPE_COLUMN),rs.getString(GET_DATETIME_COLUMN), rs.getFloat(GET_VALUE_COLUMN), rs.getInt("footsteps"), rs.getInt("caloriesburnt")));
                            break;
                        case "HeartRateMeasurement":
                            measurements.add(new HeartRateMeasurement(rs.getString(GET_TYPE_COLUMN),rs.getString(GET_DATETIME_COLUMN), rs.getFloat(GET_VALUE_COLUMN)));
                            break;
                        case "HormoneMeasurement":
                            measurements.add(new HormoneMeasurement(rs.getString(GET_TYPE_COLUMN),rs.getString(GET_DATETIME_COLUMN), rs.getFloat(GET_VALUE_COLUMN)));
                            break;
                        case "StressMeasurement":
                            measurements.add(new StressMeasurement(rs.getString(GET_TYPE_COLUMN),rs.getString(GET_DATETIME_COLUMN), rs.getFloat(GET_VALUE_COLUMN)));
                            break;
                        default:
                            measurements.add(new Measurement(rs.getString(GET_TYPE_COLUMN)  , rs.getString(GET_DATETIME_COLUMN), rs.getFloat(GET_VALUE_COLUMN)));
                    }
                } return measurements;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get measurement.", ex);
            throw new RepositoryException("Could not get measurement.");
        }
    }

    public void removeFriend(int userID, int friendID) {
        if (!checkIfFriendExists(userID, friendID)) {
            throw new NoSuchElementException("You are not friends with this person!");
        }
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_REMOVE_FRIEND)
        ) {
            stmt.setInt(1, userID);
            stmt.setInt(2, friendID);
            stmt.execute();
            stmt.setInt(2, userID);
            stmt.setInt(1, friendID);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to remove friend.", ex);
            throw new RepositoryException("Could not remove friend.");
        }
    }

    public void createMeasurement(int userID, Measurement measurement) {
        if (Objects.equals(measurement.getType(), "CalorieMeasurement")){
            createCalorieMeasurement(userID, measurement);
        } else if (Objects.equals(measurement.getType(), "BloodMeasurement")){
            createBloodMeasurement(userID, measurement);
        } else {
            createSimpleMeasurement(userID, measurement);
        }
        addHealthScore(userID, (int) MedicalCalculator.calculateHealthScore(getUserMedical(userID).getMeasurements()));
    }

    public void createSimpleMeasurement(int userID, Measurement measurement) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CREATE_SIMPLE_MEASUREMENT)
        ) {
            stmt.setInt(1, userID);
            stmt.setString(2, measurement.getType());
            stmt.setString(3, measurement.getDatetime());
            stmt.setFloat(4, measurement.getValue());
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, FAILED_TO_CREATE_MEASUREMENT, ex);
            throw new RepositoryException(FAILED_TO_CREATE_MEASUREMENT);
        }
    }

    public void createCalorieMeasurement(int userID, Measurement measurement) {
        CalorieMeasurement calorieMeasurement = (CalorieMeasurement) measurement;
        int footsteps = calorieMeasurement.getFootsteps();
        int caloriesBurnt = calorieMeasurement.getCaloriesBurnt();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CREATE_CALORIE_MEASUREMENT)
        ) {
            stmt.setInt(1, userID);
            stmt.setString(2, measurement.getType());
            stmt.setString(3, measurement.getDatetime());
            stmt.setFloat(4, measurement.getValue());
            stmt.setInt(5, footsteps);
            stmt.setInt(6, caloriesBurnt);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, FAILED_TO_CREATE_MEASUREMENT, ex);
            throw new RepositoryException(FAILED_TO_CREATE_MEASUREMENT);
        }
    }

    public void createBloodMeasurement(int userID, Measurement measurement) {
        BloodMeasurement bloodMeasurement = (BloodMeasurement) measurement;
        float bloodSugarLevel = bloodMeasurement.getBloodSugarLevel();
        float bloodPressure = bloodMeasurement.getBloodPressure();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CREATE_BLOOD_MEASUREMENT)
        ) {
            stmt.setInt(1, userID);
            stmt.setString(2, measurement.getType());
            stmt.setString(3, measurement.getDatetime());
            stmt.setFloat(4, measurement.getValue());
            stmt.setFloat(5, bloodSugarLevel);
            stmt.setFloat(6, bloodPressure);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, FAILED_TO_CREATE_MEASUREMENT, ex);
            throw new RepositoryException(FAILED_TO_CREATE_MEASUREMENT);
        }
    }

    public void sendAlertToUser(int userID, Alert alert) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SEND_ALERT)
        ) {
            stmt.setInt(1, userID);
            stmt.setString(2, alert.getName());
            stmt.setString(3, String.valueOf(alert.getType()));
            stmt.setInt(4, alert.getUrgency());
            stmt.setString(5, alert.getDescription());
            stmt.setString(6, alert.getLocation());

            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, FAILED_TO_SEND_ALERT, ex);
            throw new RepositoryException(FAILED_TO_SEND_ALERT);
        }
    }

    public void createAppointment(int userID, Appointment appointment) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CREATE_APPOINTMENT)
        ) {
            stmt.setInt(1, userID);
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getDate());
            stmt.setString(4, appointment.getLocation());

            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, FAILED_TO_CREATE_APPOINTMENT, ex);
            throw new RepositoryException(FAILED_TO_CREATE_APPOINTMENT);
        }
    }

    public void sendMessage(int userID, int friendID, String msg) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SEND_MESSAGE)
        ) {
            stmt.setInt(1, friendID);
            stmt.setInt(2, userID);
            stmt.setString(3, msg);
            stmt.setString(4, String.valueOf(LocalDateTime.now()));
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to send message.", ex);
            throw new RepositoryException("Could not send message.");
        }
    }

    public List<Message> getUserMessages(int userID, int friendID) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER_MESSAGES)
        ) {
            stmt.setInt(1, userID);
            stmt.setInt(2, friendID);
            stmt.setInt(3, friendID);
            stmt.setInt(4, userID);
            List<Message> messages = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(rs.getInt("receiverID"),rs.getInt("senderID"), rs.getString("message"), rs.getString("timestamp")));
                }
                return messages;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get user messages.", ex);
            throw new RepositoryException("Could not get user messages.");
        }
    }

    void addHealthScore(int userID, int i){
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_HEALTHSCORE)
        ) {
            stmt.setInt(1, userID);
            stmt.setInt(2, i);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to add health score.", ex);
            throw new RepositoryException("Could not add health score.");
        }
    }

    List<Integer> getHealthScores(int userID){
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER_HEALTHSCORES)
        ) {
            stmt.setInt(1, userID);
            List<Integer> healthScores = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    healthScores.add(rs.getInt("healthscore"));
                }
                return healthScores;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get user health scores.", ex);
            throw new RepositoryException("Could not get user health scores.");
        }
    }

    public List<MedicalData> getAllMedical() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_ALL_MEDICAL)
        ) {
            List<MedicalData> medicalData = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userID = rs.getInt("userid");
                    medicalData.add(new MedicalData(userID, rs.getString("chronicdiseases"), rs.getString("geneticdiseases"), rs.getFloat("height"), rs.getFloat("weight"), BloodType.valueOf(rs.getString("bloodtype")), rs.getBoolean("pregnant"), rs.getString("gender"), getHealthScores(userID), getUserMeasurements(userID), rs.getString("allergies"), rs.getInt("age"), rs.getString("birthdate")));
                }
                return medicalData;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get all medical data.", ex);
            throw new RepositoryException("Could not get all medical data.");
        }
    }
}

