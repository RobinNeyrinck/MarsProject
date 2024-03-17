package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.data.MedicalData;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() { }

    public static void sendUsers(RoutingContext ctx, List<User> users){
        JsonObject json = new JsonObject();
        json.put("users", users);
        sendOkJsonResponse(ctx, json);
    }

    public static void sendUser(RoutingContext ctx, User user){
        sendOkJsonResponse(ctx, JsonObject.mapFrom(user));
    }

    private static void sendOkJsonResponse(RoutingContext ctx, JsonObject response) {
        sendJsonResponse(ctx, 200, response);
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }

    public static void sendAppointments(RoutingContext ctx, List<Appointment> appointments) {
        JsonObject json = new JsonObject();
        json.put("appointments", appointments);
        sendOkJsonResponse(ctx, json);
    }

    public static void sendAlerts(RoutingContext ctx, List<Alert> alerts) {
        JsonObject json = new JsonObject();
        json.put("alerts", alerts);
        sendOkJsonResponse(ctx, json);
    }

    public static void sendConfirmResponse(RoutingContext ctx, String msg) {
        JsonObject json = new JsonObject();
        json.put("msg", msg);
        sendOkJsonResponse(ctx, JsonObject.mapFrom(json));
    }

    public static void sendRecommendations(RoutingContext ctx, List<Recommendation> recommendations) {
        JsonObject json = new JsonObject();
        json.put("recommendations", recommendations);
        sendJsonResponse(ctx, 200, json);
    }

    public static void sendMedicalData(RoutingContext ctx, MedicalData medData) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(medData));
    }

    public static void sendMessages(RoutingContext ctx, List<Message> messages) {
        JsonObject json = new JsonObject();
        json.put("messages", messages);
        sendOkJsonResponse(ctx, json);
    }

    public static void sendAllMedicalData(RoutingContext ctx, List<MedicalData> data) {
        JsonObject json = new JsonObject();
        json.put("medicalData", data);
        sendOkJsonResponse(ctx, json);
    }
}
