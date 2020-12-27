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

    public StringBuilder getColumnNames(boolean cisla){
        StringBuilder arr = new StringBuilder();
        int counter=1;
        for (Column e :columns){
            if(cisla)arr.append(counter+".");
            arr.append(e.getName()+" ");
            counter++;
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
