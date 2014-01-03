/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.filters;

import dto.domain.Category;
import dto.domain.SubCategory;
import dto.domain.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class InterestFilter extends AbstractFilter {

    public String name = "";
    
    public List<Category> categories = new ArrayList<Category>();
    public List<SubCategory> subCategories = new ArrayList<SubCategory>();
    
    public boolean fetchUsers = false;
    public boolean fetchCategories = false;
    public boolean fetchSubCategories = false;
    public boolean fetchGroups = false;
}
