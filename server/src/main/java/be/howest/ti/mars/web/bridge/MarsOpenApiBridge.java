package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.data.MedicalData;
import be.howest.ti.mars.logic.domain.data.measurements.BloodMeasurement;
import be.howest.ti.mars.logic.domain.data.measurements.CalorieMeasurement;
import be.howest.ti.mars.logic.domain.data.measurements.Measurement;
import be.howest.ti.mars.logic.service.NotificationService;
import be.howest.ti.mars.logic.service.Subscription;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the MarsOpenApiBridge class you will create one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class.
 * The Request class will turn the HTTP request data into the desired Java types (int, String, Custom class,...)
 * This desired type is then passed to the controller.
 * The return value of the controller is turned to Json or another Web data type in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    public static final int NOT_YET_ASSIGNED = -1;
    public static final String FAILED_TO_SEND_NOTIFICATION = "Failed to send notification: %s";
    private final MarsController controller;
    private final NotificationService service;

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getUsers");
        routerBuilder.operation("getUsers").handler(this::getUsers);

        LOGGER.log(Level.INFO, "Installing handler for: getSpecificUser");
        routerBuilder.operation("getSpecificUser").handler(this::getSpecificUser);

        LOGGER.log(Level.INFO, "Installing handler for: getUserAppointments");
        routerBuilder.operation("getUserAppointments").handler(this::getUserAppointments);

        LOGGER.log(Level.INFO, "Installing handler for: createAppointment");
        routerBuilder.operation("createAppointment").handler(this::createAppointment);

        LOGGER.log(Level.INFO, "Installing handler for: getUserAlerts");
        routerBuilder.operation("getUserAlerts").handler(this::getUserAlerts);

        LOGGER.log(Level.INFO, "Installing handler for: sendAlertToUser");
        routerBuilder.operation("sendAlertToUser").handler(this::sendAlertToUser);

        LOGGER.log(Level.INFO, "Installing handler for: addFriend");
        routerBuilder.operation("addFriend").handler(this::addFriend);

        LOGGER.log(Level.INFO, "Installing handler for: getUserFriendByID");
        routerBuilder.operation("getUserFriendByID").handler(this::getUserFriendByID);

        LOGGER.log(Level.INFO, "Installing handler for: getUserFriends");
        routerBuilder.operation("getUserFriends").handler(this::getUserFriends);

        LOGGER.log(Level.INFO, "Installing handler for: getRecommendations");
        routerBuilder.operation("getUserRecommendations").handler(this::getUserRecommendations);

        LOGGER.log(Level.INFO, "Installing handler for: removeFriend");
        routerBuilder.operation("removeFriend").handler(this::removeFriend);

        LOGGER.log(Level.INFO, "Installing handler for: getUserMedical");
        routerBuilder.operation("getUserMedical").handler(this::getUserMedical);

        LOGGER.log(Level.INFO, "Installing handler for: createMeasurement");
        routerBuilder.operation("createMeasurement").handler(this::createMeasurement);

        LOGGER.log(Level.INFO, "Installing handler for: getUserMessages");
        routerBuilder.operation("getUserMessages").handler(this::getUserMessages);

        LOGGER.log(Level.INFO, "Installing handler for: sendMessage");
        routerBuilder.operation("sendMessage").handler(this::sendMessage);

        LOGGER.log(Level.INFO, "Installing handler for: subscribe");
        routerBuilder.operation("subscribe").handler(this::subscribe);

        LOGGER.log(Level.INFO, "Installing handler for: getAllMedical");
        routerBuilder.operation("getAllMedical").handler(this::getAllMedical);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }

    private void subscribe(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        JsonObject keys = Request.from(ctx).getKeys();
        String endpoint = Request.from(ctx).getEndpoint();
        String auth = String.valueOf(keys.getValue("auth"));
        String key = String.valueOf(keys.getValue("p256dh"));

        service.subscribe(userID, new Subscription(auth, key, endpoint));
        Response.sendConfirmResponse(ctx, "Subscribed!");
    }

    private void sendAlertToUser(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        int urgency = Request.from(ctx).getUrgency();
        String alertType = Request.from(ctx).getAlertType();
        String name = Request.from(ctx).getName();
        String description = Request.from(ctx).getDescription();
        String location = Request.from(ctx).getLocation();
        Alert alert = new Alert(NOT_YET_ASSIGNED, Alert.AlertType.valueOf(alertType), name, description, urgency, location);
        controller.sendAlertToUser(userID, alert);
        Response.sendConfirmResponse(ctx, "Sent alert!");
        try {
            service.sendAllPushNotifications(String.format("%s: %s", alertType.replace("_", " "), description));
        } catch (JoseException | GeneralSecurityException | IOException | ExecutionException | InterruptedException e) {
            LOGGER.log(Level.WARNING, String.format(FAILED_TO_SEND_NOTIFICATION, e));
            Thread.currentThread().interrupt();
        }
    }

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
        this.service = new NotificationService();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
        service = new NotificationService();
    }

    private void getUsers(RoutingContext ctx) {
        List<User> users = controller.getUsers();

        Response.sendUsers(ctx, users);
    }

    private void getAllMedical(RoutingContext routingContext) {
        List<MedicalData> data = controller.getAllMedical();

        Response.sendAllMedicalData(routingContext, data);
    }

    private void getUserAppointments(RoutingContext ctx) {
        List<Appointment> appointments = controller.getUserAppointments(Request.from(ctx).getUserID());
        Response.sendAppointments(ctx, appointments);
    }

    private void createAppointment(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        String description = Request.from(ctx).getDescription();
        String dateTime = Request.from(ctx).getDateTime();
        String location = Request.from(ctx).getLocation();

        controller.createAppointment(userID, new Appointment(-1, description, dateTime, location));

        Response.sendConfirmResponse(ctx, "Created appointment!");
    }

    private void getUserAlerts(RoutingContext ctx) {
        List<Alert> alerts = controller.getUserAlerts(Request.from(ctx).getUserID());
        Response.sendAlerts(ctx, alerts);
    }

    private void addFriend(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        int friendID = Request.from(ctx).getFriendID();
        controller.addFriend(userID, friendID);
        Response.sendConfirmResponse(ctx, "Added friend");
        User user = controller.getUser(userID);
        User friend = controller.getUser(friendID);
        controller.sendAlertToUser(userID, new Alert(NOT_YET_ASSIGNED, Alert.AlertType.NEW_FRIEND, String.format("%s %s", friend.getFirstname(), friend.getLastname()), "You have a new friend!", 1, ""));
        controller.sendAlertToUser(friendID, new Alert(NOT_YET_ASSIGNED, Alert.AlertType.NEW_FRIEND, String.format("%s %s", user.getFirstname(), user.getLastname()), "You have a new friend!", 1, ""));
        try {
            if (service.containsSubscription(userID)) {
                service.sendPushNotificationByUserID(userID, String.format("You are now friends with: %s %s", friend.getFirstname(), friend.getLastname()));
            }
            if (service.containsSubscription(friendID)) {
                service.sendPushNotificationByUserID(friendID, String.format("You are now friends with: %s %s", user.getFirstname(), user.getLastname()));
            }
        } catch (JoseException | GeneralSecurityException | IOException | ExecutionException | InterruptedException e) {
            LOGGER.log(Level.WARNING, String.format(FAILED_TO_SEND_NOTIFICATION, e));
            Thread.currentThread().interrupt();
        }
    }

    private void removeFriend(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        int friendID = Request.from(ctx).getFriendID();
        controller.removeFriend(userID, friendID);
        Response.sendConfirmResponse(ctx, "Removed friend");
    }

    private void getUserFriends(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        List<User> friends = controller.getUserFriends(userID);
        Response.sendUsers(ctx, friends);
    }

    private void getUserFriendByID(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        int friendID = Request.from(ctx).getFriendID();
        User user = controller.getUserFriendByID(userID, friendID);
        Response.sendUser(ctx, user);
    }

    private void getSpecificUser(RoutingContext ctx) {
        if (Request.from(ctx).getQueryParameter("name") != null) {
            String username = Request.from(ctx).getQueryParameter("name").getString();
            User user = controller.getUser(username);
            Response.sendUser(ctx, user);
        } else if (Request.from(ctx).getQueryParameter("userID") != null) {
            int userID = Request.from(ctx).getQueryParameter("userID").getInteger();
            User user = controller.getUser(userID);
            Response.sendUser(ctx, user);
        }

    }

    private void getUserRecommendations(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        List<Recommendation> recommendations = controller.getRecommendations(userID);

        Response.sendRecommendations(ctx, recommendations);
    }

    private void getUserMedical(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        MedicalData medData = controller.getUserMedical(userID);
        Response.sendMedicalData(ctx, medData);
    }

    private void createMeasurement(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        String type = Request.from(ctx).getMeasurementType();
        String datetime = Request.from(ctx).getDateTime();
        float value = Request.from(ctx).getValue();
        int footsteps;
        int caloriesBurnt;
        float bloodSugarLevel;
        float bloodPressure;
        Measurement measurement;

        if (Objects.equals(type, "CalorieMeasurement")) {
            footsteps = Request.from(ctx).getFootsteps();
            caloriesBurnt = Request.from(ctx).getCaloriesBurnt();
            measurement = new CalorieMeasurement(type, datetime, value, footsteps, caloriesBurnt);
        } else if (Objects.equals(type, "BloodMeasurement")) {
            bloodSugarLevel = Request.from(ctx).getBloodSugarLevel();
            bloodPressure = Request.from(ctx).getBloodPressure();
            measurement = new BloodMeasurement(type, datetime, value, bloodSugarLevel, bloodPressure);
        } else {
            measurement = new Measurement(type, datetime, value);
        }
        controller.createMeasurement(userID, measurement);
        Response.sendConfirmResponse(ctx, "Created a new measurement!");
    }

    private void sendMessage(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        int friendID = Request.from(ctx).getFriendID();

        String msg = Request.from(ctx).getMessage();
        controller.sendMessage(userID, friendID, msg);
        Response.sendConfirmResponse(ctx, msg);
        try {
            if (service.containsSubscription(friendID)) {
                User user = controller.getUser(userID);
                service.sendPushNotificationByUserID(friendID, String.format("You have a new message from: %s %s", user.getFirstname(), user.getLastname()));
            }
        } catch (JoseException | GeneralSecurityException | IOException | ExecutionException | InterruptedException e) {
            LOGGER.log(Level.WARNING, String.format(FAILED_TO_SEND_NOTIFICATION, e));
            Thread.currentThread().interrupt();
        }
    }

    private void getUserMessages(RoutingContext ctx) {
        int userID = Request.from(ctx).getUserID();
        int friendID = Request.from(ctx).getFriendID();

        List<Message> messages = controller.getUserMessages(userID, friendID);
        Response.sendMessages(ctx, messages);
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof MalformedRequestException) {
            code = 400;
        } else if (cause instanceof NoSuchElementException) {
            code = 404;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
