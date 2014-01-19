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
import dto.domain.Event;
import dto.domain.Region;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.TownFilter;
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
import services.server.TownDAIF;

/**
 *
 * @author root
 */
public class TownDAImpl implements TownDAIF {

    private ResultSetInterpreterIF resultSetInterpreter = DAOUtils.resultSetInterpreter;
    
    @Override
    public Result<Town> insertTown(final List<Town> ins, Session session) throws RemoteException {
        List<Town> lst = new ArrayList<Town>();
        Exception exc = null;

        final String insert = DAOUtils.generateStmt(
                "INSERT INTO town",
                "(name, ID_region)",
                " VALUES ",
                "(?, ?)");
        try {
            CRUDHelper helper = new CRUDHelper<Town>(session, ins) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(insert);

                    int count = 0;
                    for (Town twn : ins) {
                        stmt.setString(1, twn.name);
                        stmt.setInt(2, twn.region.getID());
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
    public Result<Town> updateTown(final List<Town> upd, Session session) throws RemoteException {
        List<Region> lst = new ArrayList<Region>();
        Exception exc = null;

        final String update = DAOUtils.generateStmt(
                "UPDATE town SET ",
                "name = ?, ",
                "ID_region = ? ",
                "WHERE ",
                "id = ?");

        try {
            CRUDHelper<Town> helper = new CRUDHelper<Town>(session, upd) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    int count = 0;
                    stmt = conn.prepareStatement(update);
                    for (Town twn : upd) {
                        stmt.setString(1, twn.name);
                        stmt.setInt(2, twn.region.getID());
                        stmt.setInt(3, twn.getID());

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
    public Result<Town> deleteTown(List<Town> del, Session session) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<Town> fetchTown(TownFilter filter) throws RemoteException {
        Result<Town> res = null;
        final List<Town> lst = new ArrayList<>();
        Exception exc = null;

        String fetchRegion = FilterUtils.fetchRegion;
        String joinRegion = " INNER JOIN region reg ON (reg.ID = twn.ID_region)";
        String fetchUser = "";
        String joinTownUser = " LEFT OUTER JOIN town_user tu ON (tu.ID_town = twn.ID)";
        String joinUsers = " LEFT OUTER JOIN user usr ON (tu.ID_user = usr.ID)";
        if (filter.fetchUsers) {
            fetchUser = FilterUtils.fetchUser;
        }
        String fetchEvent = "";
        String joinEventTown = " LEFT OUTER JOIN town_event te ON (te.ID_town = twn.ID)";
        String joinEvents = " LEFT OUTER JOIN event evt ON (te.ID_event = evt.ID)";
        if (filter.fetchEvents) {
            fetchEvent = FilterUtils.fetchEvent;
        }
        
        final String slct = DAOUtils.generateStmt(
                "SELECT ",
                FilterUtils.fetchTown.replaceFirst(",", ""),
                fetchRegion,
                ", COUNT(usr.ID) AS u_count, COUNT(evt.ID) AS e_count",
                fetchUser,
                fetchEvent,
                " FROM town " + FilterUtils.TOWN,
                joinRegion,
                joinTownUser,
                joinUsers,
                joinEventTown,
                joinEvents,
                FilterUtils.prepareWHERE(filter),
                " GROUP BY(twn.ID)"
        );
        try {
            CRUDHelper<Region> helper = new CRUDHelper<Region>(null, null) {

                @Override
                protected void runQueries(Connection conn, PreparedStatement stmt, ResultSet rs) throws Exception {
                    stmt = conn.prepareStatement(slct);
                    rs = stmt.executeQuery();

                    Map<Integer, Town> map = new HashMap<>();
                    while (rs.next()) {
                        Town curr = resultSetInterpreter.getTown(rs);
                        Town old = map.get(curr.getID());
                        if (old == null) {
                            curr.eventCount = rs.getInt("e_count");
                            curr.userCount = rs.getInt("u_count");
                            old = curr;
                            map.put(old.getID(), old);
                        }
                        Region reg = resultSetInterpreter.getRegion(rs);
                        if (reg != null) {
                            old.region = reg;
                        }
                        Event e = resultSetInterpreter.getEvent(rs);
                        if (e != null) {
                            old.events.add(e);
                        }
                        User u = resultSetInterpreter.getUser(rs);
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
