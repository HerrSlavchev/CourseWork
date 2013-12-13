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
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import security.Credentials;
import security.PasswordManagerPBKDF2;
import services.client.NotifiableIF;
import services.server.ClientManagerIF;
import session.BasicSessionCodeProvider;
import session.SessionCodeProviderIF;

/**
 *
 * @author root
 */
public class ClientManagerImpl implements ClientManagerIF {

    private static Map<Integer, NotifiableIF> mapIDsToClients = new HashMap<Integer, NotifiableIF>();
    private static Map<String, Integer> mapCodesToIDs = new HashMap<String, Integer>();
    SessionCodeProviderIF sessionCodeProviderIF = BasicSessionCodeProvider.getInstance();

    public static Map<Integer, NotifiableIF> getClients() {
        return Collections.unmodifiableMap(mapIDsToClients);
    }

    private User login(User user) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, SQLException {

        User u = null;

        String email = user.eMail;
        String password = user.password;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionProvider.getConnection();
            String slct = "SELECT ID, password, hash, iterations FROM user WHERE e_mail = '" + email + "';";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(slct);

            if (rs.getFetchSize() == 1) {
                rs.next();
                int ID = rs.getInt("ID");
                String realPassword = rs.getString("password");
                String realSalt = rs.getString("salt");
                int realIterations = rs.getInt("iterations");
                Credentials creds = new Credentials(realPassword, realSalt, realIterations);
                boolean valid = PasswordManagerPBKDF2.getInstance().validatePassword(password, creds);
                if (valid) {
                    u = new User(ID, null, null, null);
                    slct = "select "
                            + "u.f_name, "
                            + "u.l_name, "
                            + "u.e_mail, "
                            + "u.role "
                            + "from "
                            + "user u "
                            + "where "
                            + "u.ID = " + ID + ";";
                    rs = stmt.executeQuery(slct);
                    rs.next();
                    u.fName = rs.getString("u.f_name");
                    u.lName = rs.getString("u.l_name");
                    u.eMail = rs.getString("u.e_mail");
                    int roleIdx = rs.getInt("u.role");
                    u.role = Role.values()[roleIdx];
                }
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
            exc = e;
        }
        Result res = new Result(lst, exc);
        
        if (dbUser == null || exc != null) {
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
            sessionCodeProviderIF.releaseSessionCode(oldSessionCode);
        }

        String sessionCode = sessionCodeProviderIF.getSessionCode();
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
        BasicSessionCodeProvider.getInstance().releaseSessionCode(sessionCode);
    }

}
