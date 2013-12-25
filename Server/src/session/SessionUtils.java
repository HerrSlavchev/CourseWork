/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

/**
  * Centralized exposer of session interface implementations.
 * All implementations should have protected creational methods,
 * thus forcing users of the framework to imply good programming practices.
 * Should one implementation be changed by another, by design, the only needed
 * change should occur here.
 * Later interface-implementation configuration can be externalized to a props file.
 * @author Herr_Slavchev
 */
public class SessionUtils {
    
    public static final SessionCodeProviderIF sessionCodeProvider = BasicSessionCodeProvider.getInstance();
}
