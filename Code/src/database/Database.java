package database;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Database {
    private String name;
    private Connection conn;
    private List<Table>tables;
    private Scanner sc = new Scanner(System.in);
    public Database(String name){
        this.name=name;
        this.conn=null;
        this.tables=new ArrayList<>();
    }

    public Connection getConn() {
        return conn;
    }

    public List<Table> getTables() {
        return tables;
    }

    public Connection connect(String connectionUrl){
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            this.conn=connection;
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

    public void initTables() throws SQLException {
        Statement stmt = conn.createStatement();
        Statement stmt2=conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES");

        while (rs.next()) {
            tables.add(new Table(rs.getString("TABLE_NAME")));
        }


        for(Table e:tables){
            e.initColumns(this.conn);
        }

    }

    public void listTables() {
        for (int i=0;i<tables.size();i++){
            System.out.println((i+1)+". "+tables.get(i).getName());
        }
    }

    public StringBuilder findViaId(int wantedId, int tableIndex) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM "+tables.get(tableIndex).getName()+" WHERE id=?");
        stmt.setInt(1, wantedId);
        ResultSet rs = stmt.executeQuery();

        StringBuilder tmp = new StringBuilder();

        while (rs.next()){
            for(int i=0;i<tables.get(tableIndex).getColumns().size();i++){
                tmp.append(tables.get(tableIndex).getColumns().get(i).getName()+": "+rs.getString(tables.get(tableIndex).getColumns().get(i).getName())+"\n");
            }
        }
        return tmp;
    }

    public StringBuilder listTable(int tableIndex) throws SQLException {
        StringBuilder tmp = new StringBuilder();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM "+tables.get(tableIndex).getName());
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            for(int i=0;i<tables.get(tableIndex).getColumns().size();i++){
                tmp.append((i+1)+". "+tables.get(tableIndex).getColumns().get(i).getName()+": "+rs.getString(tables.get(tableIndex).getColumns().get(i).getName())+"\n");
            }
            tmp.append("\n");
        }
        return tmp;
    }

    public void pridejZaznam(int tableIndex) throws SQLException {
        int size = tables.get(tableIndex).getColumns().size();
        StringBuilder tmp = new StringBuilder();
        StringBuilder tmp2 = new StringBuilder();

        for(int i=0;i<size;i++){
            tmp.append(tables.get(tableIndex).getColumns().get(i).getName());
            if(i<size-1)tmp.append(", ");
        }

        for(int i=0;i<size;i++){
            tmp2.append("?");
            if(i<size-1)tmp2.append(", ");
        }



        PreparedStatement stmt = conn.prepareStatement("INSERT INTO "+tables.get(tableIndex).getName() + "("+tmp+") VALUES ("+tmp2+")");
        System.out.println("INSERT INTO "+tables.get(tableIndex).getName() + "("+tmp+") VALUES ("+tmp2+")");
        String tmpstr="";
        for(int i=0;i<size;i++){
            String datatype=tables.get(tableIndex).getColumns().get(i).getDatatype();
            System.out.println("Zadej "+tables.get(tableIndex).getColumns().get(i).getName());
            switch (datatype){
                case "nvarchar":
                    while(tmpstr.length()<1){
                        tmpstr=sc.nextLine();
                    }
                    System.out.println(tmpstr);
                    stmt.setString(i+1, tmpstr);
                    tmpstr="";

                    break;
                case "int":
                    stmt.setInt(i+1, sc.nextInt());
                    break;
                case "date":
                    System.out.println("Formát DD/MM/YYYY");
                    Date date;
                    try {
                        date = (Date) new SimpleDateFormat("dd/MM/yyyy").parse((sc.nextLine()));
                    } catch (ParseException e) {
                        System.out.println("Datum zadan ve spatnem formatu!");
                        return;
                    }
                    stmt.setDate(i+1, date);
                    break;
                case "char":
                    stmt.setString(i+1,sc.next());
                    break;
            }
        }

        stmt.executeUpdate();
    }

    public void odstranZaznam(int tableIndex, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM "+tables.get(tableIndex).getName()+" WHERE id=?");
        stmt.setInt(1,id);
        stmt.executeUpdate();
    }
}
