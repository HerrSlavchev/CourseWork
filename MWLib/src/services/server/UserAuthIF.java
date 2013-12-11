/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import java.rmi.Remote;
import dto.Result;
import dto.domain.User;
import java.rmi.RemoteException;
/**
 *
 * @author root
 */
public interface UserAuthIF extends Remote{
    
    public Result<User> verify(User user) throws RemoteException;
}
