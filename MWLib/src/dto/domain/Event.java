/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author root
 */
public class Event extends Publication {

    private String name;
    private Date dateFrom;
    private Date dateTo;
    //extrinsic
    private List<User> users = new ArrayList<User>();
    private List<Town> towns = new ArrayList<Town>();

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
    
    @Override
    public String toString(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
