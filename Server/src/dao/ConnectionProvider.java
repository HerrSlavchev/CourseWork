/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author root
 */
public class ConnectionProvider {

    private static ComboPooledDataSource cpds = new ComboPooledDataSource();

    public static final int MAX_POOL_SIZE = 50;
    public static final int MIN_POOL_SIZE = 10;
    public static final int ACQUIRE_INCREMENT = 5;

    private static boolean initiated = false;

    public static void init() throws PropertyVetoException {
        if (initiated) {
            return;
        }

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3307/coursework");
        cpds.setUser("librarian");
        cpds.setPassword("zFOWCHAx2tFcJO9bTUvZuTsfnym9qWQtmjc/rsPih88EkctUQV+X1ynwdc+klxqZ3b07cC6jyX37\n" +
"7c+nP7uZCQa7M8/oq7u+zeS0sS/K8+wwhBOEBPFN8DqnWT84jjgzl1NPb9VePZGPETzIr3w=");
        cpds.setMaxPoolSize(MAX_POOL_SIZE);
        cpds.setMinPoolSize(MIN_POOL_SIZE);
        cpds.setMaxStatements(150);
        cpds.setAcquireIncrement(ACQUIRE_INCREMENT);
        
        initiated = true;
    }

    public static Connection getConnection() throws SQLException{
        if (false == initiated){
            throw new SQLException(ConnectionProvider.class + " has not been initialized. Call \"init()\" method first.");
        }
        return cpds.getConnection();
    }

    public static void close() {
        cpds.close();
    }

}
