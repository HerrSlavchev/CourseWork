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

    private ResultSetInterpreterIF resultSetInterpreter = DAOUtils.resultSetInterpreter;
    
    private RegionDAImpl(){
        
    }
    
    private static RegionDAImpl instance;
    
    public static RegionDAImpl getInstance(){
        if (instance == null){
            instance = new RegionDAImpl();
        }
        return instance;
    }
    
    
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
                        stmt.setString(1, r.getName());
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
                "UPDATE region SET ",
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
                        stmt.setString(1, r.getName());
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

        String groupBy = " GROUP BY reg.ID";
        
        String fetchTown = "";
        String countTown = ", COUNT(twn.ID) AS t_count";
        String joinTowns = " LEFT OUTER JOIN town twn ON(twn.ID_region = reg.ID)";
        if (filter.fetchTowns) {
            countTown = ", 0 AS t_count";
            fetchTown = FilterUtils.fetchTown;
            groupBy += ", twn.ID";
        }
        String fetchUser = "";
        String countUser = ", COUNT(usr.ID) AS u_count";
        String joinTownUser = " LEFT OUTER JOIN town_user tu ON (tu.ID_town = twn.ID)";
        String joinUsers = " LEFT OUTER JOIN user usr ON (tu.ID_user = usr.ID)";
        if (filter.fetchUsers) {
            fetchUser = FilterUtils.fetchUser;
            countUser = ", 0 AS u_count";
            groupBy += ", usr.ID";
        }
        String fetchEvent = "";
        String countEvent = ", COUNT(evt.ID) AS e_count";
        String joinEventTown = " LEFT OUTER JOIN town_event te ON (te.ID_town = twn.ID)";
        String joinEvents = " LEFT OUTER JOIN event evt ON (te.ID_event = evt.ID)";
        if (filter.fetchEvents) {
            fetchEvent = FilterUtils.fetchEvent;
            countEvent = ", 0 AS e_count";
            groupBy += ", evt.ID";
        }
        
        //all flags up, dismiss group by clause
        if(filter.fetchEvents && filter.fetchTowns && filter.fetchUsers){
            groupBy = "";
        }
        
        
        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                FilterUtils.fetchRegion.replaceFirst(",", ""),
                countTown,
                countUser,
                countEvent,
                fetchTown,
                fetchUser,
                fetchEvent,
                " FROM region " + FilterUtils.REGION,
                joinTowns,
                joinTownUser,
                joinUsers,
                joinEventTown,
                joinEvents,
                FilterUtils.prepareWHERE(filter),
                groupBy
        );
        try {
            CRUDHelper<Region> helper = new CRUDHelper<Region>(null, null) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(slct);
                    rs = stmt.executeQuery();

                    Map<Integer, Region> map = new HashMap<>();
                    while (rs.next()) {
                        Region curr = resultSetInterpreter.getRegion(rs);
                        Region old = map.get(curr.getID());
                        if (old == null) {
                            curr.setEventCount(rs.getInt("e_count"));
                            curr.setTownCount(rs.getInt("t_count"));
                            curr.setUserCount(rs.getInt("u_count"));
                            old = curr;
                            map.put(old.getID(), old);
                        }
                        Town t = resultSetInterpreter.getTown(rs);
                        if (t != null) {
                            old.getTowns().add(t);
                        }
                        Event e = resultSetInterpreter.getEvent(rs);
                        if (e != null) {
                            old.getEvents().add(e);
                        }
                        User u = resultSetInterpreter.getUser(rs);
                        if (u != null) {
                            old.getUsers().add(u);
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
