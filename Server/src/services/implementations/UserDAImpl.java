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
import dto.rolemanagement.Role;
import dto.session.Session;
import exceptions.ExceptionProcessor;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    private static final int batchSize = DAOUtils.MAX_BATCH_SIZE;

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
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(insert);

            int count = 0;
            for (User u : ins) {
                stmt.setString(1, u.fName);
                stmt.setString(2, u.sName);
                stmt.setString(3, u.lName);
                stmt.setString(4, u.eMail);
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

                if (count == batchSize) {
                    count = 0;
                    int[] batchRes = stmt.executeBatch();
                    if (DAOUtils.processBatchRes(batchRes) == false) {
                        throw new Exception("Operation failed. Data has been remotely modified.");
                    }
                }
            }
            if (count > 0) {
                int[] batchRes = stmt.executeBatch();
                if (DAOUtils.processBatchRes(batchRes) == false) {
                    throw new Exception("Operation failed. Data has been remotely modified.");
                }
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            exc = ExceptionProcessor.processException(e);
            try {
                conn.rollback();
            } catch (SQLException SQLe) {
                exc = ExceptionProcessor.processException(SQLe);
            }
        } finally {
            try {
                DAOUtils.releaseRes(conn, stmt, null);
            } catch (SQLException SQLe) {
                exc = ExceptionProcessor.processException(SQLe);
            }
        }

        res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<User> updateUser(List<User> upd, Session session) throws RemoteException {
        
        Result res = null;
        List<User> lst = new ArrayList<>();
        Exception exc = null;

        if (session == null) {
            exc = new Exception("Failed to validate the session. Please login.");
            return new Result(null, exc);
        }

        Integer id = ClientManagerImpl.getID(session);
        if (id == null) {
            exc = new Exception("Failed to validate the session. Please login.");
            return new Result(null, exc);
        }

        //check if we are trying to modify our own account
        boolean idMatch = upd.size() == 1 && id.equals(upd.get(0).getID());

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User ownUser = null;
        
        final String isAllowed = "SELECT EXISTS (SELECT 1 FROM "
                + "user u "
                + "WHERE id = ? "
                + "AND role = ?)";
        final String updateUser = "UPDATE user SET "
                + "f_name = ?, "
                + "s_name = ?, "
                + "l_name = ?, "
                + "role = ?, "
                + "id_userupd = ?, "
                + "description = ? "
                + "WHERE "
                + "id = ? "
                + "AND timeupd = ?";
        final String fetchOwnUserDetails = "SELECT "
                + "u.f_name, "
                + "u.s_name, "
                + "u.l_name, "
                + "u.e_mail, "
                + "u.role, "
                + "u.description, "
                + "u.timeins, "
                + "u.timeupd "
                + "FROM user u "
                + "WHERE "
                + "u.id = ?";
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);
            
            //check if we have rights to mess with other accounts
            if (false == idMatch) {
                stmt = conn.prepareStatement(isAllowed);
                stmt.setInt(1, id);
                stmt.setInt(2, Role.ADMIN.ordinal());
                rs = stmt.executeQuery();
                if (rs.getFetchSize() == 0) {
                    exc = new Exception("You do not have sufficient rights for this action.");
                    return new Result(null, exc);
                }
            }
            
            //update one or more users
            int count = 0;
            stmt = conn.prepareStatement(updateUser);
            for (User u : upd) {
                stmt.setString(1, u.fName);
                stmt.setString(2, u.sName);
                stmt.setString(3, u.lName);
                stmt.setInt(4, u.role.ordinal());
                stmt.setInt(5, id);
                stmt.setString(6, u.description);
                stmt.setInt(7, u.getID());
                stmt.setTimestamp(8, u.getTimeUpd());

                stmt.addBatch();
                count++;

                if (count == batchSize) {
                    count = 0;
                    int[] batchRes = stmt.executeBatch();
                    if (DAOUtils.processBatchRes(batchRes) == false) {
                        throw new Exception("Operation failed. Data has been remotely modified.");
                    }
                }
            }
            if (count > 0) {
                int[] batchRes = stmt.executeBatch();
                if (DAOUtils.processBatchRes(batchRes) == false) {
                    throw new Exception("Operation failed. Data has been remotely modified.");
                }
            }
            
            //fetch data about our user
            stmt = conn.prepareStatement(fetchOwnUserDetails);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            rs.next();
            Timestamp timeins = rs.getTimestamp("timeins");
            Timestamp timeupd = rs.getTimestamp("timeupd");
            
            ownUser = new User(id, timeins, timeupd, null);
            ownUser.description = rs.getString("description");
            ownUser.eMail = rs.getString("e_mail");
            ownUser.fName = rs.getString("f_name");
            ownUser.sName = rs.getString("s_name");
            ownUser.lName = rs.getString("l_name");
            ownUser.role = Role.values()[rs.getInt("role")];
            lst.add(ownUser);
            
            conn.commit();
        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
        } finally {
            try {
                DAOUtils.releaseRes(conn, stmt, rs);
            } catch (SQLException SQLe) {
                exc = ExceptionProcessor.processException(SQLe);
            }
        }

        res = new Result(lst, exc);
        return res;
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
