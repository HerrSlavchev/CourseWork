/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.CRUDHelper;
import dao.DAOUtils;
import dao.FilterUtils;
import dao.ResultSetInterpreterIF;
import dto.Result;
import dto.domain.Category;
import dto.domain.ChildrenManager;
import dto.domain.Group;
import dto.domain.Interest;
import dto.domain.SubCategory;
import dto.domain.User;
import dto.filters.InterestFilter;
import dto.session.Session;
import exceptions.ExceptionProcessor;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import services.server.InterestDAIF;

/**
 *
 * @author root
 */
public class InterestDAImpl implements InterestDAIF {

    private ResultSetInterpreterIF resultSetInterpreter = DAOUtils.resultSetInterpreter;
    
    private InterestDAImpl(){
        
    }
    
    private static InterestDAImpl instance;
    
    public static InterestDAImpl getInstance(){
        if (instance == null){
            instance = new InterestDAImpl();
        }
        return instance;
    }
    
    
    @Override
    public Result<Interest> insertInterest(final List<Interest> ins, Session session) throws RemoteException {
        final List<Category> lst = new ArrayList<>();
        final List<Integer> autoIDs = new ArrayList<>();
        Exception exc = null;

        final String insertIntr = DAOUtils.generateStmt(
                "INSERT INTO interest",
                "(name, description)",
                " VALUES ",
                "(?, ?)");
        final String insertSBCI = DAOUtils.generateStmt(
                "INSERT INTO subcategory_interest",
                "(ID_interest, ID_subcategory)",
                " VALUES ",
                "(?, ?)"
        );

        final String insertIU = DAOUtils.generateStmt(
                "INSERT INTO interest_user",
                "(ID_interest, ID_user)",
                " VALUES ",
                "(?, ?)"
        );
        
        final int userID = ClientManagerImpl.getID(session);
        try {
            CRUDHelper helper = new CRUDHelper<Interest>(session, ins) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {

                    String insertIntrMarked = this.markUserIns(insertIntr);

                    for (Interest intr : ins) {
                        //I: insert head
                        stmt = conn.prepareStatement(insertIntrMarked, PreparedStatement.RETURN_GENERATED_KEYS);
                        stmt.setString(1, intr.getName());
                        stmt.setString(2, intr.getDescription());
                        stmt.execute();
                        rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            //II: get PK
                            int autoID = rs.getInt(1);
                            ChildrenManager<SubCategory> chm = intr.getSubCategories();
                            List<SubCategory> newSubCats = chm.getNewChildren();
                            //III: if we have rows, insert them
                            if (false == newSubCats.isEmpty()) {
                                stmt = conn.prepareStatement(insertSBCI);
                                for (SubCategory sbc : newSubCats) {
                                    stmt.setInt(1, autoID);
                                    stmt.setInt(2, sbc.getID());
                                    stmt.addBatch();
                                }
                                int[] batchRes = stmt.executeBatch();
                                if (DAOUtils.processBatchRes(batchRes) == false) {
                                    throw new Exception("Operation failed.");
                                }
                            }
                            
                            //automatically bind entity with creating user
                            stmt = conn.prepareStatement(insertIU);
                            stmt.setInt(1, autoID);
                            stmt.setInt(2, userID);
                            stmt.execute();
                            
                            autoIDs.add(autoID);
                        }
                    }
                }
            };
            helper.performCUD();
        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
        }

        return new Result(lst, exc, autoIDs);
    }

    @Override
    public Result<Interest> updateInterest(final List<Interest> upd, Session session) throws RemoteException {
        
        final List<Category> lst = new ArrayList<>();
        Exception exc = null;

        final String updateIntr = DAOUtils.generateStmt(
                "UPDATE interest SET ",
                "name = ?, ",
                "description = ? ",
                "WHERE ",
                "id = ? ",
                "AND timeupd = ?");

        final String insertSBCI = DAOUtils.generateStmt(
                "INSERT INTO subcategory_interest",
                "(ID_interest, ID_subcategory)",
                " VALUES ",
                "(?, ?)"
        );
        final String deleteSBCI = DAOUtils.generateStmt(
                "DELETE FROM subcategory_interest ",
                "WHERE ",
                "id_interest = ? ",
                "AND id_subcategory = ?"
        );

        final String insertIUSR = DAOUtils.generateStmt(
                "INSERT INTO interest_user",
                "(ID_interest, ID_user, notify)",
                " VALUES ",
                "(?, ?, ?)"
        );
        final String updateIUSR = DAOUtils.generateStmt(
                "UPDATE interest_user",
                " SET notify=?",
                " WHERE ",
                " ID_interest=?",
                " AND ID_user=?"
        );
        final String deleteIUSR = DAOUtils.generateStmt(
                "DELETE FROM interest_user",
                " WHERE ",
                " ID_interest=?",
                " AND ID_user=?"
        );
        
        try {
            CRUDHelper helper = new CRUDHelper<Interest>(session, upd) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {

                    String updateIntrMarked = this.markUserUpd(updateIntr);

                    for (Interest intr : upd) {
                        //I: update head
                        stmt = conn.prepareStatement(updateIntrMarked);
                        stmt.setString(1, intr.getName());
                        stmt.setString(2, intr.getDescription());
                        stmt.setInt(3, intr.getID());
                        stmt.setTimestamp(4, intr.getTimeUpd());
                        int updated = stmt.executeUpdate();
                        
                        if (updated == 0){
                            throw new Exception("Data has been remotely modified. Operation failed.");
                        }
                        ChildrenManager<SubCategory> chmCats = intr.getSubCategories();
                        List<SubCategory> newCats = chmCats.getNewChildren();
                        List<SubCategory> removedCats = chmCats.getRemovedChildren();
                        //III: if we have rows, insert them
                        if (false == removedCats.isEmpty()) {
                            stmt = conn.prepareStatement(deleteSBCI);
                            for (SubCategory sbc : removedCats) {
                                stmt.setInt(1, intr.getID());
                                stmt.setInt(2, sbc.getID());
                                stmt.addBatch();
                            }
                            int[] batchRes = stmt.executeBatch();
                            if (DAOUtils.processBatchRes(batchRes) == false) {
                                throw new Exception("Operation failed.");
                            }
                        }
                        if (false == newCats.isEmpty()) {
                            stmt = conn.prepareStatement(insertSBCI);
                            for (SubCategory sbc : newCats) {
                                stmt.setInt(1, intr.getID());
                                stmt.setInt(2, sbc.getID());
                                stmt.addBatch();
                            }
                            int[] batchRes = stmt.executeBatch();
                            if (DAOUtils.processBatchRes(batchRes) == false) {
                                throw new Exception("Operation failed.");
                            }
                        }
                        
                        //manage user notifications, if any
                        ChildrenManager<User> chmUsr = intr.getUsers();
                        List<User> newUsers = chmUsr.getNewChildren();
                        List<User> modifiedUsers = chmUsr.getModifiedChildren();
                        List<User> removedUsers = chmUsr.getRemovedChildren();
                        
                        if (false == modifiedUsers.isEmpty()){
                            stmt = conn.prepareStatement(updateIUSR);
                            for (User usr : modifiedUsers) {
                                stmt.setInt(1, usr.isNotify() ? 1 : 0);
                                stmt.setInt(2, intr.getID());
                                stmt.setInt(3, usr.getID());
                                stmt.addBatch();
                            }
                            int[] batchRes = stmt.executeBatch();
                            if (DAOUtils.processBatchRes(batchRes) == false) {
                                throw new Exception("Operation failed.");
                            }
                        }
                        if (false == removedUsers.isEmpty()) {
                            stmt = conn.prepareStatement(deleteIUSR);
                            for (User usr : removedUsers) {
                                stmt.setInt(1, intr.getID());
                                stmt.setInt(2, usr.getID());
                                stmt.addBatch();
                            }
                            int[] batchRes = stmt.executeBatch();
                            if (DAOUtils.processBatchRes(batchRes) == false) {
                                throw new Exception("Operation failed.");
                            }
                        }
                        if (false == newUsers.isEmpty()) {
                            stmt = conn.prepareStatement(deleteSBCI);
                            for (User usr : newUsers) {
                                stmt.setInt(1, intr.getID());
                                stmt.setInt(2, usr.getID());
                                stmt.setInt(3, usr.isNotify() ? 1 : 0);
                                stmt.addBatch();
                            }
                            int[] batchRes = stmt.executeBatch();
                            if (DAOUtils.processBatchRes(batchRes) == false) {
                                throw new Exception("Operation failed.");
                            }
                        }
                        
                    }
                }
            };
            helper.performCUD();
        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
        }

        return new Result(lst, exc);
    }

    @Override
    public Result<Interest> deleteInterest(List<Interest> del, Session session) throws RemoteException {
        return new Result(null, new Throwable("NOT DONE YET!"));
    }

    @Override
    public Result<Interest> fetchInterests(final InterestFilter filter) throws RemoteException {

        Result<Interest> res = null;
        final List<Interest> lst = new ArrayList<>();
        Exception exc = null;

        String fetchSubCategory = FilterUtils.evalFetch(filter.fetchSubCategories, FilterUtils.fetchSubcategory);
        String joinSBCI = "";
        String joinSubCategory = "";
        if (filter.fetchSubCategories) {
            joinSBCI = " LEFT OUTER JOIN subcategory_interest sbci ON(sbci.ID_interest = intr.ID)";
            joinSubCategory = " LEFT OUTER JOIN subcategory sbc ON(sbc.ID = sbci.ID_subcategory)";
        }

        String fetchGroup = FilterUtils.evalFetch(filter.fetchGroups, FilterUtils.fetchGroup);
        String joinGroup = "";
        if (filter.fetchGroups) {
            joinGroup = " LEFT OUTER JOIN group grp ON(grp.ID_interest = intr.ID)";
        }

        String fetchCategory = FilterUtils.evalFetch(filter.fetchCategories, FilterUtils.fetchCategory);
        String joinCategory = "";
        if (filter.fetchCategories || false == filter.categories.isEmpty()) {
            joinSBCI = " LEFT OUTER JOIN subcategory_interest sbci ON(sbci.ID_interest = intr.ID)";
            joinSubCategory = " LEFT OUTER JOIN subcategory sbc ON(sbc.ID = sbci.ID_subcategory)";
            joinCategory = " LEFT OUTER JOIN category cat ON(cat.ID = sbc.ID_category)";
        }

        String fetchUser = FilterUtils.evalFetch(filter.fetchUsers, FilterUtils.fetchUser  + ", iusr.notify AS iusr_notify");
        String joinUserInterest = "";
        String joinUser = "";
        if (filter.fetchUsers || false == filter.users.isEmpty()) {
            joinUserInterest = " LEFT OUTER JOIN interest_user iusr ON(iusr.ID_interest = intr.ID)";
            joinUser = " LEFT OUTER JOIN user usr ON(usr.ID = iusr.ID_user)";
        }

        String deepFetch = "";
        String deepJoin = "";
        if (filter.deepFetch) {
            deepFetch = ", intr.description AS intr_description, intr.timeins AS intr_timeins, intr.ID_userupd AS userupd_ID,"
                    + " userins.f_name AS userins_f_name, userins.l_name AS userins_l_name,"
                    + " userupd.f_name AS userupd_f_name, userupd.l_name AS userupd_l_name";
            deepJoin = " INNER JOIN user userins ON(userins.ID = intr.ID_userins)"
                    + " LEFT OUTER JOIN user userupd ON(userupd.ID = intr.ID_userupd)";
        }

        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                FilterUtils.fetchInterest.replaceFirst(",", ""),
                deepFetch,
                fetchSubCategory,
                fetchCategory,
                fetchGroup,
                fetchUser,
                " FROM interest " + FilterUtils.INTEREST,
                joinSBCI,
                joinSubCategory,
                joinCategory,
                joinGroup,
                joinUserInterest,
                joinUser,
                deepJoin,
                FilterUtils.prepareWHERE(filter)
        );
        try {
            CRUDHelper<Interest> helper = new CRUDHelper<Interest>(null, null) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(slct);
                    rs = stmt.executeQuery();

                    Map<Integer, Interest> map = new HashMap<>();
                    while (rs.next()) {
                        Interest curr = resultSetInterpreter.getInterest(rs);
                        Interest old = map.get(curr.getID());
                        if (old == null) {
                            curr.setUserIns(resultSetInterpreter.getUserIns(rs));
                            curr.setUserUpd(resultSetInterpreter.getUserUpd(rs));
                            if (filter.deepFetch) {
                                curr.setDescription(rs.getString("intr_description"));
                            }
                            old = curr;
                            map.put(old.getID(), old);
                        }
                        if (filter.fetchSubCategories) {
                            
                            SubCategory sbcat = resultSetInterpreter.getSubCategory(rs);
                            if (sbcat != null) {
                                old.getSubCategories().addOldChild(sbcat);
                            }
                        }
                        Category e = resultSetInterpreter.getCategory(rs);
                        if (e != null) {
                            old.getCategories().add(e);
                        }
                        Group g = resultSetInterpreter.getGroup(rs);
                        if (g != null){
                            old.getGroups().add(g);
                        }
                        User u = resultSetInterpreter.getUser(rs);
                        if (u != null) {
                            u.setNotify(rs.getInt("iusr_notify") == 1);
                            old.getUsers().addOldChild(u);
                        }
                    }

                    lst.addAll(map.values());
                }
            };
            helper.setSessionCheck(false);
            helper.setRightsCheck(false);
            helper.performCUD();
        } catch (Exception e) {
            exc = ExceptionProcessor.processException(e);
        }
        return new Result(lst, exc);
    }

}
