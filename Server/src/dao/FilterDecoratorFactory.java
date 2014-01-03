/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.domain.Event;
import dto.domain.Interest;
import dto.domain.SubCategory;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.AbstractFilter;
import dto.filters.CategoryFilter;
import dto.filters.RegionFilter;
import dto.filters.TownFilter;
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
        } else if(af instanceof TownFilter){
            final TownFilter tf = (TownFilter) af;
            fd = new FilterDecorator() {

                @Override
                public String getName() {
                    return tf.name;
                }

                @Override
                public List<User> getUsers() {
                    return tf.users;
                }

                @Override
                public List<Event> getEvents() {
                    return tf.events;
                }

                @Override
                public boolean fetchEvents() {
                    return tf.fetchEvents;
                }

                @Override
                public boolean fetchUsers() {
                    return tf.fetchUsers;
                }
            };
        } else if (af instanceof CategoryFilter){
            final CategoryFilter cf = (CategoryFilter) af;
            fd = new FilterDecorator() {

                @Override
                public String getName() {
                    return cf.name;
                }

                @Override
                public List<SubCategory> getSubCategories() {
                    return cf.subCategories;
                }

                @Override
                public List<Interest> getInterests() {
                    return cf.interests;
                }

                @Override
                public boolean fetchSubCategories() {
                    return cf.fetchSubCategories;
                }

                @Override
                public boolean fetchInterests() {
                    return cf.fetchInterests;
                }
            };
        }
        fd.ids = af.ids;

        return fd;
    }

}
