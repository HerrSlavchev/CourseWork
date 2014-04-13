/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package properties;

import dto.domain.PersistedDTO;
import dto.domain.User;
import dto.rolemanagement.Role;
import dto.session.Session;

/**
 *
 * @author root
 */
public class SessionProperties {

    private static Session session = null;
    public static User user = null;
    
    public static void setSession(Session session) {
        if (SessionProperties.session != null) {
            return;
        }
        SessionProperties.session = session;
    }

    public static Session getSession() {
        return session;
    }

    public static void killSession() {
        session = null;
    }

    public static boolean isLogged() {
        return session != null;
    }
    
    public static boolean hasRole(Role role){
        return (user != null && user.getRole().compareTo(role) == 0);
    }
    
    public static boolean isOwner(PersistedDTO dto){
        User userIns = dto.getUserIns();
        if (user == null || userIns == null){
            return false;
        }
        return userIns.getID() == user.getID();
    }
}
