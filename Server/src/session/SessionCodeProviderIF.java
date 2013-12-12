/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

/**
 *
 * @author root
 */
public interface SessionCodeProviderIF {
    
    public String getSessionCode();
    public void releaseSessionCode(String sessionCode);
    
}
