package be.howest.ti.mars.web.bridge;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 *
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 *
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 *
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public class Request {

    public static final String SPEC_USER_ID = "userID";
    public static final String SPEC_FRIEND_ID = "friendID";
    public static final String SPEC_TYPE = "type";
    public static final String SPEC_DATETIME = "datetime";
    public static final String SPEC_VALUE = "value";
    public static final String SPEC_FOOTSTEPS = "footsteps";
    public static final String SPEC_CALORIESBURNT = "caloriesburnt";
    public static final String SPEC_BLOODSUGARLEVEL = "bloodsugarlevel";
    public static final String SPEC_BLOODPRESSURE = "bloodpressure";
    private static final String SPEC_URGENCY = "urgency";
    private static final String SPEC_DESCRIPTION = "description";
    private static final String SPEC_LOCATION = "location";
    private static final String SPEC_NAME = "name";
    private static final String SPEC_ALERT_TYPE = "alerttype";
    public static final String SPEC_MESSAGE = "message";
    private final RequestParameters params;

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }

    public int getUserID() {
        return params.pathParameter(SPEC_USER_ID).getInteger();
    }

    public int getFriendID() {
        return params.pathParameter(SPEC_FRIEND_ID).getInteger();
    }

    public @Nullable RequestParameter getQueryParameter(String parameter) {
        return params.queryParameter(parameter);
    }

    public String getMeasurementType() {
        return params.body().getJsonObject().getString(SPEC_TYPE);
    }

    public String getDateTime() {
        return params.body().getJsonObject().getString(SPEC_DATETIME);
    }

    public float getValue() {
        return params.body().getJsonObject().getFloat(SPEC_VALUE);
    }

    public @Nullable int getFootsteps() {
        return params.body().getJsonObject().getInteger(SPEC_FOOTSTEPS);
    }

    public @Nullable int getCaloriesBurnt() {
        return params.body().getJsonObject().getInteger(SPEC_CALORIESBURNT);
    }

    public @Nullable float getBloodSugarLevel() {
        return params.body().getJsonObject().getFloat(SPEC_BLOODSUGARLEVEL);
    }

    public @Nullable float getBloodPressure() {
        return params.body().getJsonObject().getFloat(SPEC_BLOODPRESSURE);
    }

    public int getUrgency() {
        return params.body().getJsonObject().getInteger(SPEC_URGENCY);
    }

    public String getDescription() {
        return params.body().getJsonObject().getString(SPEC_DESCRIPTION);
    }

    public String getLocation() {
        return params.body().getJsonObject().getString(SPEC_LOCATION);
    }

    public String getName() {
        return params.body().getJsonObject().getString(SPEC_NAME);
    }

    public String getAlertType() {
        return params.body().getJsonObject().getString(SPEC_ALERT_TYPE);
    }

    public String getMessage() {
        return params.body().getJsonObject().getString(SPEC_MESSAGE);
    }

    public JsonObject getKeys(){
        return params.body().getJsonObject().getJsonObject("keys");
    }
    public String getAuth(){
        return params.body().getJsonObject().getJsonObject("keys").getString("auth");
    }
    public String getEndpoint(){
        return params.body().getJsonObject().getString("endpoint");
    }
}
