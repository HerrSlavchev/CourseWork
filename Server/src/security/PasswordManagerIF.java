/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import security.Credentials;

/**
 *
 * @author root
 */
public interface PasswordManagerIF {

    public Credentials createCredentials(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

    public boolean validatePassword(String password, Credentials creds) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
