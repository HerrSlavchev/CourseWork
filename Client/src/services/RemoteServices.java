/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import services.server.UserDAIF;

/**
 *
 * @author root
 */
public class RemoteServices {

    private static Map<String, Remote> stubs = new HashMap<String, Remote>();

    private static boolean done = false;
    private static Registry reg = null;

    public static void init() throws Exception {
        if (reg != null) {
            return;
        }

        reg = LocateRegistry.getRegistry(BindingConsts.port);
    }

    public static Remote getStub(String key) {
        Remote stub = stubs.get(key);
        if (stub == null) {
            try {
                stub = (Remote) reg.lookup(key);
                stubs.put(key, stub);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stub;
    }
}
