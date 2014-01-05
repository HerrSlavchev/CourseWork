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
public class ResultSetInterpreter {

    public static Category getCategory(ResultSet rs) throws SQLException {
        Category cat = null;
        String tbl = FilterUtils.CATEGORY;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0){
            return null;
        }
        cat = new Category(id);
        cat.name = rs.getString(tbl + "_name");
        cat.description = safeStr(rs, tbl + "_description");
        return cat;
    }

    public static Conversation getConversation(ResultSet rs) throws SQLException {
        Conversation con = null;
        String tbl = FilterUtils.CONVERSATION;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        con = new Conversation(id, timeins, timeupd);
        con.topic = rs.getString(tbl + "_topic");
        return con;
    }

    public static Event getEvent(ResultSet rs) throws SQLException {
        Event e = null;
        String tbl = FilterUtils.EVENT;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        e = new Event(id, timeins, timeupd);
        e.name = rs.getString(tbl + "_name");
        e.dateFrom = rs.getDate(tbl + "_datefrom");
        e.dateTo = rs.getDate(tbl + "_dateto");
        e.text = safeStr(rs, tbl + "_text");

        return e;
    }

    public static Group getGroup(ResultSet rs) throws SQLException {
        Group ig = null;
        String tbl = FilterUtils.GROUP;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        ig = new Group(id, timeins, timeupd);
        ig.description = safeStr(rs, tbl + "_description");
        ig.name = rs.getString(tbl + "_name");
        return ig;
    }

    public static Interest getInterest(ResultSet rs) throws SQLException {
        Interest i = null;
        String tbl = FilterUtils.INTEREST;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        i = new Interest(id, timeins, timeupd);
        i.description = safeStr(rs, tbl + "_description");
        i.name = rs.getString(tbl + "_name");
        return i;
    }

    public static Message getMessage(ResultSet rs) throws SQLException {
        Message m = null;
        String tbl = FilterUtils.MESSAGE;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        m = new Message(id, timeins, timeupd);
        m.text = safeStr(rs, tbl + "_text");
        return m;
    }

    public static Publication getPublication(ResultSet rs) throws SQLException {
        Publication p = null;
        String tbl = FilterUtils.PUBLICATION;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        p = new Publication(id, timeins, timeupd);
        p.text = safeStr(rs, tbl + "_text");

        return p;
    }

    public static Region getRegion(ResultSet rs) throws SQLException {
        Region r = null;
        String tbl = FilterUtils.REGION;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        r = new Region(id);
        r.name = rs.getString(tbl + "_name");

        return r;
    }

    public static SubCategory getSubCategory(ResultSet rs) throws SQLException {
        SubCategory s = null;
        String tbl = FilterUtils.SUBCATEGORY;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        s = new SubCategory(id);
        s.description = safeStr(rs, tbl + "_description");
        s.name = rs.getString(tbl + "_name");

        return s;
    }

    public static Town getTown(ResultSet rs) throws SQLException {
        Town t = null;
        String tbl = FilterUtils.TOWN;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        t = new Town(id);
        t.name = rs.getString(tbl + "_name");

        return t;
    }

    public static User getUser(ResultSet rs) throws SQLException {
        User u = null;
        String tbl = FilterUtils.USER;
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, tbl + "_timeins");
        Timestamp timeupd = rs.getTimestamp(tbl + "_timeupd");
        u = new User(id, timeins, timeupd);
        
        u.fName = rs.getString(tbl + "_f_name");
        u.lName = rs.getString(tbl + "_l_name");
        
        return u;
    }
    
    public static User getUserIns(ResultSet rs) throws SQLException {
        User u = null;
        String tbl = "userins";
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        
        u = new User(id);
        u.fName = rs.getString(tbl + "_f_name");
        u.lName = rs.getString(tbl + "_l_name");
        
        return u;
    }

    public static User getUserUpd(ResultSet rs) throws SQLException {
        User u = null;
        String tbl = "userupd";
        Integer id = safeInt(rs, tbl + "_ID");
        if (id == null || id == 0) {
            return null;
        }
        
        u = new User(id);
        u.fName = rs.getString(tbl + "_f_name");
        u.lName = rs.getString(tbl + "_l_name");
        
        return u;
    }
    
    public static boolean checkColumn(ResultSet rs, String label) {
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
