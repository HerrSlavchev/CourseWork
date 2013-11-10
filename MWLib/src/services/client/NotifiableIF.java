/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.client;

import dto.domain.Notification;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author root
 */
public interface NotifiableIF extends Remote{
    
    public void acceptNotifications(List<Notification> news) throws RemoteException;
}
