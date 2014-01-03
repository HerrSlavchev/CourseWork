/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.implementations;

import dto.Result;
import dto.domain.Interest;
import dto.session.Session;
import java.rmi.RemoteException;
import java.util.List;
import services.server.InterestDAIF;

/**
 *
 * @author root
 */
public class InterestDAImpl implements InterestDAIF{

    @Override
    public Result<Interest> insertInterest(List<Interest> ins, Session session) throws RemoteException {
        return new Result(null, new Throwable("NOT DONE YET!"));
    }

    @Override
    public Result<Interest> updateInterest(List<Interest> upd, Session session) throws RemoteException {
        return new Result(null, new Throwable("NOT DONE YET!"));
    }

    @Override
    public Result<Interest> deleteInterest(List<Interest> del, Session session) throws RemoteException {
        return new Result(null, new Throwable("NOT DONE YET!"));
    }

    @Override
    public Result<Interest> fetchInterests(Interest filter) throws RemoteException {
        return new Result(null, new Throwable("NOT DONE YET!"));
    }
    
    
}
