/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author root
 */
public class ConnectionProvider {

    private static ComboPooledDataSource cpds = new ComboPooledDataSource();

    private static boolean initiated = false;

    public static void init() throws PropertyVetoException, IOException {
        if (initiated) {
            return;
        }

        Properties dbProps = new Properties();
        dbProps.load(ConnectionProvider.class.getResourceAsStream("dbprops.properties"));
        
        String driverClass = dbProps.getProperty("driverClass");
        String port = dbProps.getProperty("port");
        String dataBase = dbProps.getProperty("dataBase");
        String addr = dbProps.getProperty("addr");
        String url = "jdbc:mysql://" + addr + ":" + port + "/" + dataBase;
        String user = dbProps.getProperty("user");
        String pass = dbProps.getProperty("pass");
        int maxPoolSize = Integer.parseInt(dbProps.getProperty("maxPoolSize", "50"));
        int minPoolSize = Integer.parseInt(dbProps.getProperty("minPoolSize", "10"));
        int maxStatements = Integer.parseInt(dbProps.getProperty("minPoolSize", "150"));
        int acquireIncrement = Integer.parseInt(dbProps.getProperty("acquireIncrement", "5"));
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(driverClass);
        cpds.setJdbcUrl(url);
        cpds.setUser(user);
        cpds.setPassword(pass);
        cpds.setMaxPoolSize(maxPoolSize);
        cpds.setMinPoolSize(minPoolSize);
        cpds.setMaxStatements(maxStatements);
        cpds.setAcquireIncrement(acquireIncrement);
        
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
