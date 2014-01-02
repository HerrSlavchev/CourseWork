/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import services.BindingConsts;

/**
 *
 * @author root
 */
public class ServiceExposer {

    //Because of that: http://stackoverflow.com/questions/645208/java-rmi-nosuchobjectexception-no-such-object-in-table
    private static List<Remote> hardReferences = new ArrayList();
    
    private static boolean done = false;
    private static Registry registry = null;
    
    private static <E extends Remote> void register(E daImpl, String key) throws RemoteException, AlreadyBoundException{
        E daIF = daImpl;
        E stub = (E) UnicastRemoteObject.exportObject(daIF, BindingConsts.port);
        hardReferences.add(stub);
        hardReferences.add(daImpl);
        registry.bind(key, stub);
    }
    
    public static void exposeAll() throws Exception {
        
        if (done) { //bootstrap only once
            return;
        }
        
        registry = LocateRegistry.createRegistry(BindingConsts.port);

        register(new ClientManagerImpl(), BindingConsts.CLIENT_MANAGER);
        register(new UserDAImpl(), BindingConsts.USER_DA);
        register(new RegionDAImpl(), BindingConsts.REGION_DA);
        register(new TownDAImpl(), BindingConsts.TOWN_DA);
        register(new CategoryDAImpl(), BindingConsts.CATEGORY_DA);
        register(new SubCategoryDAImpl(), BindingConsts.SUBCATEGORY_DA);
        
        /*
        RegionDAIF regionsIF = new RegionDAImpl(); 
        RegionDAIF regionsStub = (RegionDAIF) UnicastRemoteObject.exportObject(regionsIF, 0); 
        registry.bind(BindingConsts.REGION_DA, regionsStub); 

        ClientManagerIF clmIF = new ClientManagerImpl();
        ClientManagerIF clmStub = (ClientManagerIF) UnicastRemoteObject.exportObject(clmIF, 0); 
        registry.bind(BindingConsts.CLIENT_MANAGER, clmStub);
        */
        
        done = true; //bootstrapping went well
    }
}
