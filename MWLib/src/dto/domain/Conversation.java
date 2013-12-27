/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author root
 */
public class Conversation extends PersistedDTO{

    public String topic;
    
    //extrinsic
    public List<User> users;
    public List<Message> messages;
    
    public Conversation(int ID) {
        super(ID);
    }
    
    public Conversation(int ID, Timestamp timeIns, Timestamp timeUpd){
        super(ID, timeIns, timeUpd);
    }
    
    public Conversation(int ID, Timestamp timeIns, Timestamp timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
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
    
}
