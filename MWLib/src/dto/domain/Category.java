/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.domain;

/**
 *
 * @author root
 */
public class Category extends PersistedDTO {

    public String name;

    public Category(int ID) {
        super(ID);
    }

}
