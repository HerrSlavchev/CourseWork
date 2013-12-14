/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

import dao.DAOUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import security.SecurityUtils;

/**
 * Determines if we should process the exception via some local utilities
 * or send it back to the client as part of the Result<E> answer
 * returns: null if processed locally or the same exception otherwise
 * @author root
 */
public class ExceptionProcessor {
    
    
    public static Exception processException(Exception e){
        if (e instanceof SQLException){
            SQLException SQLe = (SQLException) e;
            DAOUtils.processSQLException(SQLe);
        } else if (e instanceof IOException) {
            IOException IOe = (IOException) e;
            SecurityUtils.processSecurityException(IOe);
        } else if (e instanceof NoSuchAlgorithmException) {
            NoSuchAlgorithmException NSAe = (NoSuchAlgorithmException) e;
            SecurityUtils.processSecurityException(NSAe);
        } else if (e instanceof InvalidKeySpecException) {
            InvalidKeySpecException invalidKeySpecException = (InvalidKeySpecException) e;
            SecurityUtils.processSecurityException(invalidKeySpecException);
        } else {
            return e;
        }
        return null;
    }
}
