/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dto.Result;
import dto.domain.Region;
import dto.domain.Town;
import dto.filters.TownFilter;
import dto.session.Session;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import services.server.TownDAIF;

/**
 *
 * @author root
 */
public class TownDAImpl implements TownDAIF {

    @Override
    public Result<Town> insertTown(List<Town> ins, Session session) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<Town> updateTown(List<Town> upd, Session session) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<Town> deleteTown(List<Town> del, Session session) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<Town> fetchTown(TownFilter filter) throws RemoteException {
        List<Town> lst = new ArrayList<>();
        if (filter.ids.size() == 1) {
            int i = filter.ids.get(0);
            Town twn = new Town(i);
            Region reg = new Region(i);
            twn.name = "town " + i;
            twn.region = reg;
            lst.add(twn);
        } else {
            for (int i = 0; i < 5; i++) {
                Town twn = new Town(i);
                Region reg = new Region(i);
                twn.name = "town " + i;
                twn.region = reg;
                lst.add(twn);
            }
        }

        Result<Town> res = new Result(lst, null);
        return res;
    }

}
