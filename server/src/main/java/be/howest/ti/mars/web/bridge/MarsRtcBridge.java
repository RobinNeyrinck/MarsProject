package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.chat.Chatroom;
import be.howest.ti.mars.logic.chat.events.*;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The RTC bridge is one of the class taught topics.
 * If you do not choose the RTC topic you don't have to do anything with this class.
 * Otherwise, you will need to expand this bridge with the websockets topics shown in the other modules.
 *
 * The client-side starter project does not contain any teacher code about the RTC topic.
 * The rtc bridge is already initialized and configured in the WebServer.java.
 * No need to change the WebServer.java
 *
 * The job of the "bridge" is to bridge between websockets events and Java (the controller).
 * Just like in the openapi bridge, keep business logic isolated in the package logic.
 * <p>
 */
public class MarsRtcBridge {
    private static final String EB_EVENT_TO_MARTIANS = "events.to.martians";
    private SockJSHandler sockJSHandler;
    private EventBus eb;

    private static final String CHNL_TO_SERVER = "events.to.server";
    private static final String CHNL_TO_CLIENTS = "events.to.clients";
    private static final String CHNL_TO_CLIENT_UNICAST = "events.to.client.";

    private static final Chatroom CHATROOM = new Chatroom();

    /**
     * Example function to put a message on the event bus every 10 seconds.
     * The timer logic is only there to simulate a repetitive stream of data as an example.
     * Please remove this timer logic or move it to an appropriate place.
     * Please call the controller to get some business logic data.
     * Afterwords publish the result to the client.
     */
    public void sendEventToClients() {
        final Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                eb.publish(EB_EVENT_TO_MARTIANS, new JsonObject(Map.of("MyJsonProp", "some value")));
            }
        };

        timer.schedule(task, 0, 30000);
    }

    private void createSockJSHandler() {
        final PermittedOptions permittedOptions = new PermittedOptions().setAddressRegex("events\\..+");
        final SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addInboundPermitted(permittedOptions)
                .addOutboundPermitted(permittedOptions);
        sockJSHandler.bridge(options);
    }

    public SockJSHandler getSockJSHandler(Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
        eb = vertx.eventBus();
        createSockJSHandler();

        registerConsumers(vertx);

        // This is for demo purposes only.
        // Do not send messages in this getSockJSHandler function.
        sendEventToClients();

        return sockJSHandler;
    }


    private void registerConsumers(Vertx vertx) {
        vertx.eventBus().consumer(CHNL_TO_SERVER, this::handleIncomingMessage);
    }

    private void handleIncomingMessage(Message<JsonObject> msg) {
        IncomingEvent incoming = EventFactory.getInstance().createIncomingEvent(msg.body());
        OutgoingEvent result = CHATROOM.handleEvent(incoming);
        handleOutgoingMessage(result);
    }

    private void handleOutgoingMessage(OutgoingEvent result) {
        switch (result.getType()) {
            case BROADCAST:
                broadcastMessage((BroadcastEvent) result);
                break;
            case UNICAST:
                unicastMessage((UnicastEvent) result);
                break;
            case MULTICAST:
                multicastMessage((MulticastEvent) result);
                break;
            default:
                throw new IllegalArgumentException("Type of outgoing message not supported");
        }
    }

    private void broadcastMessage(BroadcastEvent e) {
        eb.publish(CHNL_TO_CLIENTS, e.getMessage());
    }

    private void unicastMessage(UnicastEvent e) {
        eb.publish(CHNL_TO_CLIENT_UNICAST + e.getRecipient(), e.getMessage());
    }

    private void multicastMessage(MulticastEvent e) {
        if (!e.getRecipient().equals("")){
            eb.publish(CHNL_TO_CLIENT_UNICAST + e.getRecipient(), e.getMessage());
        }
        eb.publish(CHNL_TO_CLIENT_UNICAST + e.getSender(), e.getMessage());
    }
}
