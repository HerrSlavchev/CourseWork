/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.implementations;

import dto.domain.Notification;
import dto.domain.TriggerType;
import dto.domain.User;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import services.client.NotifiableIF;
import services.server.ClientManagerIF;

/**
 *
 * @author root
 */
public class ClientManagerImpl implements ClientManagerIF{

    private static Map<Integer, NotifiableIF> clients = new HashMap<Integer, NotifiableIF>();
    private static int _ITERATIONS = 1000;
    
    public static Map<Integer, NotifiableIF> getClients(){
        return Collections.unmodifiableMap(clients);
    }
    
    @Override
    public void registerClient(NotifiableIF cli, User user) throws RemoteException {
        
        String password = user.password;
        int extraIters = 7;
        
        //if someone has logged with this user, kill his client and register this one
        NotifiableIF old = clients.get(user.getID());
        if (old != null) {
            Notification info = new Notification(0);
            info.triggerType = TriggerType.LOGGED_FROM_ANOTHER_CLIENT;
            List<Notification> news = new ArrayList();
            news.add(info);
            old.acceptNotifications(news);
        }
        
        clients.put(user.getID(), cli);
    }

    @Override
    public void removeClient(User user) throws RemoteException {
        int id = user.getID();
        clients.remove(id);
        //TODO - persist notifications
    }
    
}
