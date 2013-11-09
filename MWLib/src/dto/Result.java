/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dto;

import dto.domain.PersistedDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author root
 */
public class Result<E extends PersistedDTO> implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private List<E> result;
    private Throwable exception;
    
    public Result(List<E> res, Throwable exception){
        this.result = res;
        this.exception = exception;
    }
    
    public List<E> getResult(){
        return result;
    }
    
    public Throwable getException(){
        return exception;
    }
}
