package com.repairagency.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class that is used to hash the password with SHA-256 algorithm
 */
public class PasswordHash {

    private static final Logger logger = LogManager.getLogger();
    private static final String ALGORITHM = "SHA-256";

    private PasswordHash() {
    }

    /**
     * Receives hexadecimal hash string of given input.
     * This method is intended to hashing user passwords.
     * It uses defined in {@link #ALGORITHM} hash function.
     * @param password user's password to be hashed
     * @return hexadecimal hash string of the given input
     */
    public static String getHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Unable to generate password hash");
            throw new IllegalStateException(ex);
        }
    }

}
