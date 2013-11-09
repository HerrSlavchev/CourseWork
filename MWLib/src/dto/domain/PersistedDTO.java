/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.io.Serializable;

/**
 *
 * @author root
 */
public abstract class PersistedDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int ID = 0;
    private long timeIns = 0;
    private long timeUpd = 0;
    private int userIns = 0;
    private int userUpd = 0;
    
    public PersistedDTO(int ID){
        this.ID = ID;
    }
    
    public PersistedDTO(int ID, long timeIns, long timeUpd, int userIns, int userUpd){
        this.ID = ID;
        this.timeIns = timeIns;
        this.timeUpd = timeUpd;
        this.userIns = userIns;
        this.userUpd = userUpd;
    }
    
    public int getID(){
        return this.ID;
    }
    
    public long getTimeIns(){
        return timeIns;
    }
    
    public long getTimeUpd(){
        return timeUpd;
    }
    
    public int getUserIns(){
        return this.userIns;
    }
    
    public int getUserUpd(){
        return this.userUpd;
    }
}
