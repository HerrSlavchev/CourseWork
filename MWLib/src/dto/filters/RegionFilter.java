/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.filters;

import dto.domain.Event;

import dto.domain.Town;
import dto.domain.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class RegionFilter extends AbstractFilter {

    public String name = "";

    public List<Town> towns = new ArrayList<Town>();
    public List<Event> events = new ArrayList<Event>();
    public List<User> users = new ArrayList<User>();

    public boolean fetchTowns = false;
    public boolean fetchEvents = false;
    public boolean fetchUsers = false;

    public String getName() {
        return name;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean fetchTowns() {
        return fetchTowns;
    }

    public boolean fetchEvents() {
        return fetchEvents;
    }
    
    public boolean fetchUsers() {
        return fetchUsers;
    }
}
