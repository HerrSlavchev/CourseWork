/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.CRUDHelper;
import dao.DAOUtils;
import dto.Result;
import dto.domain.Category;
import dto.domain.ChildrenManager;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import services.server.InterestDAIF;

/**
 *
 * @author root
 */
public class InterestDAImpl implements InterestDAIF {

    @Override
    public Result<Interest> insertInterest(final List<Interest> ins, Session session) throws RemoteException {
        List<Category> lst = new ArrayList<>();
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
        try {
            CRUDHelper helper = new CRUDHelper<Interest>(session, ins) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {

                    String insertIntrMarked = this.markUserIns(insertIntr);

                    for (Interest intr : ins) {
                        //I: insert head
                        stmt = conn.prepareStatement(insertIntrMarked, PreparedStatement.RETURN_GENERATED_KEYS);
                        stmt.setString(1, intr.name);
                        stmt.setString(2, intr.description);
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
    public Result<Interest> updateInterest(List<Interest> upd, Session session) throws RemoteException {
        return new Result(null, new Throwable("NOT DONE YET!"));
    }

    @Override
    public Result<Interest> deleteInterest(List<Interest> del, Session session) throws RemoteException {
        return new Result(null, new Throwable("NOT DONE YET!"));
    }

    @Override
    public Result<Interest> fetchInterests(InterestFilter filter) throws RemoteException {
        List<Interest> lst = new ArrayList<>();
        Interest intr = new Interest(3, new Timestamp(10000), new Timestamp(10000), new User(2), new User(2));
        intr.name = "TESTING";
        intr.description = "test test test";
        
        List<SubCategory> sbcs = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            SubCategory sbc = new SubCategory(i);
            sbc.name = "sbc " + i;
            sbcs.add(sbc);
        }
        ChildrenManager<SubCategory> chm = new ChildrenManager<>(sbcs);
        intr.subCategories = chm;
        
        lst.add(intr);
        
        return new Result(lst, null);
    }

}
