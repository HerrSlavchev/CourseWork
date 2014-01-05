/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import dto.domain.PersistedDTO;
import java.util.List;

/**
 *
 * @author root
 */
public interface  ChoiceDelegatorIF <E extends PersistedDTO>{
    
    public void processSelected(List<E> selected);
}
