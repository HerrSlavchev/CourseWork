/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Experimental : configuration constants and methods to generate SQL,
 * release SQL resources or process <code>SQLException</code>
 * @author root
 */
public class DAOUtils {

    public static final int MAX_BATCH_SIZE = 1000;

    public static final ResultSetInterpreterIF resultSetInterpreter = new BasicResultSetInterpreter();
    
    /**
     * Calls the <code>close()</code> method of each interface
     * in this order: <code>rs</code>, <code>stmt</code>, <code>conn</code>,
     * @param conn - returned to <code>ConnectionPool</code> if pooled
     * @param stmt
     * @param rs
     * @throws SQLException 
     */
    public static void releaseRes(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
        if (rs != null && false == rs.isClosed()) {
            rs.close();
        }
        if (stmt != null && false == stmt.isClosed()) {
            stmt.close();
        }
        if (conn != null && false == conn.isClosed()) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    /**
     * Process an exception; Currently only prints stack trace to console.
     * @param SQLe 
     */
    public static void processSQLException(SQLException SQLe) {
        SQLe.printStackTrace();
    }

    /**
     * Used to check if all batch operations were successful.
     * @param batchRes - result of a batched execution
     * @return <code>false</code> if one or more of statement executions failed.
     */
    public static boolean processBatchRes(int[] batchRes) {

        if (batchRes.length == 0) {
            return false;
        }
        for (int i : batchRes) {
            if (i == Statement.EXECUTE_FAILED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Uses a <code>StringBuilder</code> to form a long SQL clause from several statements.
     * @param stmts
     * @return - the concatenated result of all statements passed.
     */
    public static String generateStmt(String... stmts) {
        StringBuilder sb = new StringBuilder(256);
        for (String stmt : stmts) {
            sb.append(stmt);
        }
        String all = sb.toString();
        System.out.println(all);
        return all;
    }
}
