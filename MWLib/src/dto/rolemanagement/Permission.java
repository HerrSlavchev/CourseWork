/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.rolemanagement;

/**
 *
 * @author root
 */
public enum Permission {

    //--------
    USER_MODIFY_ACC("Modify user account"),
    USER_BAN_ACCOUNT("Ban user"),
    USER_BAN_FROM_GROUP("Ban user from group"),
    USER_DELETE("Delete user"),
    USER_DELETE_FROM_GROUP("Delete user from group"),
    //--------
    CATEGORY_CREATE("Create category"),
    CATEGORY_MODIFY("Modify category"),
    CATEGORY_HIDE("Hide category"),
    CATEGORY_DELETE("Delete category"),
    //--------
    SUBCATEGORY_CREATE("Create subcategory"),
    SUBCATEGORY_MODIFY("Modify subcategory"),
    SUBCATEGORY_HIDE("Hide subcategory"),
    SUBCATEGORY_DELETE("Delete subcategory"),
    //--------
    OBLAST_CREATE("Create municipality"),
    OBLAST_MODIFY("Modify municipality"),
    OBLAST_HIDE("Hide municipality"),
    OBLAST_DELETE("Delete municipality"),
    //--------
    TOWN_CREATE("Create town"),
    TOWN_MODIFY("Modify town"),
    TOWN_HIDE("Hide town"),
    TOWN_DELETE("Delete town"), 
    //--------
    INTEREST_CREATE("Create interest"),
    INTEREST_MODIFY("Modify interest"),
    INTEREST_HIDE("Hide interest"),
    INTEREST_DELETE("Delete interest"), 
    //--------
    GROUP_CREATE("Create group"),
    GROUP_MODIFY("Modify group"),
    GROUP_HIDE("Hide group"),
    GROUP_DELETE("Delete group"), 
    //--------
    POST_CREATE("Create post"),
    POST_MODIFY("Modify post"),
    POST_HIDE("Hide post"),
    POST_DELETE("Delete post"), 
    //--------
    EVENT_CREATE("Create event"),
    EVENT_MODIFY("Modify event"),
    EVENT_HIDE("Hide event"),
    EVENT_DELETE("Delete event"), 
    ;
    private String name;
    private static final long serialVersionUID = 1L;
    
    Permission(String name) {
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
