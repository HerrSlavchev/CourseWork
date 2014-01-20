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
public class Town extends PersistedDTO {

    private String name;
    
    //extrinsic
    private Region region;
    private int userCount;
    public List<User> users = new ArrayList<>();
    private int eventCount;
    public List<Event> events = new ArrayList<>();
    
    
    public Town(int ID) {
        super(ID);
    }
    
    public String getName(){
        return name;
    }

    public Region getRegion() {
        return region;
    }

    public List<User> getUsers() {
        return users;
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public int getUserCount() {
        return userCount > users.size() ? userCount : users.size();
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
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }
}
