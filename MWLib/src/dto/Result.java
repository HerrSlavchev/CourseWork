/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import dto.domain.PersistedDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class Result<E extends PersistedDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<E> result;
    private List<Integer> autoIDs;
    private Throwable exception;

    public Result(List<E> res, Throwable exception) {
        this(res, exception, new ArrayList<Integer>());
    }

    public Result(List<E> res, Throwable exception, List<Integer> autoIDs) {
        this.result = res;
        this.exception = exception;
        this.autoIDs = autoIDs;
    }
    
    public List<E> getResult() {
        return result;
    }

    public List<Integer> getAutoIDs() {
        return autoIDs;
    }

    public Throwable getException() {
        return exception;
    }
}
