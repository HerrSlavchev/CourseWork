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
public class Conversation extends PersistedDTO{

    private String topic;

    //extrinsic
    private List<User> users = new ArrayList<User>();
    private List<Message> messages = new ArrayList<Message>();
    
    public Conversation(int ID) {
        super(ID);
    }
    
    public Conversation(int ID, Timestamp timeIns, Timestamp timeUpd){
        super(ID, timeIns, timeUpd);
    }
    
    public Conversation(int ID, Timestamp timeIns, Timestamp timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public String getTopic(){
        return topic;
    }
    
    public List<User> getUsers(){
        return users;
    }
    
    public List<Message> getMessages(){
        return messages;
    }
    
    @Override
    public String toString(){
        return topic;
    }
}
