/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import dto.Result;
import dto.domain.Interest;
import dto.session.Session;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author root
 */
public interface InterestDAIF extends Remote{
    
    public Result<Interest> insertInterest(List<Interest> ins, Session session) throws RemoteException;
    public Result<Interest> updateInterest(List<Interest> upd, Session session) throws RemoteException;
    public Result<Interest> deleteInterest(List<Interest> del, Session session) throws RemoteException;
    public Result<Interest> fetchInterests(Interest filter) throws RemoteException;
}
