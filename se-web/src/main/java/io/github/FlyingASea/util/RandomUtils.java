package io.github.FlyingASea.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RandomUtils {
    private static final String SECRET = "fZ_f6'nz]0PI/LJ3vfYxWTR/PW3~.]v/fuz;I=Ua/andLtfQn3,<`zW:U~{t-OqL";
    private static final SecureRandom random = new SecureRandom();

    private static final JWTVerifier VERIFIER = JWT.require(Algorithm.HMAC256(SECRET)).build();

    private static final char[] RANDOM_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    public static String randomString(int length, String id) {
        StringBuilder builder = new StringBuilder();
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        for (int i = 0; i < length; i++)
            builder.append(RANDOM_CHARS[random.nextInt(RANDOM_CHARS.length)]);
        builder.toString();
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                .withHeader(map)
                .withClaim("id", id)
                .withClaim("randomPayload", builder.toString())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static String randomString(String id) {
        return randomString(12, id);
    }

    public static int randomInt() {
        return random.nextInt();
    }

    public static Map<String, Claim> verifySession(String session) {
        try {
            return VERIFIER.verify(session).getClaims();
        } catch (Exception e) {
            return null;
        }
    }
}
