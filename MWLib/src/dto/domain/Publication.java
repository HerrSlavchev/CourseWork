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
public class Publication extends PersistedDTO{

    public String text;
    List<Publication> childer;
    
    public Publication(int ID, long timeIns, long timeUpd, int userIns, int userUpd) {
        super(ID, timeIns, timeUpd, userIns, userUpd);
    }
    
    
}
