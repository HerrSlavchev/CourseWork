/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.CRUDHelper;
import dao.DAOUtils;
import dto.Result;
import dto.domain.Notification;
import dto.domain.TriggerType;
import dto.domain.Region;
import dto.domain.User;
import dto.filters.RegionFilter;
import dto.session.Session;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import services.client.NotifiableIF;
import services.server.RegionDAIF;

/**
 *
 * @author root
 */
public class RegionDAImpl implements RegionDAIF {

    @Override
    public Result<Region> insertRegion(final List<Region> ins, Session session) throws RemoteException {

        List<Region> lst = new ArrayList<Region>();
        Exception exc = null;

        final String insert = DAOUtils.generateStmt(
                "INSERT INTO region",
                "(name)",
                " VALUES ",
                "(?)");

        try {
            CRUDHelper helper = new CRUDHelper<Region>(session, ins) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(insert);

                    int count = 0;
                    for (Region r : ins) {
                        stmt.setString(1, r.name);
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
            exc = e;
        }

        return new Result(lst, exc);
    }

    @Override
    public Result<Region> updateRegion(final List<Region> upd, Session session) throws RemoteException {
        List<Region> lst = new ArrayList<Region>();
        Exception exc = null;

        final String update = DAOUtils.generateStmt(
                "UPDATE region SET",
                "name = ? ",
                "WHERE ",
                "id = ?");

        try {
            CRUDHelper<Region> helper = new CRUDHelper<Region>(session, upd) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    int count = 0;
                    stmt = conn.prepareStatement(update);
                    for (Region r : upd) {
                        stmt.setString(1, r.name);
                        stmt.setInt(2, r.getID());

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
            exc = e;
        }
        return new Result(lst, exc);
    }

    @Override
    public Result<Region> deleteRegion(List<Region> del, Session session) throws RemoteException {
        List<Region> res = new ArrayList<Region>();
        Exception ex = null;
        try {
            //TODO
        } catch (Exception exc) {
            ex = exc;
        }
        return new Result(res, ex);
    }

    @Override
    public Result<Region> fetchRegions(RegionFilter filter) {
        List<Region> res = new ArrayList<Region>();
        Exception ex = null;
        try {
            //WARNING
            //System.out.println("fetching");
            int rand = (int) (5 * Math.random() + 30);
            for (int i = 0; i < rand; i++) {
                Region r = new Region(i);
                r.name = "huc " + i;
                res.add(r);
            }
            /*for (NotifiableIF ntf : ClientManagerImpl.getClients().values()){
             int idx = (int) (Math.random() * NotificationType.values().length);
             Notification info = new Notification(0, NotificationType.values()[idx], null);
             List<Notification> news = new ArrayList<Notification>();
             news.add(info);
             ntf.acceptNotifications(news);
             }*/
            //TODO
        } catch (Exception exc) {
            ex = exc;
        }
        return new Result(res, ex);
    }

}
