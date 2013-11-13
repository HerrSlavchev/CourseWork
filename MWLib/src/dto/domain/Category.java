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
    public List<SubCategory> subCategories;

    public Category(int ID) {
        super(ID);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }
}
