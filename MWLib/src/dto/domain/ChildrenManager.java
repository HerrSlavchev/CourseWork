/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Experimental : use to omit SQL-redundancies
 * @author root
 */
public class ChildrenManager<E extends PersistedDTO>  implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private final List<E> oldChildren = new ArrayList<>();
    private final List<E> newChildren = new ArrayList<>();
    private final List<E> removedChildren = new ArrayList<>();
    
    public ChildrenManager(Collection<E> oldChildren){
        this.oldChildren.addAll(oldChildren);
    }
    
    public ChildrenManager(){
    }
    
    public void addOldChild(E child){
        oldChildren.add(child);
    }
    /**
     * Checks if the entity was originally among old children.
     * If child was removed from oldChildren, returns it there from removedChildren.
     * Otherwise only adds it to the newChildren.
     * @param child 
     */
    public void addChild(E child){
        
        int id = child.getID();
        
        for(E cmp : oldChildren){
            if(cmp.getID() == id){
                return;
            }
        }
        
        E old = null;
        for(E cmp : removedChildren){
            if(cmp.getID() == id){
                old = cmp;
                break;
            }
        }
        
        if(old != null){
            removedChildren.remove(old);
            oldChildren.add(old);
        } else {
            newChildren.add(child);
        }
    }
    
    /**
     * Checks if the entity was originally among old children.
     * If so, removes it and adds it to removedChildren.
     * Otherwise only tries to remove it from newChildren.
     * @param child 
     */
    public void removeChild(E child){
        int id = child.getID();
        
        E old = null;
        for(E cmp : oldChildren){
            if(cmp.getID() == id){
                old = cmp;
                break;
            }
        }
        
        if (old != null){
            oldChildren.remove(old);
            removedChildren.add(old);
        } else {
            for (E cmp : newChildren){
                if (cmp.getID() == child.getID()){
                    newChildren.remove(cmp);
                    break;
                }
            }
        }
    }
    
    /**
     * 
     * @return A list with all old and new children
     */
    public List<E> getCurrentChildren(){
        ArrayList arr = new ArrayList(oldChildren);
        arr.addAll(newChildren);
        return arr;
    }
   
    public List<E> getOldChildren(){
        return new ArrayList(oldChildren);
    } 
    
    public List<E> getModifiedChildren(){
        List<E> modified = new ArrayList<>();
        for(E item : oldChildren){
            if(item.isModified()){
                modified.add(item);
            }
        }
        return modified;
    }
    
    public List<E> getNewChildren(){
        return new ArrayList(newChildren);
    } 
    
    public List<E> getRemovedChildren(){
        return new ArrayList(removedChildren);
    }
    
    /**
     * Filters a list and returns all items from it that do not belong to the current set of the manager.
     * @param items
     * @return 
     */
    public List<E> filterList(List<E> items){
        List<E> filtered = new ArrayList<>();
        List<E> current = getCurrentChildren();
        for (E item : items){
            boolean matched = false;
            for(E curr : current){
                if(item.getID() == curr.getID()){
                    matched = true;
                    break;
                }
            }
            if (false == matched){
                filtered.add(item);
            }
        }
        return filtered;
    }
}
