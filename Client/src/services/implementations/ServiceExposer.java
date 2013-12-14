/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import example.Client;
import java.rmi.server.UnicastRemoteObject;
import services.client.NotifiableIF;
import utils.Utils;

/**
 *
 * @author root
 */
public class ServiceExposer {

    public static final NotifiableIF client = getStub();

    private static NotifiableIF getStub() {
        NotifiableIF notifiableStub = null;
        try {
            NotifiableIF notifiableIF = new NotifiableImpl();
            notifiableStub = (NotifiableIF) UnicastRemoteObject.exportObject(notifiableIF, 0);
            return notifiableStub;
        } catch (Exception e) {
            Utils.showError(e.getMessage(), Client.getMainPageStage());
        }
        return notifiableStub;
    }
}
