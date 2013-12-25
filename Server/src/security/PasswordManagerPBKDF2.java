/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 * @author root
 */
public class PasswordManagerPBKDF2 implements PasswordManagerIF {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static final int SALT_BYTE_MIN = 64;
    public static final int SALT_BYTE_MAX = 128;

    public static final int HASH_BYTE_MIN = 64;
    public static final int HASH_BYTE_MAX = 128;

    public static final int ITERATIONS_MIN = 10000;
    public static final int ITERATIONS_MAX = 30000;

    private static PasswordManagerPBKDF2 instance;

    private PasswordManagerPBKDF2() {
    }

    /* You can obtain instance outside the package only 
    * via the SecurityUtils class
    */
    protected static PasswordManagerPBKDF2 getInstance() {
        if (instance == null) {
            instance = new PasswordManagerPBKDF2();
        }
        return instance;
    }

    @Override
    public Credentials createCredentials(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        SecureRandom random = new SecureRandom();
        int saltSize = SALT_BYTE_MIN + random.nextInt(SALT_BYTE_MAX - SALT_BYTE_MIN);
        byte[] salt = new byte[saltSize];
        random.nextBytes(salt);
        int iterations = ITERATIONS_MIN + random.nextInt(ITERATIONS_MAX - ITERATIONS_MIN);
        int hashSize = HASH_BYTE_MIN + random.nextInt(HASH_BYTE_MAX - HASH_BYTE_MIN);
        byte[] hash = pbkdf2(password.toCharArray(), salt, iterations, hashSize);

        return new Credentials(hash, salt, iterations);
    }

    @Override
    public boolean validatePassword(String password, Credentials creds)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        int iterations = creds.getIterations();
        byte[] salt = creds.getSalt();
        byte[] realHashedPassword = creds.getHashedPassword();

        byte[] testHashedPassword = pbkdf2(password.toCharArray(), salt, iterations, realHashedPassword.length);

        return slowEquals(realHashedPassword, testHashedPassword);
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

}
