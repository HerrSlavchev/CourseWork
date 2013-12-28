/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.filters;

import dto.domain.Conversation;
import dto.domain.Event;
import dto.domain.Group;
import dto.domain.Interest;
import dto.domain.Town;
import dto.domain.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class UserFilter extends AbstractFilter {

    //lists
    public List<Town> towns = new ArrayList<Town>();
    public List<Group> groups = new ArrayList<Group>();
    public List<Interest> interests = new ArrayList<Interest>();
    public List<Event> events = new ArrayList<Event>();
    public List<Conversation> conversations = new ArrayList<Conversation>();

    //fetchers
    public boolean fetchTowns = false;
    public boolean fetchGroups = false;
    public boolean fetchInterests = false;
    public boolean fetchEvents = false;
    public boolean fetchConversations = false;
    
    //special
    public String fName = "";
    public String sName = "";
    public String lName = "";
    public boolean fetchFullPersonalData = false;

    public List<Town> getTowns() {
        return towns;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public boolean fetchTowns() {
        return fetchTowns;
    }

    public boolean fetchGroups() {
        return fetchGroups;
    }

    public boolean fetchInterest() {
        return fetchInterests;
    }

    public boolean fetchEvents() {
        return fetchEvents;
    }

    public boolean fetchConversations() {
        return fetchConversations;
    }
}
