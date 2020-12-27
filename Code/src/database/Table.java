package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private List<Column>columns;

    public Table(String name){
        this.name=name;
        this.columns=new ArrayList<>();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String[] getColumnNames(){
        String[]arr = new String[columns.size()];
        for (int i=0;i<arr.length;i++){
            arr[i]=columns.get(i).getName();
        }
        return arr;
    }

    public String getName(){
        return name;
    }

    public void initColumns(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT COLUMN_NAME,DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+name+"'");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            columns.add(new Column(rs.getString("COLUMN_NAME"),rs.getString("DATA_TYPE")));
        }
    }
}
