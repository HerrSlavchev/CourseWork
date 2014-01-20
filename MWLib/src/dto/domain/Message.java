/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.sql.Timestamp;

/**
 *
 * @author root
 */
public class Message extends PersistedDTO {

    private String text;

    //extrinsic
    private Conversation conversation;

    public Message(int ID) {
        super(ID);
    }

    public Message(int ID, Timestamp timeIns, Timestamp timeUpd) {
        super(ID, timeIns, timeUpd);
    }

    public Message(int ID, Timestamp timeIns, Timestamp timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }

    public String getText() {
        return text;
    }

    public Conversation getConversation() {
        return conversation;
    }
    
    @Override
    public String toString(){
        int len = text.length();
        String txt = text;
        if (len > 20){
            txt = text.substring(0, 17) + "...";
        }
        return txt;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
