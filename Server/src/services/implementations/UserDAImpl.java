/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.ConnectionProvider;
import dto.Result;
import dto.domain.User;
import dto.filters.UserFilter;
import dto.rolemanagement.Role;
import dto.session.Session;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import security.Credentials;
import security.PasswordManagerIF;
import security.PasswordManagerPBKDF2;
import services.server.UserDAIF;

/**
 *
 * @author root
 */
public class UserDAImpl implements UserDAIF {

    

    @Override
    public Result<User> insertUser(List<User> ins, Session session) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<User> updateUser(List<User> upd, Session session) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<User> deleteUser(List<User> del, Session session) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<User> fetchUsers(UserFilter filter) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
