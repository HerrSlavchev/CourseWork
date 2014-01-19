/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import dto.rolemanagement.Role;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class User extends PersistedDTO {

    public String fName;
    public String sName;
    public String lName;
    public String eMail;
    public String password;
    public String description;
    public Role role;
    //extrinsic
    public ChildrenManager<Town> towns = new ChildrenManager<Town>();    
    public List<Interest> interests = new ArrayList<Interest>();
    public List<Group> groups = new ArrayList<Group>();
    public List<Event> events = new ArrayList<Event>();
    public List<Conversation> conversations = new ArrayList<Conversation>();

    public boolean notify = false;
    
    public User(int ID){
        super(ID);
    }
    
    public User(int ID, Timestamp timeIns, Timestamp timeUpd) {
        super(ID, timeIns, timeUpd);
    }
    
    public User(int ID, Timestamp timeIns, Timestamp timeUpd, User userUpd) {
        super(ID, timeIns, timeUpd, null, userUpd);
    }

    public String getFName() {
        return fName;
    }

    public String getSName() {
        return sName;
    }

    public String getLName() {
        return lName;
    }

    public String getEMail() {
        return eMail;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public Role getRole() {
        return role;
    }

    public ChildrenManager<Town> getTowns() {
        return towns;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }
    
    @Override
    public String toString(){
        return lName + ", " + sName;
    }
    
    public boolean getNotify(){
        return notify;
    }
}
