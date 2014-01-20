/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.implementations;

import dao.CRUDHelper;
import dao.DAOUtils;
import dao.FilterUtils;
import dao.BasicResultSetInterpreter;
import dao.ResultSetInterpreterIF;
import dto.Result;
import dto.domain.Category;
import dto.domain.Interest;
import dto.domain.SubCategory;
import dto.domain.User;
import dto.filters.CategoryFilter;
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
import services.server.CategoryDAIF;

/**
 *
 * @author root
 */
public class CategoryDAImpl implements CategoryDAIF{

    private ResultSetInterpreterIF resultSetInterpreter = DAOUtils.resultSetInterpreter;
    
    @Override
    public Result<Category> insertCategory(final List<Category> ins, Session session) throws RemoteException {
        
        List<Category> lst = new ArrayList<Category>();
        Exception exc = null;

        final String insert = DAOUtils.generateStmt(
                "INSERT INTO category",
                "(name, description)",
                " VALUES ",
                "(?, ?)");
        try {
            CRUDHelper helper = new CRUDHelper<Category>(session, ins) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(insert);

                    int count = 0;
                    for (Category cat : ins) {
                        stmt.setString(1, cat.getName());
                        stmt.setString(2, cat.getDescription());
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
    public Result<Category> updateCategory(final List<Category> upd, Session session) throws RemoteException {
        List<Category> lst = new ArrayList<Category>();
        Exception exc = null;

        final String update = DAOUtils.generateStmt(
                "UPDATE category SET ",
                "name = ?, ",
                "description = ? ",
                "WHERE ",
                "id = ?");

        try {
            CRUDHelper<Category> helper = new CRUDHelper<Category>(session, upd) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    int count = 0;
                    stmt = conn.prepareStatement(update);
                    for (Category cat : upd) {
                        stmt.setString(1, cat.getName());
                        stmt.setString(2, cat.getDescription());
                        stmt.setInt(3, cat.getID());

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
    public Result<Category> deleteCategory(List<Category> del, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<Category> fetchCategories(CategoryFilter filter) throws RemoteException {
        Result<Category> res = null;
        final List<Category> lst = new ArrayList<>();
        Exception exc = null;

        String fetchSubCategory = "";
        String joinSubCategory = " LEFT OUTER JOIN subcategory sbc ON (sbc.ID_category = cat.ID)";
        if (filter.fetchSubCategories) {
            fetchSubCategory = FilterUtils.fetchSubcategory;
        }
        
        String fetchInterest = "";
        String joinSubcategoryInterest = " LEFT OUTER JOIN subcategory_interest sbi ON (sbi.ID_subcategory = sbc.ID)";
        String joinInterest = " LEFT OUTER JOIN interest intr ON (sbi.ID_interest = intr.ID)";
        if (filter.fetchInterests) {
            fetchInterest = FilterUtils.fetchInterest;
        }
        
        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                FilterUtils.fetchCategory.replaceFirst(",", ""),
                ", cat.description as cat_description, COUNT(sbc.ID) AS sbc_count, COUNT(intr.ID) AS intr_count",
                fetchSubCategory,
                fetchInterest,
                " FROM category " + FilterUtils.CATEGORY,
                joinSubCategory,
                joinSubcategoryInterest,
                joinInterest,
                FilterUtils.prepareWHERE(filter),
                " GROUP BY(cat.ID)"
        );
        try {
            CRUDHelper<Category> helper = new CRUDHelper<Category>(null, null) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(slct);
                    rs = stmt.executeQuery();

                    Map<Integer, Category> map = new HashMap<>();
                    while (rs.next()) {
                        Category curr = resultSetInterpreter.getCategory(rs);
                        Category old = map.get(curr.getID());
                        if (old == null) {
                            int subCategoryCount = rs.getInt("sbc_count");
                            int interestCount = rs.getInt("intr_count");
                            curr.setSubCategoryCount(subCategoryCount);
                            curr.setInterestCount(interestCount);
                            old = curr;
                            map.put(old.getID(), old);
                        }
                        SubCategory sbcat = resultSetInterpreter.getSubCategory(rs);
                        if (sbcat != null) {
                            old.getSubCategories().add(sbcat);
                        }
                        Interest e = resultSetInterpreter.getInterest(rs);
                        if (e != null) {
                            old.getInterests().add(e);
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
