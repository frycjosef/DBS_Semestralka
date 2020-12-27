package database;

import java.sql.*;
import java.util.List;

public class Database {
    private String name;
    private List<Table>tables;
    public Database(String name){
        this.name=name;
    }

    public Connection connect(String connectionUrl){
        try (Connection connection = DriverManager.getConnection(connectionUrl);) {
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void addTable(String tableName){
        tables.add(new Table(tableName));
    }
}
