/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.ConnectionProvider;
import dao.DAOUtils;
import dto.Result;
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

    private Session session;
    private Integer ID_user;
    private List<E> upd;

    private boolean checkRights = true;
    private boolean checkSession = true;

    public void setRightsCheck(boolean b) {
        checkRights = b;
    }

    public void setSessionCheck(boolean b) {
        checkSession = b;
    }

    public CRUDHelper(Session session, List<E> upd) {
        this.session = session;
        this.upd = upd;
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

        String isAllowed = "SELECT EXISTS (SELECT 1 FROM "
                + "user u "
                + "WHERE u.id = ? "
                + "AND u.role = ?)";
        stmt = conn.prepareStatement(isAllowed);
        stmt.setInt(1, ID_user);
        stmt.setInt(2, Role.ADMIN.ordinal());

        if (upd.size() == 1) { //editing something we have created
            PersistedDTO obj = upd.get(0);
            int oid = obj instanceof User ? obj.getID() : obj.getUserIns().getID();
            if (ID_user.equals(oid)) {
                return;
            }
            if (obj instanceof Group) { //groups have moderators, who also can modify things
                isAllowed = "SELECT EXISTS (SELECT 1 FROM "
                        + "user u "
                        + "LEFT OUTER JOIN igroup_user igu "
                        + "ON (igu.ID_user = u.ID) "
                        + "WHERE u.id = ? "
                        + "AND (u.role = ? "
                        + "OR igu.role = 1) "
                        + ")";
            }
        }

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
                validateRights();
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
