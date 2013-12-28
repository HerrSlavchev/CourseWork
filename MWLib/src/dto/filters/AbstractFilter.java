/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.filters;

import dto.domain.Category;
import dto.domain.Conversation;
import dto.domain.Event;
import dto.domain.Group;
import dto.domain.Interest;
import dto.domain.Message;
import dto.domain.Publication;
import dto.domain.Region;
import dto.domain.SubCategory;
import dto.domain.Town;
import dto.domain.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ALWAYS OVERRIDE APPROPRIATE METHODS IN CONCRETE CLASSES!
 *
 * @author root
 */
public abstract class AbstractFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Integer> ids = new ArrayList<Integer>();

    public String getName() {
        return "";
    }

    public List<Category> getCategories() {
        return null;
    }

    public List<Conversation> getConversations() {
        return null;
    }

    public List<Event> getEvents() {
        return null;
    }

    public List<Group> getGroups() {
        return null;
    }

    public List<Interest> getInterests() {
        return null;
    }

    public List<Message> getMessages() {
        return null;
    }

    public List<Publication> getPublications() {
        return null;
    }

    public List<Region> getRegions() {
        return null;
    }

    public List<SubCategory> getSubCategories() {
        return null;
    }

    public List<Town> getTowns() {
        return null;
    }

    public List<User> getUsers() {
        return null;
    }

    public boolean fetchCategories() {
        return false;
    }

    public boolean fetchConversations() {
        return false;
    }

    public boolean fetchEvents() {
        return false;
    }

    public boolean fetchGroups() {
        return false;
    }

    public boolean fetchInterests() {
        return false;
    }

    public boolean fetchMessages() {
        return false;
    }

    public boolean fetchPublications() {
        return false;
    }

    public boolean fetchRegions() {
        return false;
    }

    public boolean fetchSubCategories() {
        return false;
    }

    public boolean fetchTowns() {
        return false;
    }

    public boolean fetchUsers() {
        return false;
    }
}
