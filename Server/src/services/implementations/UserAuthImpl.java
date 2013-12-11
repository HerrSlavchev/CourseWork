/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.implementations;

import dto.Result;
import dto.domain.User;
import java.rmi.RemoteException;
import services.server.UserAuthIF;

/**
 *
 * @author root
 */
public class UserAuthImpl implements UserAuthIF {

    @Override
    public Result<User> verify(User user) throws RemoteException {
        String email = user.eMail;
        String password = user.password;
        
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
