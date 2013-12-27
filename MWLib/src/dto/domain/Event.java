/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author root
 */
public class Event extends Publication {

    public String name;
    public Date dateFrom;
    public Date dateTo;
    //extrinsic
    public List<User> users;
    public List<Town> towns;

    public Event(int ID) {
        super(ID);
    }

    public Event(int ID, Timestamp timeIns, Timestamp timeUpd) {
        super(ID, timeIns, timeUpd);
    }

    public Event(int ID, Timestamp timeIns, Timestamp timeUpd, User userIns, User userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }

    public String getName() {
        return name;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Town> getTowns() {
        return towns;
    }
}
