/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.domain.Category;
import dto.domain.Event;
import dto.domain.Interest;
import dto.domain.SubCategory;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.AbstractFilter;
import dto.filters.CategoryFilter;
import dto.filters.InterestFilter;
import dto.filters.RegionFilter;
import dto.filters.SubCategoryFilter;
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
        } else if (af instanceof TownFilter) {
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
        } else if (af instanceof CategoryFilter) {
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
        } else if (af instanceof SubCategoryFilter) {
            final SubCategoryFilter cf = (SubCategoryFilter) af;
            fd = new FilterDecorator() {

                @Override
                public String getName() {
                    return cf.name;
                }

                @Override
                public List<Category> getCategories() {
                    return cf.categories;
                }

                @Override
                public List<Interest> getInterests() {
                    return cf.interests;
                }

                @Override
                public boolean fetchInterests() {
                    return cf.fetchInterests;
                }
            };
        } else if (af instanceof InterestFilter) {
            final InterestFilter inf = (InterestFilter) af;
            fd = new FilterDecorator() {
                
                @Override
                public List<User> getUsers(){
                    return inf.users;
                }
                
                @Override
                public String getName() {
                    return inf.name;
                }

                @Override
                public List<Category> getCategories() {
                    return inf.categories;
                }
                
                @Override
                public List<SubCategory> getSubCategories() {
                    return inf.subCategories;
                }
                
                @Override
                public boolean fetchSubCategories() {
                    return inf.fetchSubCategories;
                }

                @Override
                public boolean fetchCategories() {
                    return inf.fetchCategories;
                }
                
                @Override
                public boolean fetchUsers() {
                    return inf.fetchUsers;
                }
                
                @Override
                public boolean fetchGroups() {
                    return inf.fetchGroups;
                }
            };
        }
        fd.ids = af.ids;

        return fd;
    }

}
