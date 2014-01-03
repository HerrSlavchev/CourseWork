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
public class Interest extends PersistedDTO {

    public String name;
    public String description;
    //extrinsic
    public List<Category> categories = new ArrayList<Category>();
    public List<SubCategory> subCategories = new ArrayList<SubCategory>();
    public List<User> users = new ArrayList<User>();
    public List<Group> groups = new ArrayList<Group>();

    public Interest(int ID) {
        super(ID);
    }

    public Interest(int ID, Timestamp timeIns, Timestamp timeUpd) {
        super(ID, timeIns, timeUpd);
    }

    public Interest(int ID, Timestamp timeIns, Timestamp timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Group> getGroups() {
        return groups;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
