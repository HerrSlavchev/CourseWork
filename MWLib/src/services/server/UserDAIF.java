/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import java.rmi.Remote;
import dto.Result;

import dto.domain.User;
import dto.filters.UserFilter;
import java.rmi.RemoteException;
import java.util.List;
/**
 *
 * @author root
 */
public interface UserDAIF extends Remote{
    
    public Result<User> verify(User user) throws RemoteException;
    
    public Result<User> insertUser(List<User> ins, User user) throws RemoteException;
    public Result<User> updateUser(List<User> upd, User user) throws RemoteException;
    public Result<User> deleteUser(List<User> del, User user) throws RemoteException;
    public Result<User> fetchUsers(UserFilter filter) throws RemoteException;
}
