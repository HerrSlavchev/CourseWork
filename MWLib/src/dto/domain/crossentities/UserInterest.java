/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain.crossentities;

import dto.domain.Interest;
import dto.domain.User;
import java.io.Serializable;

/**
 *
 * @author root
 */
public class UserInterest implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public User parent;
    public Interest intr;
    public int notify;
    
}
