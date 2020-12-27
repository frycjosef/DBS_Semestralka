package database;

import java.sql.*;

public class Database {
    private String name;
    public Database(String name){
        this.name=name;
    }

    boolean connect(String connectionUrl){
        try (Connection connection = DriverManager.getConnection(connectionUrl);) {
            return true;
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
