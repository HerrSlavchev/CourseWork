/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package properties;

import dto.domain.User;
import dto.session.Session;

/**
 *
 * @author root
 */
public class Properties {

    private static Session session;
    public static User user;
    
    public static void setSession(Session session) {
        if (Properties.session != null) {
            return;
        }
        Properties.session = session;
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
    
}
