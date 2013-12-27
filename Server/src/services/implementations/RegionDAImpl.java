/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.implementations;

import dao.CRUDHelper;
import dao.DAOUtils;
import dto.Result;
import dto.domain.Event;
import dto.domain.Region;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.RegionFilter;
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
        Result<Region> res = null;
        final List<Region> lst = new ArrayList<>();
        Exception exc = null;

        String fetchTown = "";
        String joinTowns = " LEFT OUTER JOIN town t ON(t.ID_region = r.ID)";
        if (filter.fetchTowns) {
            fetchTown = DAOUtils.fetchTown;
        }
        String fetchUser = "";
        String joinTownUser = " LEFT OUTER JOIN town_user tu ON (tu.ID_town = t.ID)";
        String joinUsers = " LEFT OUTER JOIN user u ON (tu.ID_user = u.ID)";
        if (filter.fetchUsers) {
            fetchUser = DAOUtils.fetchUser;
        }
        String fetchEvent = "";
        String joinEventTown = " LEFT OUTER JOIN town_event te ON (te.ID_town = t.ID)";
        String joinEvents = " LEFT OUTER JOIN event e ON (te.ID_event = e.ID)";
        if (filter.fetchEvents) {
            fetchEvent = DAOUtils.fetchEvent;
        }
        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                DAOUtils.fetchRegion.replaceFirst(",", ""),
                ", COUNT(t.ID) AS t_count, COUNT(u.ID) AS u_count, COUNT(e.ID) AS e_count",
                fetchTown,
                fetchUser,
                fetchEvent,
                " FROM region r",
                joinTowns,
                joinTownUser,
                joinUsers,
                joinEventTown,
                joinEvents,
                " GROUP BY(r.ID)"
        );
        try {
            CRUDHelper<Region> helper = new CRUDHelper<Region>(null, null) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(slct);
                    rs = stmt.executeQuery();

                    Map<Integer, Region> map = new HashMap<>();
                    while (rs.next()) {
                        Region curr = DAOUtils.getRegion(rs);
                        Region old = map.get(curr.getID());
                        if (old == null) {
                            curr.eventCount = rs.getInt("e_count");
                            curr.townCount = rs.getInt("t_count");
                            curr.userCount = rs.getInt("u_count");
                            old = curr;
                            map.put(old.getID(), old);
                        }
                        Town t = DAOUtils.getTown(rs);
                        if (t != null) {
                            old.towns.add(t);
                        }
                        Event e = DAOUtils.getEvent(rs);
                        if (e != null) {
                            old.events.add(e);
                        }
                        User u = DAOUtils.getUser(rs);
                        if (u != null) {
                            old.users.add(u);
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
