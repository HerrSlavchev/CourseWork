/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import dto.domain.crossentities.UserInterest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class Interest extends PersistedDTO {

    public String name = "";
    public String description = "";
    //extrinsic
    
    public ChildrenManager<SubCategory> subCategories = new ChildrenManager<SubCategory>();
    
    public List<Category> categories = new ArrayList<>();
    public ChildrenManager<User> users = new ChildrenManager<>();
    public List<Group> groups = new ArrayList<>();

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

    public ChildrenManager<SubCategory> getSubCategories(){
        return subCategories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public ChildrenManager<User> getUsers() {
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
