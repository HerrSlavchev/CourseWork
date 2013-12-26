/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.domain.Group;
import dto.domain.PersistedDTO;
import dto.domain.User;
import dto.rolemanagement.Role;
import dto.session.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import services.implementations.ClientManagerImpl;

/**
 * EXPERIMENTAL Template-Pattern (behavioural pattern), enforces common
 * execution logic
 *
 * @author root
 */
public abstract class CRUDHelper<E extends PersistedDTO> {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    private final Session session;
    private Integer ID_user;
    private final List<E> upd;

    private List<Role> roles;
    private boolean checkRights = true;
    private boolean checkSession = true;

    public void setRightsCheck(boolean b) {
        checkRights = b;
    }

    public void setSessionCheck(boolean b) {
        checkSession = b;
    }

    public CRUDHelper(Session session, List<E> upd) {
        this(session, upd, null);
    }

    public CRUDHelper(Session session, List<E> upd, List<Role> roles) {
        this.session = session;
        this.upd = upd;
        this.roles = roles;
    }

    private void validateSession() throws Exception {

        if (session == null) {
            throw new Exception("Failed to validate the session. Please login.");
        }

        ID_user = ClientManagerImpl.getID(session);
        if (ID_user == null) {
            throw new Exception("Failed to validate the session. Please login.");
        }
    }

    private void validateRights() throws Exception {

        String isAllowed = DAOUtils.generateStmt(
                "SELECT EXISTS (SELECT 1 FROM ",
                "user u ",
                "WHERE u.id = ? ",
                "AND u.role = ?)");
        if (upd.size() == 1) { //editing something we have created
            PersistedDTO obj = upd.get(0);
            int oid = -1;
            if (obj instanceof User) {
                oid = obj.getID();
            } else {
                User userIns = obj.getUserIns();
                if (userIns != null) {
                    oid = userIns.getID();
                }
            }
            if (ID_user.equals(oid)) {
                return;
            }
            if (obj instanceof Group) { //groups have moderators, who also can modify things
                isAllowed = DAOUtils.generateStmt(
                        "SELECT EXISTS (SELECT 1 FROM ",
                        "user u ",
                        "LEFT OUTER JOIN igroup_user igu ",
                        "ON (igu.ID_user = u.ID) ",
                        "WHERE u.id = ? ",
                        "AND (u.role = ? ",
                        "OR igu.role = 1) ",
                        ")");
            }
        }

        stmt = conn.prepareStatement(isAllowed);
        stmt.setInt(1, ID_user);
        stmt.setInt(2, Role.ADMIN.ordinal());

        rs = stmt.executeQuery();
        if (rs.getFetchSize() == 0) {
            throw new Exception("You do not have sufficient rights for this action.");
        }
    }

    private void validateRole() throws Exception {
        StringBuilder sb = new StringBuilder(8);
        sb.append("(");
        for (Role r : roles) {
            sb.append(r.ordinal());
            sb.append(", ");
        }
        sb.append(")");
        String vals = sb.toString();
        vals = vals.replace(", )", ")");

        String isAllowed = DAOUtils.generateStmt(
                "SELECT EXISTS (SELECT 1 FROM ",
                "user u ",
                "WHERE u.id = ? ",
                "AND u.role IN" + vals + " )");
        stmt = conn.prepareStatement(isAllowed);
        stmt.setInt(1, ID_user);

        rs = stmt.executeQuery();
        if (rs.getFetchSize() == 0) {
            throw new Exception("You do not have sufficient rights for this action.");
        }
    }

    public final void performCUD() throws Exception {
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);
            if (checkSession) {
                validateSession();
            } else if (checkRights) {
                if (roles != null && roles.size() > 0) {
                    validateRole();
                } else {
                    validateRights();
                }
            }

            runQueries(conn, stmt, rs);

            conn.commit();
        } catch (Exception exc) {
            conn.rollback();
            throw exc;
        } finally {
            DAOUtils.releaseRes(conn, stmt, rs);
        }
    }

    public String markUserIns(String insrt) {
        String extd = insrt.replace(") VALUES", ", ID_userins) VALUES");
        String marked = extd.replace("?)", "?, " + ID_user + ")");
        return marked;
    }

    public String markUserUpd(String updt) {
        String extd = updt.replace(" WHERE", ", ID_userupd = " + ID_user + " WHERE");
        return extd;
    }

    protected abstract void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception;
}
