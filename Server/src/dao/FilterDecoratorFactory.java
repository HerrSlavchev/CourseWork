/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.domain.Event;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.AbstractFilter;
import dto.filters.CategoryFilter;
import dto.filters.RegionFilter;
import java.util.List;

/**
 *
 * @author root
 */
public class FilterDecoratorFactory {

    public static FilterDecorator getInstance(AbstractFilter af) {

        FilterDecorator fd = null;
        if (af instanceof RegionFilter) {
            final RegionFilter rf = (RegionFilter) af;
            fd = new FilterDecorator() {

                @Override
                public String getName() {
                    return rf.name;
                }

                @Override
                public List<Town> getTowns() {
                    return rf.towns;
                }

                @Override
                public List<User> getUsers() {
                    return rf.users;
                }

                @Override
                public List<Event> getEvents() {
                    return rf.events;
                }

                @Override
                public boolean fetchEvents() {
                    return rf.fetchEvents;
                }

                @Override
                public boolean fetchTowns() {
                    return rf.fetchTowns;
                }
                
                @Override
                public boolean fetchUsers() {
                    return rf.fetchUsers;
                }
            };
        }
        fd.ids = af.ids;

        return fd;
    }

}
