/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.util.List;

/**
 *
 * @author root
 */
public class Town extends PersistedDTO {

    public String name;
    
    //extrinsic
    public Region region;
    public int userCount;
    public List<User> users;
    public int eventCount;
    public List<Event> events;
    
    
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
        return userCount;
    }
    
    public int getEventCount() {
        return eventCount;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
