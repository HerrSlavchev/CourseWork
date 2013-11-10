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
public class Message extends PersistedDTO{

    public String text;
    
    public Message(int ID) {
        super(ID);
    }
    
}
