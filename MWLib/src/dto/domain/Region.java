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

    private String name;
    private String shortName;
    //extrinsic
    private int townCount;
    private List<Town> towns = new ArrayList<>();
    private int userCount;
    private List<User> users = new ArrayList<>();
    private int eventCount;
    private List<Event> events = new ArrayList<>();

    public Region(int ID) {
        super(ID);
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
    
    public List<Town> getTowns() {
        return towns;
    }

    public int getTownCount() {
        return townCount > towns.size() ? townCount : towns.size();
    }

    public List<User> getUsers() {
        return users;
    }

    public int getUserCount() {
        return userCount > users.size() ? userCount : users.size();
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getEventCount() {
        return eventCount > events.size() ? eventCount : events.size();
    }
    
    @Override
    public String toString(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
        shortName = name;
        if(name != null && name.length() > 11){
            shortName = name.substring(0, 9) + "...";
        }
    }

    public void setTownCount(int townCount) {
        this.townCount = townCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

}
