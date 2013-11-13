/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import dto.rolemanagement.Role;
import java.util.Date;

/**
 *
 * @author root
 */
public class User extends PersistedDTO{
    
    public String fName;
    public String sName;
    public String lName;
    public String eMail;
    public String password;
    public String description;
    public Role role;
    
    public User(int ID, Date timeIns, Date timeUpd, User userUpd) {
        super(ID, timeIns, timeUpd, null, userUpd);
    }
    
    public String getFName(){
        return fName;
    }
    
    public String getSName(){
        return sName;
    }
    
    public String getLName(){
        return lName;
    }
    
    public String getEMail(){
        return eMail;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getDescription(){
        return description;
    }
    
    public Role getRole(){
        return role;
    }
}
