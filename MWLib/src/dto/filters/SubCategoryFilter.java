/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.filters;

import dto.domain.Category;
import dto.domain.Interest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class SubCategoryFilter extends AbstractFilter{
    
    public String name = "";
    
    public List<Category> categories = new ArrayList<Category>();
    public List<Interest> interests = new ArrayList<Interest>();
    
    public boolean fetchInterests = false;
    
}
