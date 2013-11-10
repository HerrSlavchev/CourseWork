/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import services.BindingConsts;
import services.server.ClientManagerIF;
import services.server.RegionDAIF;

/**
 *
 * @author root
 */
public class ServiceExposer {

    private static boolean done = false;

    public static void exposeAll() throws Exception {
        
        if (done) { //bootstrap only once
            return;
        }
        
        Registry registry = LocateRegistry.createRegistry(BindingConsts.port);

        RegionDAIF regionsIF = new RegionDAImpl(); //local implementation
        RegionDAIF regionsStub = (RegionDAIF) UnicastRemoteObject.exportObject(regionsIF, 0); //expose
        registry.bind(BindingConsts.REGION_DA, regionsStub); //publish to the registry

        ClientManagerIF clmIF = new ClientManagerImpl();
        ClientManagerIF clmStub = (ClientManagerIF) UnicastRemoteObject.exportObject(clmIF, 0); 
        registry.bind(BindingConsts.CLIENT_MANAGER, clmStub);
        
        done = true; //bootstrapping went well
    }
}
