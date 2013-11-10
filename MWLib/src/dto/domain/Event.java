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
public class Event extends Publication{

    public Event(int ID, long timeIns, long timeUpd, int userIns, int userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }
    
}
