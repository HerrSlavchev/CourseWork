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
public class Notification extends PersistedDTO{
    
    private NotificationType type;
    private PersistedDTO ref; //what exactly happened
    
    public boolean isImportant;
    public boolean isRead;
    
    public Notification(int ID, NotificationType type, PersistedDTO ref){
        super(ID);
        this.type = type;
        this.ref = ref;
    }
    
    public NotificationType getType(){
        return type;
    }
    
    public PersistedDTO getRef(){
        return ref;
    }
    
}
