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

    private TriggerType triggerType;
    private ReasonType reasonType;
    private boolean important;
    private boolean read;

    //extrinsic
    private PersistedDTO reason; //why report
    private PersistedDTO trigger; //what exactly happened

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
        return important;
    }

    public boolean isRead() {
        return read;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public void setReasonType(ReasonType reasonType) {
        this.reasonType = reasonType;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setReason(PersistedDTO reason) {
        this.reason = reason;
    }

    public void setTrigger(PersistedDTO trigger) {
        this.trigger = trigger;
    }
}
