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
public enum NotificationType{
    
    NEW_GROUP("New group created"), //and it regards smth you are interested in
    NEW_PUBLICATION("New publication"), //in a group you participate in
    NEW_EVENT("New event"); //in a group you participate in or a city near you
    
    private String message;

    NotificationType(String message){
        this.message = message;
    }
}
