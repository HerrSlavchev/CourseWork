/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dto.domain.Notification;
import dto.domain.TriggerType;
import dto.session.Session;
import view.Client;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import javafx.application.Platform;
import properties.SessionProperties;
import services.client.NotifiableIF;
import utils.Utils;

/**
 *
 * @author root
 */
public class NotifiableImpl implements NotifiableIF, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void acceptNotifications(List<Notification> news) throws RemoteException {
        for (Notification n : news) {
            final TriggerType tt = n.getTriggerType();
            if (tt == TriggerType.LOGGED_FROM_ANOTHER_CLIENT) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showError(tt.getMessage(), Client.getMainPageStage());
                        Client.getMainPageController().setLogged(false);
                    }
                });

            }
        }
    }

    @Override
    public String getSessionCode() throws RemoteException {
        return SessionProperties.getSession().getSessionCode();
    }

    @Override
    public void setSessionCode(String sessionCode) throws RemoteException {
        Session session = new Session(sessionCode);
        SessionProperties.setSession(session);
    }

    @Override
    public void logout() throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Client.getMainPageController().setLogged(false);
                SessionProperties.user = null;
            }
        });

    }

}
