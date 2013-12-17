/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.rolemanagement;

/**
 *
 * @author root
 */
public enum Role {

    USER("User"),
    MODERATOR("Moderator"),
    ADMIN("Administrator"),
    ;
    
    
    
    private String name;
    private static final long serialVersionUID = 1L;    


    Role(String name) {
        this.name = name;
    }

}
