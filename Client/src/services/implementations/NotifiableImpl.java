/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.implementations;

import dto.domain.Notification;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import services.client.NotifiableIF;

/**
 *
 * @author root
 */
public class NotifiableImpl implements NotifiableIF, Serializable{

    private static final long serialVersionUID = 1L;
    
    @Override
    public void acceptNotifications(List<Notification> news) throws RemoteException {
        for(Notification n : news){
            System.out.println(n.getTriggerType());
        }
    }
    
}
