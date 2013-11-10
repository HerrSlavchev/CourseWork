/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dto.Result;
import dto.domain.Notification;
import dto.domain.NotificationType;
import dto.domain.Region;
import dto.domain.User;
import dto.filters.RegionFilter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import services.client.NotifiableIF;
import services.server.RegionDAIF;

/**
 *
 * @author root
 */
public class RegionDAImpl implements RegionDAIF {

    @Override
    public Result<Region> insertRegion(List<Region> ins, User user) throws RemoteException {
        List<Region> res = new ArrayList<Region>();
        Exception ex = null;
        try {
            System.out.println("inserting");
            //TODO
        } catch (Exception exc) {
            ex = exc;
        }
        return new Result(res, ex);
    }

    @Override
    public Result<Region> updateRegion(List<Region> upd, User user) throws RemoteException {
        List<Region> res = new ArrayList<Region>();
        Exception ex = null;
        try {
            System.out.println("updating");
            //TODO
        } catch (Exception exc) {
            ex = exc;
        }
        return new Result(res, ex);
    }

    @Override
    public Result<Region> deleteRegion(List<Region> del, User user) throws RemoteException {
        List<Region> res = new ArrayList<Region>();
        Exception ex = null;
        try {
            System.out.println("deleting");
            //TODO
        } catch (Exception exc) {
            ex = exc;
        }
        return new Result(res, ex);
    }

    @Override
    public Result<Region> fetchRegions(RegionFilter filter) {
        List<Region> res = new ArrayList<Region>();
        Exception ex = null;
        try {
            //WARNING
            //System.out.println("fetching");
            int rand = (int) (5 * Math.random() + 1);
            for (int i = 0; i < rand; i++){
                Region r = new Region(i);
                r.name = "huc " + i;
                res.add(r);
            }
            /*for (NotifiableIF ntf : ClientManagerImpl.getClients().values()){
                int idx = (int) (Math.random() * NotificationType.values().length);
                Notification info = new Notification(0, NotificationType.values()[idx], null);
                List<Notification> news = new ArrayList<Notification>();
                news.add(info);
                ntf.acceptNotifications(news);
            }*/
            //TODO
        } catch (Exception exc) {
            ex = exc;
        }
        return new Result(res, ex);
    }

}
