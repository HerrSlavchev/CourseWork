/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author root
 */
public class BasicSessionCodeProvider implements SessionCodeProviderIF{
    
    public static final boolean ALLOW_SESSION_REUSE = false;
    
    private Deque<String> codePool;
    private int codeCounter;
    
    public static final int MAX_POOL_SIZE = 128;
    
    private static BasicSessionCodeProvider instance;
    
    private BasicSessionCodeProvider(){
        codeCounter = 0;
        codePool = new ArrayDeque<String>(MAX_POOL_SIZE);
    }
    
    public static BasicSessionCodeProvider getInstance(){
        if (instance == null){
            instance = new BasicSessionCodeProvider();
        }
        return instance;
    }
    
    
    public String getSessionCode(){
        String code = "";
        synchronized(this) {
            if (false == ALLOW_SESSION_REUSE || codePool.isEmpty()) {
                code = codeCounter + "";
                codeCounter++;
            } else {
                code = codePool.pop();
            }
        }
        return code;
    }
    
    public void releaseSessionCode(String sessionCode){
        
        if(false == ALLOW_SESSION_REUSE){
            return;
        }
        
        synchronized(this){
            if (codePool.size() < MAX_POOL_SIZE) {
                codePool.push(sessionCode);
            }
        }
    }

}
