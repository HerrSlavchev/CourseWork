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
public class Publication extends PersistedDTO {

    public String text;

    //extrinsic
    public List<Publication> children;
    public Group group;
    public Publication parentPublication;
    public Event event;

    public Publication(int ID) {
        super(ID);
    }

    public Publication(int ID, Timestamp timeIns, Timestamp timeUpd) {
        super(ID, timeIns, timeUpd);
    }

    public Publication(int ID, Timestamp timeIns, Timestamp timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }

    public String getText() {
        return text;
    }

    public List<Publication> getChilder() {
        return children;
    }

    public Group getGroup() {
        return group;
    }

    public Publication getParentPublication() {
        return parentPublication;
    }

    public Event getEvent() {
        return event;
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
}
