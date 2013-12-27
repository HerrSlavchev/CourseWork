/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.CRUDHelper;
import dao.DAOUtils;
import dto.Result;
import dto.domain.User;
import dto.filters.UserFilter;
import dto.session.Session;
import exceptions.ExceptionProcessor;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public Result<User> insertUser(final List<User> ins, Session session) throws RemoteException {

        Result res = null;
        Exception exc = null;

        if (session != null && session.getSessionCode() != null && session.getSessionCode().length() > 0) {
            exc = new Exception("Cannot register user while logged in the system.");
            return new Result(null, exc);
        }

        final String insert = "INSERT INTO user"
                + "(f_name, s_name, l_name, e_mail, description, password, iterations, salt) VALUES"
                + "(?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            CRUDHelper helper = new CRUDHelper<User>(session, ins) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
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
                }
            };
            helper.setRightsCheck(false);
            helper.setSessionCheck(false);
            helper.performCUD();
        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
        }

        res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<User> updateUser(final List<User> upd, Session session) throws RemoteException {

        Result res = null;
        List<User> lst = new ArrayList<>();
        Exception exc = null;

        final String updateUser = DAOUtils.generateStmt(
                "UPDATE user SET ",
                "f_name = ?, ",
                "s_name = ?, ",
                "l_name = ?, ",
                "role = ?, ",
                "description = ? ",
                "WHERE ",
                "id = ? ",
                "AND timeupd = ?");
        try {

            CRUDHelper helper = new CRUDHelper<User>(session, upd) {

                //update one or more users
                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    String updateUserExt = this.markUserUpd(updateUser);
                    int count = 0;
                    stmt = conn.prepareStatement(updateUserExt);
                    for (User u : upd) {
                        stmt.setString(1, u.fName);
                        stmt.setString(2, u.sName);
                        stmt.setString(3, u.lName);
                        stmt.setInt(4, u.role.ordinal());
                        stmt.setString(5, u.description);
                        stmt.setInt(6, u.getID());
                        stmt.setTimestamp(7, u.getTimeUpd());

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
                }
            };
            helper.performCUD();

        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
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
        Result<User> res = null;
        List<User> lst = new ArrayList<User>();
        Exception exc = null;

        String fetchFullPersonal = "";
        if (filter.fetchFullPersonalData) {
            fetchFullPersonal = ", u.description";
        }
        String fetchTown = "";
        String joinTown = "";
        if (filter.fetchTowns) {
            fetchTown = DAOUtils.fetchTown;
            joinTown = " LEFT OUTER JOIN town_user tu ON(tu.ID_user = u.ID) " +
                        "LEFT OUTER JOIN town t ON(t.ID = tu.ID_town)";
        }
        String fetchGroups = "";
        String joinGroups = "";
        if (filter.fetchGroups) {
            fetchGroups = ", ig.ID AS ig_ID, ig.name AS ig_name";
            joinGroups = " LEFT OUTER JOIN igroup_user igu ON(igu.ID_user = u.ID) " +
                        "LEFT OUTER JOIN igroup ig ON(ig.ID = igu.ID_igroup)";
        }
        String fetchInterests = "";
        String joinInterests = "";
        if (filter.fetchInterests) {
            fetchInterests = ", i.ID AS i_ID, i.name AS i_name";
            joinInterests = " LEFT OUTER JOIN interest_user iu ON(iu.ID_user = u.ID) " +
                        "LEFT OUTER JOIN interest i ON(i.ID = iu.ID_interest)";
        }
        String fetchEvents = "";
        String joinEvents = "";
        if (filter.fetchEvents) {
            fetchEvents = ", e.ID AS e_ID, e.name AS e_name";
            joinEvents = " LEFT OUTER JOIN event_user eu ON(eu.ID_user = u.ID) " +
                        "LEFT OUTER JOIN event e ON(e.ID = eu.ID_event)";
        }
        String fetchConversations = "";
        String joinConversations = "";
        if (filter.fetchConversations) {
            fetchConversations = ", c.ID AS c_ID, c.topic AS c_topic";
            joinConversations = " LEFT OUTER JOIN conversation_user cu ON(cu.ID_user = u.ID) " +
                        "LEFT OUTER JOIN conversation c ON(c.ID = cu.ID_conversation)";
        }
        
        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                "u.ID, u.f_name, u.s_name, u.l_name, u.role, u.e_mail, u.timeins, u.timeupd, u.ID_userupd",
                fetchFullPersonal,
                fetchTown,
                fetchGroups,
                fetchInterests,
                fetchEvents,
                fetchConversations,
                " FROM user u",
                joinTown,
                joinGroups,
                joinInterests,
                joinEvents,
                joinConversations);
                
        try {
        CRUDHelper<User> helper = new CRUDHelper<User>(null, null) {
            
            @Override
            protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                stmt = conn.prepareStatement(slct);
                rs = stmt.executeQuery();
                for (int i = 1; i < rs.getMetaData().getColumnCount(); i++){
                    System.out.println(rs.getMetaData().getColumnLabel(i) + "|" + rs.getMetaData().getColumnName(i));
                }
                
            }
        };
        helper.setSessionCheck(false);
        helper.setRightsCheck(false);
        helper.performCUD();
        } catch (Exception e){
            exc = ExceptionProcessor.processException(e);
        }
        return res;
    }

}
