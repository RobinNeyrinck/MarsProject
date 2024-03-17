package be.howest.ti.mars.logic.service;

import be.howest.ti.mars.logic.exceptions.ServiceException;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationService {
    private static final int TTL = 255;
    private static final String PUBLIC_KEY = "BLydROMDPetgxalNDbYQr3Jo3ufdgJ0jYIBzV5korpmfgYXGyFj9mCZBhaTcKZIv2DlWgSCn0JIpwVN0fb7isQA";
    private static final String PRIVATE_KEY = "skFmb2BwRayi3-uuVfvhtkZKTUdAYgDdC12H3lV_sMA";

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    private final PushService pushService;

    Map<Integer, Subscription> subscriptions = new HashMap<>();

    public NotificationService(){
        pushService = new PushService();
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            pushService.setSubject("mailto:arman.gasparyan@student.howest.be");
            pushService.setPublicKey(PUBLIC_KEY);
            pushService.setPrivateKey(PRIVATE_KEY);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            LOGGER.log(Level.SEVERE, "Failed to set up service.", e);
            throw new ServiceException("Failed to set up service.");
        }
    }
    public void subscribe(int userID, Subscription subscription){
        subscriptions.put(userID, subscription);
        }

    public void sendAllPushNotifications(String message) throws JoseException, GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        for (Subscription s: subscriptions.values()) {
            sendPushMessage(s, message.getBytes());
        }
    }

    public void sendPushNotificationByUserID(int userID, String message) throws JoseException, GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        sendPushMessage(subscriptions.get(userID), message.getBytes());
    }

    public void sendPushMessage(Subscription sub, byte[] payload) throws JoseException, GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        Notification notification;
        notification = new Notification(
                    sub.getEndpoint(),
                    sub.getUserPublicKey(),
                    sub.getAuthAsBytes(),
                    payload,
                    TTL
            );
        pushService.send(notification);
    }

    public Boolean containsSubscription(int id){
        return subscriptions.containsKey(id);
    }
}
