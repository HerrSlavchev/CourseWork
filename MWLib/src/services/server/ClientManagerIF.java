/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import dto.domain.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import services.client.NotifiableIF;

/**
 *
 * @author root
 */
public interface ClientManagerIF extends Remote{
    
    //call this whenever someone logs in
    public void registerClient(NotifiableIF cli, User user) throws RemoteException;
    
    //call this when loggin out
    public void removeClient(User user) throws RemoteException;
}
