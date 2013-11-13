/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

/**
 *
 * @author root
 */
public enum TriggerType{
    
    NEW_GROUP("New group created"), //and it regards smth you are interested in
    NEW_PUBLICATION("New publication"), //in a group you participate in
    NEW_EVENT("New event"),
    LOGGED_FROM_ANOTHER_CLIENT("Another session with this user has been initialized"); //in a group you participate in or a city near you
    
    private String message;

    TriggerType(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return message;
    }
}
