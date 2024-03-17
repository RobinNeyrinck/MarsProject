package be.howest.ti.mars.logic.service;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

public class Subscription {

    private final String auth;
    private final String key;
    private final String endpoint;

    public Subscription(String auth, String key, String endpoint) {
        this.auth = auth;
        this.key = key;
        this.endpoint = endpoint;
    }

    public String getAuth() {
        return auth;
    }

    public byte[] getAuthAsBytes() {
        return Base64.getUrlDecoder().decode(getAuth());
    }

    public String getKey() {
        return key;
    }

    public byte[] getKeyAsBytes() {
        return Base64.getUrlDecoder().decode(getKey());
    }

    public PublicKey getUserPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        ECPoint point = ecSpec.getCurve().decodePoint(getKeyAsBytes());
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);

        return kf.generatePublic(pubSpec);
    }

    public String getEndpoint() {
        return endpoint;
    }
}
