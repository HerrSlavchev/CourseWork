/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class SubCategory extends PersistedDTO{

    private String name;
    private String description;
    
    //extrinsic
    private Category category;
    private int interestCount;
    public List<Interest> interests = new ArrayList<Interest>();
    
    public SubCategory(int ID) {
        super(ID);
    }
    
    public String getName(){
        return name;
    }
    
    public String getDescription(){
        return description;
    }
    
    public Category getCategory(){
        return category;
    }
    
    public int getInterestCount() {
        return interestCount > interests.size() ? interestCount : interests.size();
    }
    
    public List<Interest> getInterests(){
        return interests;
    }
    
    @Override
    public String toString(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setInterestCount(int interestCount) {
        this.interestCount = interestCount;
    }
}
