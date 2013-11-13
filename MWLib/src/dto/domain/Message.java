/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.util.Date;
import java.util.List;

/**
 *
 * @author root
 */
public class Message extends PersistedDTO {

    public String text;

    //extrinsic
    public Conversation conversation;
    
    public Message(int ID, Date timeIns, Date timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }

    public String getText() {
        return text;
    }

    public Conversation getConversation() {
        return conversation;
    }
}
