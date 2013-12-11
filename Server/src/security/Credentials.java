/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author root
 */
public class Credentials {

    
    private byte[] hashedPassword;              //in clear byte form
    private String encodedHashedPassword;       //converted to String, for DB

    private byte[] salt;                        //in clear byte form
    private String encodedSalt;                 //converted to String, for DB

    private int iterations;                     //number of iterations

    /**
     * Create credentials for an old entry.
     *
     * @param encodedHashedPassword the password from DB
     */
    public Credentials(String encodedHashedPassword, String encodedSalt, int iterations) throws IOException {

        this.iterations = iterations;

        this.hashedPassword = decodeString64(encodedHashedPassword);
        this.salt = decodeString64(encodedSalt);

        this.encodedHashedPassword = encodedHashedPassword;
        this.encodedSalt = encodedSalt;
    }

    public Credentials(byte[] hashedPassword, byte[] salt, int iterations) throws IOException {

        this.iterations = iterations;

        this.hashedPassword = hashedPassword;
        this.salt = salt;

        this.encodedHashedPassword = encodeBytes64(hashedPassword);
        this.encodedSalt = encodeBytes64(salt);
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getEncodedSalt() {
        return encodedSalt;
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public String getEncodedHashedPassword() {
        return encodedHashedPassword;
    }

    public int getIterations() {
        return iterations;
    }
    
    private byte[] decodeString64(String input) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(input);
    }

    private String encodeBytes64(byte[] input) throws IOException {
        BASE64Encoder endecoder = new BASE64Encoder();
        return endecoder.encode(input);
    }
}
