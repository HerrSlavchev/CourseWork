/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class Region extends PersistedDTO {

    public String name;
    //extrinsic
    public int townCount;
    public List<Town> towns = new ArrayList<Town>();
    public int userCount;
    public List<User> users = new ArrayList<User>();
    public int eventCount;
    public List<Event> events = new ArrayList<Event>();

    public Region(int ID) {
        super(ID);
    }

    public String getName() {
        return name;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public int getTownCount() {
        return townCount;
    }

    public List<User> getUsers() {
        return users;
    }

    public int getUserCount() {
        return userCount;
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getEventCount() {
        return eventCount;
    }
}
