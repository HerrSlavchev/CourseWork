/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import dto.Result;
import dto.domain.User;
import dto.session.Session;
import java.rmi.Remote;
import java.rmi.RemoteException;
import services.client.NotifiableIF;

/**
 *
 * @author root
 */
public interface ClientManagerIF extends Remote{
        
    public Result<User> registerClient(NotifiableIF cli, User user) throws RemoteException;
    
    public void removeClient(Session session) throws RemoteException;
}
