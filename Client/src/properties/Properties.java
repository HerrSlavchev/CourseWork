/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package properties;

import dto.session.Session;

/**
 *
 * @author root
 */
public class Properties {

    private static Session session;

    public static void setSession(Session session) {
        if (Properties.session != null) {
            return;
        }
        Properties.session = session;
        System.out.println("reg: " + session.getSessionCode());
    }
    
    public static Session getSession(){
        return session;
    }
}
