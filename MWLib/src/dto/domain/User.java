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


    private String firstName;
    private String surName;
    private String lastName;
    private String e_Mail;
    private String password;
    private String description;
    private Role role;
    //extrinsic
    private ChildrenManager<Town> towns = new ChildrenManager<>();    
    private List<Interest> interests = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Conversation> conversations = new ArrayList<>();

    private boolean notify = false;
    
    public User(int ID){
        super(ID);
    }
    
    public User(int ID, Timestamp timeIns, Timestamp timeUpd) {
        super(ID, timeIns, timeUpd);
    }
    
    public User(int ID, Timestamp timeIns, Timestamp timeUpd, User userUpd) {
        super(ID, timeIns, timeUpd, null, userUpd);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getE_Mail() {
        return e_Mail;
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

    public boolean isNotify() {
        return notify;
    }

    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setE_Mail(String e_Mail) {
        this.e_Mail = e_Mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setTowns(ChildrenManager<Town> towns) {
        this.towns = towns;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
