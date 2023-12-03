package io.github.FlyingASea.util;

import org.slf4j.Logger;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SHA256withRSAUtil {

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 1024;
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private static Map<String, String> createRSAKeys() {
        Map<String, String> keyPairMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String publicKeyValue = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyValue = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            keyPairMap.put(PUBLIC_KEY, publicKeyValue);
            keyPairMap.put(PRIVATE_KEY, privateKeyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPairMap;
    }

    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {

        }
        return null;
    }

    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {

        }
        return null;
    }

    public static String sign(String key, String requestData) {
        String signature = null;
        byte[] signed;
        try {
            PrivateKey privateKey = getPrivateKey(key);

            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();

            signature = Base64.getEncoder().encodeToString(signed);

        } catch (Exception e) {

        }
        return signature;
    }

    public static boolean verifySign(String key, String data, String signature) {
        boolean verifySignSuccess = false;
        try {
            PublicKey publicKey = getPublicKey(key);

            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(data.getBytes());

            verifySignSuccess = verifySign.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {

        }
        return verifySignSuccess;
    }


}
