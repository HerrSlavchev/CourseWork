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
import dto.session.Session;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author root
 */
public interface UserDAIF extends Remote {

    public Result<User> insertUser(List<User> ins, Session session) throws RemoteException;

    public Result<User> updateUser(List<User> upd, Session session) throws RemoteException;

    public Result<User> deleteUser(List<User> del, Session session) throws RemoteException;

    public Result<User> fetchUsers(UserFilter filter) throws RemoteException;
}
