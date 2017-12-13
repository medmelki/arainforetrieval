package com.awn.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBAccess {

    public static Connection conn;  //global database connection


    public static void connectToDB() {
        String url = "jdbc:derby:wordnet";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("DriverManager class not found");
        } catch (Exception e) {
            System.err.println("Other error in loading driver "
                    + e.getMessage());
        }
        try {
            //Set Derby tuning parameters
            Properties p = System.getProperties();
            p.put("derby.storage.pageSize", "32000");
            p.put("derby.storage.pageCacheSize", "5000");
            conn = DriverManager.getConnection(url);

        } catch (SQLException sqle) {
            System.out.println("Unable to connect to " + url);

            System.err.println("SQLException: " + sqle.getMessage());
            System.err.println("SQLState: " + sqle.getSQLState());
            System.err.println("VendorError: " + sqle.getErrorCode());

        } catch (Exception ie) {
            System.err.println("error");

        }
    }

    public static void closeConnection() {
        try {
            conn.close();
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            System.out.println("SQL State: " + e.getSQLState());
        }
    }
}
