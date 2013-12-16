/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.ConnectionProvider;
import dao.DAOUtils;
import dto.Result;
import dto.domain.User;
import dto.filters.UserFilter;
import dto.session.Session;
import exceptions.ExceptionProcessor;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import security.Credentials;
import security.SecurityUtils;
import services.server.UserDAIF;

/**
 *
 * @author root
 */
public class UserDAImpl implements UserDAIF {

    public final int batchSize = 1000;
    
    @Override
    public Result<User> insertUser(List<User> ins, Session session) throws RemoteException {

        
        Result res = null;
        Exception exc = null;
        
        if (session != null && session.getSessionCode() != null && session.getSessionCode().length() > 0) {
            exc = new Exception("Cannot register user while logged in the system.");
            return new Result(null, exc);
        }
        
        String insert = "INSERT INTO user"
                + "(f_name, s_name, l_name, e_mail, description, password, iterations, salt) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionProvider.getConnection();
            stmt = conn.prepareStatement(insert);
            
            int count = 0;
            for (User u : ins) {
                stmt.setString(1,u.fName);
                stmt.setString(2,u.sName);
                stmt.setString(3,u.lName);
                stmt.setString(4,u.eMail);
                stmt.setString(5, u.description);
                
                String password = u.password;
                Credentials creds = SecurityUtils.passwordManager.createCredentials(password);
                
                String hashedPass = creds.getEncodedHashedPassword();
                stmt.setString(6, hashedPass);
                int iterations = creds.getIterations();
                stmt.setInt(7, iterations);
                String salt = creds.getEncodedSalt();
                stmt.setString(8, salt);
                
                stmt.addBatch();
                count++;
                
                if(count == batchSize){
                    count = 0;
                    stmt.executeBatch();
                }
            }
            if(count > 0) {
                stmt.executeBatch();
            }
        } catch (Exception e){
            exc = ExceptionProcessor.processException(e);
        }
        finally {
            try {
                DAOUtils.releaseRes(conn, stmt, null);
            } catch (SQLException SQLe) {
                DAOUtils.processSQLException(SQLe);
            }
        }
        
        res = new Result(null, exc);
        return res;
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
