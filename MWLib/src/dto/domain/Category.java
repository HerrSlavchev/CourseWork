/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

import java.util.List;

/**
 *
 * @author root
 */
public class Category extends PersistedDTO {

    public String name;
    public String description;

    //extrinsic
    public int subCategoryCount;
    public List<SubCategory> subCategories;
    public int interestCount;
    public List<Interest> interests;

    public Category(int ID) {
        super(ID);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
    
    @Override
    public String toString(){
        return name;
    }
}
