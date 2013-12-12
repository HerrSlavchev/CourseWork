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
            String slct = "SELECT ID, password, hash, iterations FROM user WHERE e_mail = '" + email + "';";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(slct);

            if (rs.getFetchSize() == 0) {
                exc = new Exception("Wrong e-mail or password");
            } else if (rs.getFetchSize() > 1) {
                exc = new Exception("More than one users are registered with this e-mail.");
            } else {
                rs.next();
                int ID = rs.getInt("ID");
                String realPassword = rs.getString("password");
                String realSalt = rs.getString("salt");
                int realIterations = rs.getInt("iterations");
                Credentials creds = new Credentials(realPassword, realSalt, realIterations);
                boolean valid = PasswordManagerPBKDF2.getInstance().validatePassword(password, creds);
                if (false == valid) {
                    exc = new Exception("Wrong e-mail or password.");
                } else {
                    User u = new User(ID, null, null, null);
                    slct = "select " + 
                                "u.f_name, " + 
                                "u.l_name, " +
                                "u.e_mail, " +
                                "u.role " +
                            "from " +
                                "user u " +
                            "where " +
                                "u.ID = " + ID + ";";
                    rs = stmt.executeQuery(slct);
                    rs.next();
                    u.fName = rs.getString("u.f_name");
                    u.lName = rs.getString("u.l_name");
                    u.eMail = rs.getString("u.e_mail");
                    int roleIdx = rs.getInt("u.role");
                    u.role = Role.values()[roleIdx];
                    lst.add(u);
                }

            }
        } catch (SQLException SQLe) {
            exc = SQLe;
        } catch (Exception e) {
            exc = e;
        }
        
        if(true) {
            throw new UnsupportedOperationException("MUST ATTACH TO CLIENTMANAGER!");
        }
        //ATTACH CLIENT TO CLIENTMANAGER!
        return new Result(lst, exc);
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
