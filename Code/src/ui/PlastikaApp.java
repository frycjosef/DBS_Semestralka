package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PlastikaApp {

    public static void main(String[] args) {
        String connectionUrl =
                "jdbc:sqlserver://147.230.21.34:1433;"
                        + "database=DBS2020_PetrPomeisl;"
                        + "user=student;"
                        + "password=student;";

        try (Connection connection = DriverManager.getConnection(connectionUrl);) {
            System.out.println("Connection established!");
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
