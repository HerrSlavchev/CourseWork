/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author root
 */
public abstract class AbstractFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Integer> ids = new ArrayList<Integer>();
    
    /**
     * Use for more datailed fetch (counts, parents, children, etc).
     */
    public boolean deepFetch = false;
}
