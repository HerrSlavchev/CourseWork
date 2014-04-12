/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.CRUDHelper;
import dao.DAOUtils;
import dao.FilterUtils;
import dao.ResultSetInterpreterIF;
import dto.Result;
import dto.domain.Conversation;
import dto.domain.Event;
import dto.domain.Group;
import dto.domain.Interest;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.UserFilter;
import dto.rolemanagement.Role;
import dto.session.Session;
import exceptions.ExceptionProcessor;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import security.Credentials;
import security.PasswordManagerIF;
import security.SecurityUtils;
import services.server.UserDAIF;

/**
 *
 * @author root
 */
public class UserDAImpl implements UserDAIF {

    private static final int batchSize = DAOUtils.MAX_BATCH_SIZE;
    private static final PasswordManagerIF passwordManager = SecurityUtils.passwordManager;
    private ResultSetInterpreterIF resultSetInterpreter = DAOUtils.resultSetInterpreter;

    private UserDAImpl(){
        
    }
    
    private static UserDAImpl instance;
    
    public static UserDAImpl getInstance(){
        if (instance == null){
            instance = new UserDAImpl();
        }
        return instance;
    }
    
    
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
                        stmt.setString(1, u.getFirstName());
                        stmt.setString(2, u.getSurName());
                        stmt.setString(3, u.getLastName());
                        stmt.setString(4, u.getE_Mail());
                        stmt.setString(5, u.getDescription());

                        String password = u.getPassword();
                        Credentials creds = passwordManager.createCredentials(password);

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
                        stmt.setString(1, u.getFirstName());
                        stmt.setString(2, u.getSurName());
                        stmt.setString(3, u.getLastName());
                        stmt.setInt(4, u.getRole().ordinal());
                        stmt.setString(5, u.getDescription());
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
    public Result<User> fetchUsers(final UserFilter filter) throws RemoteException {
        Result<User> res = null;
        final List<User> lst = new ArrayList<User>();
        Exception exc = null;

        String fetchFullPersonal = "";
        if (filter.fetchFullPersonalData) {
            fetchFullPersonal = ", usr.s_name AS usr_s_name, usr.role AS usr_role, usr.timeins AS usr_timeins, usr.ID_userupd, usr.description AS usr_description";
        }
        String fetchTown = "";
        String joinTown = "";
        if (filter.fetchTowns) {
            fetchTown = FilterUtils.fetchTown;
            joinTown = " LEFT OUTER JOIN town_user tu ON(tu.ID_user = usr.ID) "
                    + "LEFT OUTER JOIN town twn ON(twn.ID = tu.ID_town)";
        }
        String fetchGroups = "";
        String joinGroups = "";
        if (filter.fetchGroups) {
            fetchGroups = FilterUtils.fetchGroup;
            joinGroups = " LEFT OUTER JOIN igroup_user igu ON(igu.ID_user = usr.ID) "
                    + "LEFT OUTER JOIN igroup grp ON(grp.ID = igu.ID_igroup)";
        }
        String fetchInterests = "";
        String joinInterests = "";
        if (filter.fetchInterests) {
            fetchInterests = FilterUtils.fetchInterest;
            joinInterests = " LEFT OUTER JOIN interest_user iu ON(iu.ID_user = usr.ID) "
                    + "LEFT OUTER JOIN interest intr ON(intr.ID = iu.ID_interest)";
        }
        String fetchEvents = "";
        String joinEvents = "";
        if (filter.fetchEvents) {
            fetchEvents = FilterUtils.fetchEvent;
            joinEvents = " LEFT OUTER JOIN event_user eu ON(eu.ID_user = usr.ID) "
                    + "LEFT OUTER JOIN event evt ON(evt.ID = eu.ID_event)";
        }
        String fetchConversations = "";
        String joinConversations = "";
        if (filter.fetchConversations) {
            fetchConversations = FilterUtils.fetchConversation;
            joinConversations = " LEFT OUTER JOIN conversation_user cu ON(cu.ID_user = usr.ID) "
                    + "LEFT OUTER JOIN conversation con ON(con.ID = cu.ID_conversation)";
        }

        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                FilterUtils.fetchUser.replaceFirst(", ", ""),
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

                    Map<Integer, User> map = new HashMap<>();
                    while (rs.next()) {
                        User curr = resultSetInterpreter.getUser(rs);
                        User old = map.get(curr.getID());
                        if (old == null) {
                            curr.setUserIns(resultSetInterpreter.getUserIns(rs));
                            curr.setUserUpd(resultSetInterpreter.getUserUpd(rs));
                            if (filter.deepFetch) {
                                curr.setDescription(rs.getString("usr_description"));
                                curr.setSurName(rs.getString("usr_s_name"));
                                curr.setRole(Role.values()[rs.getInt("usr_role")]);
                            }
                            old = curr;
                            map.put(old.getID(), old);
                        }
                        if (filter.fetchTowns) {
                            Town twn = resultSetInterpreter.getTown(rs);
                            if (twn != null) {
                                old.getTowns().addOldChild(twn);
                            }
                        }
                        if (filter.fetchInterests) {
                            Interest intr = resultSetInterpreter.getInterest(rs);
                            if (intr != null) {
                                old.getInterests().add(intr);
                            }
                        }
                        if (filter.fetchConversations) {
                            Conversation conv = resultSetInterpreter.getConversation(rs);
                            if (conv != null) {
                                old.getConversations().add(conv);
                            }
                        }
                        if (filter.fetchGroups) {
                            Group g = resultSetInterpreter.getGroup(rs);
                            if (g != null) {
                                old.getGroups().add(g);
                            }
                        }
                        if (filter.fetchEvents) {
                            Event evt = resultSetInterpreter.getEvent(rs);
                            if (evt != null) {
                                old.getEvents().add(evt);
                            }
                        }
                    }

                    lst.addAll(map.values());
                }
            };
            helper.setSessionCheck(false);
            helper.setRightsCheck(false);
            helper.performCUD();
        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
        }
        res = new Result<>(lst, exc);
        return res;
    }

}
