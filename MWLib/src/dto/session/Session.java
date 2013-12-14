/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.session;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class Session implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private final String sessionCode;
    
    public Session(String sessionCode){
        this.sessionCode = sessionCode;
    }
    
    public String getSessionCode(){
        return sessionCode;
    }
    
}
