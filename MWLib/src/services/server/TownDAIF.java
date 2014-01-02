/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import dto.Result;
import dto.domain.Region;
import dto.domain.Town;
import dto.filters.RegionFilter;
import dto.filters.TownFilter;
import dto.session.Session;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Lubomir
 */
public interface TownDAIF extends Remote{
    
    public Result<Town> insertTown(List<Town> ins, Session session) throws RemoteException;
    public Result<Town> updateTown(List<Town> upd, Session session) throws RemoteException;
    public Result<Town> deleteTown(List<Town> del, Session session) throws RemoteException;
    public Result<Town> fetchTown(TownFilter filter) throws RemoteException;
}
