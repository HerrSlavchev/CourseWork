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
    
    private Deque<String> codePool;
    private int codeCounter;
    
    public static final int MAX_POOL_SIZE = 128;
    
    private BasicSessionCodeProvider instance;
    
    private BasicSessionCodeProvider(){
        codeCounter = 0;
        codePool = new ArrayDeque<String>(MAX_POOL_SIZE);
    }
    
    public BasicSessionCodeProvider getInstance(){
        if (instance == null){
            instance = new BasicSessionCodeProvider();
        }
        return instance;
    }
    
    
    public String getSessionCode(){
        String code = "";
        synchronized(this) {
            if (codePool.isEmpty()) {
                code = codeCounter + "";
                codeCounter++;
                
            } else {
                code = codePool.pop();
            }
        }
        return code;
    }
    
    public void releaseSessionCode(String sessionCode){
        
        synchronized(this){
            if (codePool.size() < MAX_POOL_SIZE) {
                codePool.push(sessionCode);
            }
        }
    }

}
