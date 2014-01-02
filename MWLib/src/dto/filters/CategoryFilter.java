/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.filters;

import dto.domain.Interest;
import dto.domain.SubCategory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class CategoryFilter extends AbstractFilter{
    
    public String name = "";
    
    public List<Interest> interests = new ArrayList<Interest>();
    public List<SubCategory> subCategories = new ArrayList<SubCategory>();
            
    public boolean fetchInterests = false;
    public boolean fetchSubCategories = false;
}
