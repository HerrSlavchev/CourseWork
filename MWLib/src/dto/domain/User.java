/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.util.Date;

/**
 *
 * @author root
 */
public class User extends PersistedDTO{

    public User(int ID, Date timeIns, Date timeUpd, User userUpd) {
        super(ID, timeIns, timeUpd, null, userUpd);
    }
    
    
}
