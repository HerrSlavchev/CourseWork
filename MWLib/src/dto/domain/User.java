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
public class User extends PersistedDTO{

    public User(int ID, long timeIns, long timeUpd, int userUpd) {
        super(ID, timeIns, timeUpd, 0, userUpd);
    }
    
    
}
