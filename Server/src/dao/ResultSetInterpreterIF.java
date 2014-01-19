/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author root
 */
public interface ResultSetInterpreterIF {
    
    public Category getCategory(ResultSet rs) throws SQLException;
    public Conversation getConversation(ResultSet rs) throws SQLException;
    public Event getEvent(ResultSet rs) throws SQLException;
    public Group getGroup(ResultSet rs) throws SQLException;
    public Interest getInterest(ResultSet rs) throws SQLException;
    public Message getMessage(ResultSet rs) throws SQLException;
    public Publication getPublication(ResultSet rs) throws SQLException;
    public Region getRegion(ResultSet rs) throws SQLException;
    public SubCategory getSubCategory(ResultSet rs) throws SQLException;
    public Town getTown(ResultSet rs) throws SQLException;
    public User getUser(ResultSet rs) throws SQLException;
    public User getUserIns(ResultSet rs) throws SQLException;
    public User getUserUpd(ResultSet rs) throws SQLException;
}
