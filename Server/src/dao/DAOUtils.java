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
import dto.domain.PersistedDTO;
import dto.domain.Publication;
import dto.domain.Region;
import dto.domain.SubCategory;
import dto.domain.Town;
import dto.domain.User;
import dto.rolemanagement.Role;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author root
 */
public class DAOUtils {

    public static final int MAX_BATCH_SIZE = 1000;

    /**
     * Commonly used for select statements;
     */
    public static final String fetchCategory = ", cat.ID AS cat_ID, cat.name AS cat_name";
    public static final String fetchConversation = ", con.ID AS con_ID, con.topic AS con_topic, con.timeupd AS con_timeupd";
    public static final String fetchEvent = ", e.ID AS e_ID, e.name AS e_name, e.datefrom AS e_datefrom, e.dateto AS e_dateto, e.timeupd AS e_timeupd";
    public static final String fetchIGroup = ", ig.ID AS ig_ID, ig.name AS ig_name, ig.timeupd AS ig_timeupd";
    public static final String fetchInterest = ", int.ID AS int_ID, int.name AS int_name, int.timeupd AS int_timeupd";
    public static final String fetchMessage = ", m.ID AS m_ID, m.text AS m_text, m.timeupd AS m_timeupd";
    public static final String fetchPublication = ", p.ID AS p_ID, p.text AS p_text, p.timeupd AS p_timeupd";
    public static final String fetchRegion = ", r.ID AS r_ID, r.name AS r_name";
    public static final String fetchSubcategory = ", s.ID AS s_ID, s.name AS s_name";
    public static final String fetchTown = ", t.ID AS t_ID, t.name AS t_name";
    public static final String fetchUser = ", u.ID AS u_ID, u.f_name AS u_f_name, u.l_name AS u_l_name, u.e_mail AS u_e_mail, u.timeupd AS u_timeupd";

    public static Category getCategory(ResultSet rs) throws SQLException {
        Category cat = null;
        int id = rs.getInt("cat_ID");
        cat = new Category(id);
        cat.name = rs.getString("cat_name");
        cat.description = safeStr(rs, "cat_description");
        return cat;
    }

    public static Conversation getConversation(ResultSet rs) throws SQLException {
        Conversation con = null;
        Integer id = safeInt(rs, "con_ID");
        if (id == null) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, "con_timeins");
        Timestamp timeupd = rs.getTimestamp("con_timeupd");
        con = new Conversation(id, timeins, timeupd);
        con.topic = rs.getString("con_topic");
        return con;
    }

    public static Event getEvent(ResultSet rs) throws SQLException {
        Event e = null;
        Integer id = safeInt(rs, "e_ID");
        if (id == null) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, "e_timeins");
        Timestamp timeupd = rs.getTimestamp("e_timeupd");
        e = new Event(id, timeins, timeupd);
        e.name = rs.getString("e_name");
        e.dateFrom = rs.getDate("e_datefrom");
        e.dateTo = rs.getDate("e_dateto");
        e.text = safeStr(rs, "e_text");

        return e;
    }

    public static Group getGroup(ResultSet rs) throws SQLException {
        Group ig = null;
        Integer id = safeInt(rs, "ig_ID");
        if (id == null) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, "ig_timeins");
        Timestamp timeupd = rs.getTimestamp("ig_timeupd");
        ig = new Group(id, timeins, timeupd);
        ig.description = safeStr(rs, "ig_description");
        ig.name = rs.getString("ig_name");
        return ig;
    }

    public static Interest getInterest(ResultSet rs) throws SQLException {
        Interest i = null;
        Integer id = safeInt(rs, "int_ID");
        if (id == null) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, "int_timeins");
        Timestamp timeupd = rs.getTimestamp("int_timeupd");
        i = new Interest(id, timeins, timeupd);
        i.description = safeStr(rs, "int_description");
        i.name = rs.getString("int_name");
        return i;
    }

    public static Message getMessage(ResultSet rs) throws SQLException {
        Message m = null;
        Integer id = safeInt(rs, "m_ID");
        if (id == null) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, "m_timeins");
        Timestamp timeupd = rs.getTimestamp("m_timeupd");
        m = new Message(id, timeins, timeupd);
        m.text = safeStr(rs, "m_text");
        return m;
    }

    public static Publication getPublication(ResultSet rs) throws SQLException {
        Publication p = null;
        Integer id = safeInt(rs, "p_ID");
        if (id == null) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, "p_timeins");
        Timestamp timeupd = rs.getTimestamp("p_timeupd");
        p = new Publication(id, timeins, timeupd);
        p.text = safeStr(rs, "p_text");

        return p;
    }

    public static Region getRegion(ResultSet rs) throws SQLException {
        Region r = null;
        Integer id = safeInt(rs, "r_ID");
        if (id == null) {
            return null;
        }
        r = new Region(id);
        r.name = rs.getString("r_name");

        return r;
    }

    public static SubCategory getSubCategory(ResultSet rs) throws SQLException {
        SubCategory s = null;
        Integer id = safeInt(rs, "s_ID");
        if (id == null) {
            return null;
        }
        s = new SubCategory(id);
        s.description = safeStr(rs, "s_description");
        s.name = rs.getString("s_name");

        return s;
    }

    public static Town getTown(ResultSet rs) throws SQLException {
        Town t = null;
        Integer id = safeInt(rs, "t_ID");
        if (id == null) {
            return null;
        }
        t = new Town(id);
        t.name = rs.getString("t_name");

        return t;
    }

    public static User getUser(ResultSet rs) throws SQLException {
        User u = null;
        Integer id = safeInt(rs, "u_ID");
        if (id == null) {
            return null;
        }
        Timestamp timeins = safeTimestamp(rs, "u_timeins");
        Timestamp timeupd = rs.getTimestamp("u_timeupd");
        u = new User(id, timeins, timeupd);
        u.eMail = rs.getString("u_e_mail");
        u.fName = rs.getString("u_f_name");
        u.lName = rs.getString("u_l_name");
        u.description = safeStr(rs, "u_description");

        u.role = safeRole(rs, "u_role");
        u.sName = safeStr(rs, "u_s_name");

        return u;
    }

    public static void releaseRes(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
        if (rs != null && false == rs.isClosed()) {
            rs.close();
        }
        if (stmt != null && false == stmt.isClosed()) {
            stmt.close();
        }
        if (conn != null && false == conn.isClosed()) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public static void processSQLException(SQLException SQLe) {
        SQLe.printStackTrace();
    }

    private static boolean checkColumn(ResultSet rs, String label) {
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

    public static boolean processBatchRes(int[] batchRes) {

        if (batchRes.length == 0) {
            return false;
        }
        for (int i : batchRes) {
            if (i == Statement.EXECUTE_FAILED) {
                return false;
            }
        }
        return true;
    }

    public static String generateInClause(List<PersistedDTO> lst) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("(");
        for (PersistedDTO dto : lst) {
            sb.append(dto.getID());
            sb.append(", ");
        }
        sb.append(")");
        String all = sb.toString();
        String fixed = all.replace(", )", ")");
        return fixed;
    }

    public static String generateStmt(String... stmts) {
        StringBuilder sb = new StringBuilder(256);
        for (String stmt : stmts) {
            sb.append(stmt);
        }
        String all = sb.toString();
        System.out.println(all);
        return all;
    }
}
