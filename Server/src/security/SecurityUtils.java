/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package security;

/**
 *
 * @author root
 */
public class SecurityUtils {
    
    public static final PasswordManagerIF passwordManager = PasswordManagerPBKDF2.getInstance();
    
    public static void processSecurityException(Exception e){
        e.printStackTrace();
    }
}
