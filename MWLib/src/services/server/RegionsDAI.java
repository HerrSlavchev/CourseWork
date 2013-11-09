/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import dto.Result;
import dto.domain.Region;
import dto.domain.User;
import dto.filters.RegionFilter;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author root
 */
public interface RegionsDAI extends Remote{
    
    public Result<Region> insertRegion(List<Region> ins, User user) throws RemoteException;
    public Result<Region> updateRegion(List<Region> upd, User user) throws RemoteException;
    public Result<Region> deleteRegion(List<Region> del, User user) throws RemoteException;
    public Result<Region> fetchRegions(RegionFilter filter);
}
