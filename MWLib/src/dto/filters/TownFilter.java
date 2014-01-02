/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.filters;

import dto.domain.Event;
import dto.domain.Region;
import dto.domain.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class TownFilter extends AbstractFilter{
    
    public String name = "";
    
    public List<Region> regions = new ArrayList<Region>();
    public List<Event> events = new ArrayList<Event>();
    public List<User> users = new ArrayList<User>();
    
    
    public boolean fetchEvents = false;
    public boolean fetchUsers = false;
}
