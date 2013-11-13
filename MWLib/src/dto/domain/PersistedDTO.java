/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author root
 */
public abstract class PersistedDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    //all private fields must be READONLY
    //rest of the fields - public for ease of access
    //getter methods - required for javaFX viewresolvers
    
    private int ID = 0;
    //most entities are expected to have these fields also
    private Date timeIns;
    private Date timeUpd;
    private User userIns;
    private User userUpd;
    
    public PersistedDTO(int ID){
        this.ID = ID;
    }
    
    public PersistedDTO(int ID, Date timeIns, Date timeUpd, User userIns, User userUpd){
        this.ID = ID;
        this.timeIns = timeIns;
        this.timeUpd = timeUpd;
        this.userIns = userIns;
        this.userUpd = userUpd;
    }
    
    public int getID(){
        return this.ID;
    }
    
    public Date getTimeIns(){
        return timeIns;
    }
    
    public Date getTimeUpd(){
        return timeUpd;
    }
    
    public User getUserIns(){
        return this.userIns;
    }
    
    public User getUserUpd(){
        return this.userUpd;
    }
}
