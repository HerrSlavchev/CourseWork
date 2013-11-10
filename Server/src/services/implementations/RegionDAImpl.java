/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dto.Result;
import dto.domain.Region;
import dto.domain.User;
import dto.filters.RegionFilter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import services.server.RegionsDAIF;

/**
 *
 * @author root
 */
public class RegionDAImpl implements RegionsDAIF {

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
            System.out.println("fetching");
            int rand = (int) (5 * Math.random() + 1);
            for (int i = 0; i < rand; i++){
                Region r = new Region(i);
                res.add(r);
            }
            //TODO
        } catch (Exception exc) {
            ex = exc;
        }
        return new Result(res, ex);
    }

}
