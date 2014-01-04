/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Experimental : use to omit SQL-redundancies
 * @author root
 */
public class ChildrenManager<E extends PersistedDTO> {
    
    private final List<E> oldChildren = new ArrayList<>();
    private final List<E> newChildren = new ArrayList<>();
    private final List<E> removedChildren = new ArrayList<>();
    
    public ChildrenManager(Collection<E> oldChildren){
        this.oldChildren.addAll(oldChildren);
    }
    
    public ChildrenManager(){
        
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
            newChildren.add(old);
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
            newChildren.add(old);
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
    
    /**
     * 
     * @return UnmodifiableList with all removed children.
     */
    public List<E> getRemovedChildren(){
        return Collections.unmodifiableList(removedChildren);
    }
    
    
}
