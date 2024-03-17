package be.howest.ti.mars.web;

import be.howest.ti.mars.logic.controller.MockMarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.web.bridge.MarsOpenApiBridge;
import be.howest.ti.mars.web.bridge.MarsRtcBridge;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert", "PMD.AvoidDuplicateLiterals"})
/*
 * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
 * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenAPITest {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
    public static final String MSG_201_EXPECTED = "If a resource is successfully created.";
    public static final String MSG_204_EXPECTED = "If a resource is successfully deleted";
    private Vertx vertx;
    private WebClient webClient;

    @BeforeAll
    void deploy(final VertxTestContext testContext) {
        Repositories.shutdown();
        vertx = Vertx.vertx();

        WebServer webServer = new WebServer(new MarsOpenApiBridge(new MockMarsController()), new MarsRtcBridge());
        vertx.deployVerticle(
                webServer,
                testContext.succeedingThenComplete()
        );
        webClient = WebClient.create(vertx);
    }

    @AfterAll
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.succeedingThenComplete());
        webClient.close();
        Repositories.shutdown();
    }

    @Test
    void getUsers(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("users")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getAllMedical(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/Medical").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("medicalData")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getUserAppointments(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1/appointments").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("appointments")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getUserAlerts(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1/alerts").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("alerts")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void addFriend(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/users/1/friends/2").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("msg")),
                            "Added friend"
                    );
                    testContext.completeNow();
                }));
    }
    @Test
    void removeFriend(final VertxTestContext testContext) {
        webClient.delete(PORT, HOST, "/api/users/1/friends/2").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("msg")),
                            "removed friend"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getRecommendations(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1/recommendations").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("recommendations")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getSpecificUser (final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/user?userID=1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("firstname")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getUserMedical (final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1/Medical").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("height")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void createMeasurement (final VertxTestContext testContext) {
        JsonObject json = new JsonObject();
        json.put("type", "BloodMeasurement");
        json.put("datetime", "00-00-00 00:00:00");
        json.put("value", 60);
        json.put("bloodsugarlevel", 99.0);
        json.put("bloodpressure", 120.0);
        webClient.post(PORT, HOST, "/api/users/1/measure").sendJsonObject(json)
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("msg")),
                            "Created a new measurement!"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void createAppointment (final VertxTestContext testContext) {
        JsonObject json = new JsonObject();
        json.put("description", "Time for appointments!");
        json.put("datetime", "00-00-00 00:00:00");
        json.put("location", "Mars");
        webClient.post(PORT, HOST, "/api/users/1/appointments").sendJsonObject(json)
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("msg")),
                            "Created a new appointment!"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void sendMessage (final VertxTestContext testContext) {
        JsonObject json = new JsonObject();
        json.put("message", "Mars is lit dawg...");
        webClient.post(PORT, HOST, "/api/users/1/messages/2").sendJsonObject(json)
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("msg")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getUserMessages (final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1/messages/2").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("messages")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void sendAlertToUser (final VertxTestContext testContext) {
        JsonObject json = new JsonObject();
        json.put("name", "Alice");
        json.put("alerttype", "EMERGENCY");
        json.put("urgency", 5);
        json.put("description", "Birthday party at alice's place!");
        json.put("location", "alice's place");
        webClient.post(PORT, HOST, "/api/users/1/alerts").sendJsonObject(json)
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("msg")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void subscribe (final VertxTestContext testContext) {
        JsonObject json = new JsonObject();
        JsonObject keys = new JsonObject();
        keys.put("auth", "auth");
        keys.put("p256dh", "p256dh");
        json.put("keys", keys);
        json.put("endpoint", "the end");
        webClient.post(PORT, HOST, "/api/users/1/subscribe").sendJsonObject(json)
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("msg")),
                            ""
                    );
                    testContext.completeNow();
                }));
    }
}