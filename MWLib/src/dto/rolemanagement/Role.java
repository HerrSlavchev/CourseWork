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

    ADMIN("Administrator"),
    MODERATOR("Moderator"),
    USER("User"),
    GUEST("Guest");
    
    private String name;

    Role(String name) {
        this.name = name;
    }

}
