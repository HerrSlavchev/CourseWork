/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webdao;

import dto.domain.Notification;
import dto.domain.TriggerType;
import dto.session.Session;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import javax.servlet.http.HttpSession;
import properties.SessionProperties;
import services.client.NotifiableIF;

/**
 *
 * @author root
 */
public class NotifiableImpl implements NotifiableIF, Serializable {

    private static final long serialVersionUID = 1L;
    
    private HttpSession session;
    
    public NotifiableImpl(HttpSession session){
        this.session = session;
    }
    
    @Override
    public void acceptNotifications(List<Notification> news) throws RemoteException {
        for (Notification n : news) {
            final TriggerType tt = n.getTriggerType();
            if (tt == TriggerType.LOGGED_FROM_ANOTHER_CLIENT) {
                /* Kill session... somehow
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showError(tt.getMessage(), Client.getMainPageStage());
                        Client.getMainPageController().setLogged(false);
                    }
                });
                */
                logout();
            }
        }
    }

    @Override
    public String getSessionCode() throws RemoteException {
        return (String) session.getAttribute("sessionCode");
    }

    @Override
    public void setSessionCode(String sessionCode) throws RemoteException {
        session.setAttribute("sessionCode", sessionCode);
    }

    @Override
    public void logout() throws RemoteException {
        System.out.println("logging out");
        session.removeAttribute("sessionCode"); 
    }

}
