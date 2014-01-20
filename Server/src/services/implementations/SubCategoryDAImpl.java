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
import dto.domain.Interest;
import dto.domain.SubCategory;
import dto.filters.SubCategoryFilter;
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
import services.server.SubCategoryDAIF;

/**
 *
 * @author root
 */
public class SubCategoryDAImpl implements SubCategoryDAIF{

    private ResultSetInterpreterIF resultSetInterpreter = DAOUtils.resultSetInterpreter;
    
    @Override
    public Result<SubCategory> insertSubCategory(final List<SubCategory> ins, Session session) throws RemoteException {
        List<SubCategory> lst = new ArrayList<SubCategory>();
        Exception exc = null;

        final String insert = DAOUtils.generateStmt(
                "INSERT INTO subcategory",
                "(name, ID_category, description)",
                " VALUES ",
                "(?, ?, ?)");
        try {
            CRUDHelper helper = new CRUDHelper<SubCategory>(session, ins) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(insert);

                    int count = 0;
                    for (SubCategory sbc : ins) {
                        stmt.setString(1, sbc.getName());
                        stmt.setInt(2, sbc.getCategory().getID());
                        stmt.setString(3, sbc.getDescription());
                        stmt.addBatch();
                        count++;

                        if (count == DAOUtils.MAX_BATCH_SIZE) {
                            count = 0;
                            int[] batchRes = stmt.executeBatch();
                            if (DAOUtils.processBatchRes(batchRes) == false) {
                                throw new Exception("Operation failed. Data has been remotely modified.");
                            }
                        }
                    }
                    if (count > 0) {
                        int[] batchRes = stmt.executeBatch();
                        if (DAOUtils.processBatchRes(batchRes) == false) {
                            throw new Exception("Operation failed. Data has been remotely modified.");
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
    public Result<SubCategory> updateSubCategory(final List<SubCategory> upd, Session session) throws RemoteException {
        List<SubCategory> lst = new ArrayList<SubCategory>();
        Exception exc = null;

        final String update = DAOUtils.generateStmt(
                "UPDATE subcategory SET ",
                "name = ?, ",
                "ID_category = ?, ",
                "description = ? ",
                "WHERE ",
                "id = ?");

        try {
            CRUDHelper<SubCategory> helper = new CRUDHelper<SubCategory>(session, upd) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    int count = 0;
                    stmt = conn.prepareStatement(update);
                    for (SubCategory sbc : upd) {
                        stmt.setString(1, sbc.getName());
                        stmt.setInt(2, sbc.getCategory().getID());
                        stmt.setString(3, sbc.getDescription());
                        stmt.setInt(4, sbc.getID());

                        stmt.addBatch();
                        count++;

                        if (count == DAOUtils.MAX_BATCH_SIZE) {
                            count = 0;
                            int[] batchRes = stmt.executeBatch();
                            if (DAOUtils.processBatchRes(batchRes) == false) {
                                throw new Exception("Operation failed. Data has been remotely modified.");
                            }
                        }
                    }
                    if (count > 0) {
                        int[] batchRes = stmt.executeBatch();
                        if (DAOUtils.processBatchRes(batchRes) == false) {
                            throw new Exception("Operation failed. Data has been remotely modified.");
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
    public Result<SubCategory> deleteSubCategory(List<SubCategory> del, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<SubCategory> fetchSubCategories(SubCategoryFilter filter) throws RemoteException {
        Result<SubCategory> res = null;
        final List<SubCategory> lst = new ArrayList<>();
        Exception exc = null;

        String fetchCategory = FilterUtils.fetchCategory;
        String joinCategory = " INNER JOIN category cat ON (cat.ID = sbc.ID_category)";
        
        String fetchInterest = "";
        String joinSubcategoryInterest = " LEFT OUTER JOIN subcategory_interest sbi ON (sbi.ID_subcategory = sbc.ID)";
        String joinInterest = " LEFT OUTER JOIN interest intr ON (sbi.ID_interest = intr.ID)";
        if (filter.fetchInterests) {
            fetchInterest = FilterUtils.fetchInterest;
        }
        
        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                FilterUtils.fetchSubcategory.replaceFirst(",", ""),
                ", sbc.description AS sbc_description, COUNT(intr.ID) AS intr_count",
                fetchCategory,
                fetchInterest,
                " FROM subcategory " + FilterUtils.SUBCATEGORY,
                joinCategory,
                joinSubcategoryInterest,
                joinInterest,
                FilterUtils.prepareWHERE(filter),
                " GROUP BY(sbc.ID)"
        );
        try {
            CRUDHelper<SubCategory> helper = new CRUDHelper<SubCategory>(null, null) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(slct);
                    rs = stmt.executeQuery();

                    Map<Integer, SubCategory> map = new HashMap<>();
                    while (rs.next()) {
                        SubCategory curr = resultSetInterpreter.getSubCategory(rs);
                        SubCategory old = map.get(curr.getID());
                        if (old == null) {
                            curr.setInterestCount(rs.getInt("intr_count"));
                            old = curr;
                            map.put(old.getID(), old);
                        }
                        Category cat = resultSetInterpreter.getCategory(rs);
                        if (cat != null) {
                            old.setCategory(cat);
                        }
                        Interest intr = resultSetInterpreter.getInterest(rs);
                        if (intr != null) {
                            old.interests.add(intr);
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
