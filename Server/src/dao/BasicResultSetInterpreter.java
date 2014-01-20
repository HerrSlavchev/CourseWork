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
import dto.rolemanagement.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Experimental : Used to interpret <code>ResultSet</code> and build the most
 * basic DTOs from it.
 *
 * @author root
 */
public class BasicResultSetInterpreter implements ResultSetInterpreterIF{

    public Category getCategory(ResultSet rs) throws SQLException {
        Category cat = null;
        String tbl = FilterUtils.CATEGORY;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0){
            return null;
        }
        cat = new Category(id);
        String name = rs.getString(tbl + "_name");
        String description = safeStr(rs, tbl + "_description");
        cat.setName(name);
        cat.setDescription(description);
        return cat;
    }

    public Conversation getConversation(ResultSet rs) throws SQLException {
        Conversation con = null;
        String tbl = FilterUtils.CONVERSATION;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        con = new Conversation(id, timeins, timeupd);
        String topic = rs.getString(tbl + "_topic");
        con.setTopic(topic);
        return con;
    }

    public Event getEvent(ResultSet rs) throws SQLException {
        Event e = null;
        String tbl = FilterUtils.EVENT;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        e = new Event(id, timeins, timeupd);
        e.setName(rs.getString(tbl + "_name"));
        e.setDateFrom(rs.getDate(tbl + "_datefrom"));
        e.setDateTo(rs.getDate(tbl + "_dateto"));
        e.setText(safeStr(rs, tbl + "_text"));

        return e;
    }

    public Group getGroup(ResultSet rs) throws SQLException {
        Group ig = null;
        String tbl = FilterUtils.GROUP;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        ig = new Group(id, timeins, timeupd);
        ig.setDescription(safeStr(rs, tbl + "_description"));
        ig.setName(rs.getString(tbl + "_name"));
        return ig;
    }

    public Interest getInterest(ResultSet rs) throws SQLException {
        Interest i = null;
        String tbl = FilterUtils.INTEREST;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        i = new Interest(id, timeins, timeupd);
        i.setDescription(safeStr(rs, tbl + "_description"));
        i.setName(rs.getString(tbl + "_name"));
        return i;
    }

    public Message getMessage(ResultSet rs) throws SQLException {
        Message m = null;
        String tbl = FilterUtils.MESSAGE;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        m = new Message(id, timeins, timeupd);
        m.setText(safeStr(rs, tbl + "_text"));
        return m;
    }

    public Publication getPublication(ResultSet rs) throws SQLException {
        Publication p = null;
        String tbl = FilterUtils.PUBLICATION;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        p = new Publication(id, timeins, timeupd);
        p.setText(safeStr(rs, tbl + "_text"));

        return p;
    }

    public Region getRegion(ResultSet rs) throws SQLException {
        Region r = null;
        String tbl = FilterUtils.REGION;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        r = new Region(id);
        r.setName(rs.getString(tbl + "_name"));

        return r;
    }

    public SubCategory getSubCategory(ResultSet rs) throws SQLException {
        SubCategory s = null;
        String tbl = FilterUtils.SUBCATEGORY;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        s = new SubCategory(id);
        s.setDescription(safeStr(rs, tbl + "_description"));
        s.setName(rs.getString(tbl + "_name"));

        return s;
    }

    public Town getTown(ResultSet rs) throws SQLException {
        Town t = null;
        String tbl = FilterUtils.TOWN;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        t = new Town(id);
        t.setName(rs.getString(tbl + "_name"));

        return t;
    }

    public User getUser(ResultSet rs) throws SQLException {
        User u = null;
        String tbl = FilterUtils.USER;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        u = new User(id, timeins, timeupd);
        
        u.setFirstName(rs.getString(tbl + "_f_name"));
        u.setLastName(rs.getString(tbl + "_l_name"));
        
        return u;
    }
    
    public User getUserIns(ResultSet rs) throws SQLException {
        User u = null;
        String tbl = "userins";
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        
        u = new User(id);
        u.setFirstName(safeStr(rs, tbl + "_f_name"));
        u.setLastName(safeStr(rs, tbl + "_l_name"));
        
        return u;
    }

    public User getUserUpd(ResultSet rs) throws SQLException {
        User u = null;
        String tbl = "userupd";
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        
        u = new User(id);
        u.setFirstName(safeStr(rs, tbl + "_f_name"));
        u.setLastName(safeStr(rs, tbl + "_l_name"));
        
        return u;
    }
    
    public boolean checkColumn(ResultSet rs, String label) {
        int idx = -1;
        try {
            idx = rs.findColumn(label);
        } catch (SQLException SQLe) {
        }
        return idx > -1;
    }

    public static Integer safeInt(ResultSet rs, String label) {
        Integer i = null;
        try {
            i = rs.getInt(label);
        } catch (SQLException SQLe) {
        }
        return i;
    }

    public static String safeStr(ResultSet rs, String label) {
        String str = "";
        try {
            str = rs.getString(label);
        } catch (SQLException SQLe) {
        }

        return str;
    }

    public static Timestamp safeTimestamp(ResultSet rs, String label) {
        Timestamp ts = null;
        try {
            ts = rs.getTimestamp(label);
        } catch (SQLException SQLe) {
        }
        return ts;
    }

    public static Role safeRole(ResultSet rs, String label) {
        Role r = Role.USER;
        try {
            int roleIdx = rs.getInt(label);
            r = Role.values()[roleIdx];
        } catch (SQLException SQLe) {
        }
        return r;
    }
    
    
}
