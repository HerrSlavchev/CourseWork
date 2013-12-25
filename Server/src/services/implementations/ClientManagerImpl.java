/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.ConnectionProvider;
import dao.DAOUtils;
import dto.Result;
import dto.domain.Notification;
import dto.domain.TriggerType;
import dto.domain.User;
import dto.rolemanagement.Role;
import dto.session.Session;
import exceptions.ExceptionProcessor;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import security.Credentials;
import security.SecurityUtils;
import services.client.NotifiableIF;
import services.server.ClientManagerIF;
import session.SessionUtils;

/**
 *
 * @author root
 */
public class ClientManagerImpl implements ClientManagerIF {

    private static Map<Integer, NotifiableIF> mapIDsToClients = new HashMap<Integer, NotifiableIF>();
    private static Map<String, Integer> mapCodesToIDs = new HashMap<String, Integer>();

    public static Map<Integer, NotifiableIF> getClients() {
        return Collections.unmodifiableMap(mapIDsToClients);
    }

    public static Integer getID(Session session){
        return mapCodesToIDs.get(session.getSessionCode());
    }
    
    private User login(User user) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, SQLException, Exception {

        User u = null;

        String email = user.eMail;
        String password = user.password;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionProvider.getConnection();
            String slct = "SELECT ID, password, salt, iterations FROM user WHERE e_mail = ?";
            stmt = conn.prepareStatement(slct);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.first()) {
                int ID = rs.getInt("ID");
                String realPassword = rs.getString("password");
                String realSalt = rs.getString("salt");
                int realIterations = rs.getInt("iterations");
                Credentials creds = new Credentials(realPassword, realSalt, realIterations);
                boolean valid = SecurityUtils.passwordManager.validatePassword(password, creds);
                if (valid) {
                    
                    slct = "select "
                            + "u.f_name, "
                            + "u.s_name, "
                            + "u.l_name, "
                            + "u.e_mail, "
                            + "u.description, "
                            + "u.role, "
                            + "u.timeins, "
                            + "u.timeupd "
                            + "from "
                            + "user u "
                            + "where "
                            + "u.ID = ?";
                    stmt = conn.prepareStatement(slct);
                    stmt.setInt(1, ID);
                    rs = stmt.executeQuery();
                    rs.next();
                    Timestamp timeins = rs.getTimestamp("timeins");
                    Timestamp timeupd = rs.getTimestamp("timeupd");
                    u = new User(ID, timeins, timeupd, null);
                    u.fName = rs.getString("f_name");
                    u.sName = rs.getString("s_name");
                    u.lName = rs.getString("l_name");
                    u.eMail = rs.getString("e_mail");
                    u.description = rs.getString("description");
                    int roleIdx = rs.getInt("role");
                    u.role = Role.values()[roleIdx];
                } else {
                    throw new Exception("Password and e-mail do not match!");
                }
            } else {
                throw new Exception("Password and e-mail do not match!");
            }
        } finally {
            DAOUtils.releaseRes(conn, stmt, rs);
        }
        return u;
    }

    @Override
    public Result<User> registerClient(NotifiableIF cli, User user) throws RemoteException {

        List<User> lst = new ArrayList<>();
        User dbUser = null;
        Throwable exc = null;
        
        try {
            dbUser = login(user);
            lst.add(dbUser);
        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
        }
        Result res = new Result(lst, exc);
        
        if (exc != null) {
            return res;
        }
        
        
        int userID = dbUser.getID();
        
        //if someone has logged with this user, kill his client and register this one
        NotifiableIF old = mapIDsToClients.get(userID);
        if (old != null) {
            Notification info = new Notification(0);
            info.triggerType = TriggerType.LOGGED_FROM_ANOTHER_CLIENT;
            List<Notification> news = new ArrayList();
            news.add(info);
            old.acceptNotifications(news);
            String oldSessionCode = old.getSessionCode();
            mapCodesToIDs.remove(oldSessionCode);
            mapIDsToClients.remove(userID);
            SessionUtils.sessionCodeProvider.releaseSessionCode(oldSessionCode);
        }
        
        
        String sessionCode = SessionUtils.sessionCodeProvider.getSessionCode();
        mapCodesToIDs.put(sessionCode, userID);
        mapIDsToClients.put(userID, cli);
        cli.setSessionCode(sessionCode);
        
        return res;
    }

    @Override
    public void removeClient(Session session) throws RemoteException {
        
        String sessionCode = session.getSessionCode();
        Integer userID = mapCodesToIDs.remove(sessionCode);
        if (userID == null){
            return;
        }
        NotifiableIF cli = mapIDsToClients.remove(userID);
        if (cli == null){
            return;
        }
        
        SessionUtils.sessionCodeProvider.releaseSessionCode(sessionCode);
        
        cli.logout();
    }

}
