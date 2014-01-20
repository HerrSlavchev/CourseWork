/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class Group extends PersistedDTO {

    private String name;
    private String description;
    //extrinsic
    private Interest interest;
    private List<Publication> publications = new ArrayList<Publication>();
    private List<User> users = new ArrayList<User>();

    public Group(int ID) {
        super(ID);
    }
    
    public Group(int ID, Timestamp timeIns, Timestamp timeUpd) {
        super(ID, timeIns, timeUpd);
    }

    public Group(int ID, Timestamp timeIns, Timestamp timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Interest getInterest() {
        return interest;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public List<User> getUsers() {
        return users;
    }
    
    @Override
    public String toString(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }
}
