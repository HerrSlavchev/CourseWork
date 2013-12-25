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
import session.BasicSessionCodeProvider;
import session.SessionCodeProviderIF;

/**
 *
 * @author root
 */
public class DAOUtils {

    public static final int MAX_BATCH_SIZE = 1000;
    
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
    
    public static void processSQLException(SQLException SQLe){
        SQLe.printStackTrace();
    }
    
    public static boolean processBatchRes(int[] batchRes){
        
        if (batchRes.length == 0){
            return false;
        }
        for (int i : batchRes){
            if (i == Statement.EXECUTE_FAILED) {
                return false;
            }
        }
        return true;
    }
}
