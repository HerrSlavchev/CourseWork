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
public class Notification extends PersistedDTO {

    public TriggerType triggerType;
    public ReasonType reasonType;
    public boolean isImportant;
    public boolean isRead;

    //extrinsic
    public PersistedDTO reason; //why report
    public PersistedDTO trigger; //what exactly happened

    public Notification(int ID) {
        super(ID);
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public PersistedDTO getTrigger() {
        return trigger;
    }

    public ReasonType getReasonType() {
        return reasonType;
    }

    public PersistedDTO getReason() {
        return reason;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public boolean isRead() {
        return isRead;
    }
}
