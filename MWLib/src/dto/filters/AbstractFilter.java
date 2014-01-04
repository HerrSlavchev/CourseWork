/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.filters;

import dto.domain.Category;
import dto.domain.Conversation;
import dto.domain.Event;
import dto.domain.Group;
import dto.domain.Interest;
import dto.domain.Message;
import dto.domain.Publication;
import dto.domain.Region;
import dto.domain.SubCategory;
import dto.domain.Town;
import dto.domain.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author root
 */
public abstract class AbstractFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Integer> ids = new ArrayList<Integer>();
}
