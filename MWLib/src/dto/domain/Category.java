/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class Category extends PersistedDTO {

    private String name;
    private String description;
    
    private String shortDescription;
    //extrinsic
    private int subCategoryCount;
    private List<SubCategory> subCategories = new ArrayList<SubCategory>();
    private int interestCount;
    private List<Interest> interests = new ArrayList<Interest>();

    public Category(int ID) {
        super(ID);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public String getShortDescription(){
        return shortDescription;
    }

    public int getSubCategoryCount() {
        return subCategoryCount;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public int getInterestCount() {
        return interestCount;
    }

    public List<Interest> getInterests() {
        return interests;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        
        this.description = description;
        this.shortDescription = description;
        if(description != null && description.length() > 9){
            shortDescription = description.substring(0, 7) + "...";
        }
    }

    public void setSubCategoryCount(int subCategoryCount) {
        this.subCategoryCount = subCategoryCount;
    }

    public void setInterestCount(int interestCount) {
        this.interestCount = interestCount;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
