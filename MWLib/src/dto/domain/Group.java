/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.util.Date;
import java.util.List;

/**
 *
 * @author root
 */
public class Group extends PersistedDTO{

    public String name;
    public String description;
    //extrinsic
    public Interest interest;
    public List<Publication> publications;
    public List<User> users;
    
    public Group(int ID, Date timeIns, Date timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }
    
    public String getName(){
        return name;
    }
    
    public String getDescription(){
        return description;
    }
    
    public Interest getInterest(){
        return interest;
    }
    
    public List<Publication> getPublications(){
        return publications;
    }
    
    public List<User> getUsers() {
        return users;
    }
}
