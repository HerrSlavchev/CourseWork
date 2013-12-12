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
    public Result<User> verify(User user) throws RemoteException {

        List<User> lst = new ArrayList<User>();
        Throwable exc = null;

        String email = user.eMail;
        String password = user.password;

        try {
            Connection conn = ConnectionProvider.getConnection();
            String slct = "SELECT password, hash, iterations FROM user WHERE e_mail = '" + email + "';";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(slct);

            if (rs.getFetchSize() == 0) {
                exc = new Exception("Wrong e-mail or password");
            } else if (rs.getFetchSize() > 1) {
                exc = new Exception("More than one users are registered with this e-mail.");
            } else {
                rs.next();
                String realPassword = rs.getString("password");
                String realSalt = rs.getString("salt");
                int realIterations = rs.getInt("iterations");
                Credentials creds = new Credentials(realPassword, realSalt, realIterations);
                boolean valid = PasswordManagerPBKDF2.getInstance().validatePassword(password, creds);
                if (false == valid) {
                    exc = new Exception("Wrong e-mail or password.");
                } else {
                    //SELECT USER AND ROLE, ATTACH CLIENT TO THE CLIENTMANAGER
                }

            }
        } catch (SQLException SQLe) {
            exc = SQLe;
        } catch (Exception e) {
            exc = e;
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<User> insertUser(List<User> ins, User user) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<User> updateUser(List<User> upd, User user) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<User> deleteUser(List<User> del, User user) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<User> fetchUsers(UserFilter filter) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
