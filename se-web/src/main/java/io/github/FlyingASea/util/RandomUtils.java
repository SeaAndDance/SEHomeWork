package io.github.FlyingASea.util;

import java.security.SecureRandom;

public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();
    private static final char[] RANDOM_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String randomString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append(RANDOM_CHARS[random.nextInt(RANDOM_CHARS.length)]);
        return builder.toString();
    }

    public static String randomString() {
        return randomString(12);
    }

    public static int randomInt() {
        return random.nextInt();
    }
}
