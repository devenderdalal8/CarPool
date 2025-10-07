package com.carpool.carpool.util;

import java.security.SecureRandom;

import static com.carpool.carpool.util.Constants.CHAR_POOL;
import static com.carpool.carpool.util.Constants.SESSION_ID_LENGTH;

public class Ext {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateToken() {
        StringBuilder sb = new StringBuilder(SESSION_ID_LENGTH);
        for (int i = 0; i < SESSION_ID_LENGTH; i++) {
            sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }
}
