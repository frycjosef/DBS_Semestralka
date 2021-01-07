package database;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Database {

    private String name;
    private Connection conn;
    private List<Table> tables;
    private Scanner sc = new Scanner(System.in);

    public Database(String name) {
        this.name = name;
        this.conn = null;
        this.tables = new ArrayList<>();
    }

    public Connection getConn() {
        return conn;
    }

    public List<Table> getTables() {
        return tables;
    }

    public Connection connect(String connectionUrl) {
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            this.conn = connection;
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void addTable(String tableName) {
        tables.add(new Table(tableName));
    }

    /***
     * Zjistí tabulky z databáze a uloží je do seznamu
     * @throws SQLException
     */
    public void initTables() throws SQLException {
        Statement stmt = conn.createStatement();
        Statement stmt2 = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES");

        while (rs.next()) {
            tables.add(new Table(rs.getString("TABLE_NAME")));
        }

        for (Table e : tables) {
            e.initColumns(this.conn);
        }

    }

    /***
     * Vypíše všechny tabulky ze seznamu
     */
    public void listTables() {
        for (int i = 0; i < tables.size(); i++) {
            System.out.println((i + 1) + ". " + tables.get(i).getName());
        }
    }

    /***
     *
     * @param wantedId  Index položky, která má být nalezena
     * @param tableIndex index tabulky v seznamu
     * @return  StringBuilder pro výpis záznamu
     * @throws SQLException
     */
    public StringBuilder findViaId(int wantedId, int tableIndex) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tables.get(tableIndex).getName() + " WHERE id=?");
        stmt.setInt(1, wantedId);
        ResultSet rs = stmt.executeQuery();

        StringBuilder tmp = new StringBuilder();

        while (rs.next()) {
            for (int i = 0; i < tables.get(tableIndex).getColumns().size(); i++) {
                tmp.append(tables.get(tableIndex).getColumns().get(i).getName() + ": " + rs.getString(tables.get(tableIndex).getColumns().get(i).getName()) + "\n");
            }
        }
        return tmp;
    }

    /***
     *
     * @param tableIndex index tabulky v seznamu
     * @param cisla true = vypsání pořadí 1.,2.,3.,...
     * @return StringBuilder s výpisem tabulky
     * @throws SQLException
     */
    public StringBuilder listTable(int tableIndex, boolean cisla) throws SQLException {
        StringBuilder tmp = new StringBuilder();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tables.get(tableIndex).getName());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            for (int i = 0; i < tables.get(tableIndex).getColumns().size(); i++) {
                if (cisla) {
                    tmp.append((i + 1) + ". ");
                }
                tmp.append(tables.get(tableIndex).getColumns().get(i).getName() + ": " + rs.getString(tables.get(tableIndex).getColumns().get(i).getName()) + "\n");
            }
            tmp.append("\n");
        }
        return tmp;
    }

    /***
     * Přidá záznam do vybrané tabulky
     * @param tableIndex index tabulky v seznamu
     * @throws SQLException
     */
    public void pridejZaznam(int tableIndex) throws SQLException {
        int size = tables.get(tableIndex).getColumns().size();
        StringBuilder tmp = new StringBuilder();
        StringBuilder tmp2 = new StringBuilder();

        for (int i = 0; i < size; i++) {
            tmp.append(tables.get(tableIndex).getColumns().get(i).getName());
            if (i < size - 1) {
                tmp.append(", ");
            }
        }

        for (int i = 0; i < size; i++) {
            tmp2.append("?");
            if (i < size - 1) {
                tmp2.append(", ");
            }
        }

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tables.get(tableIndex).getName() + "(" + tmp + ") VALUES (" + tmp2 + ")");
        System.out.println("INSERT INTO " + tables.get(tableIndex).getName() + "(" + tmp + ") VALUES (" + tmp2 + ")");
        String tmpstr = "";
        for (int i = 0; i < size; i++) {
            String datatype = tables.get(tableIndex).getColumns().get(i).getDatatype();
            System.out.println("Zadej " + tables.get(tableIndex).getColumns().get(i).getName());
            switch (datatype) {
                case "nvarchar":
                    while (tmpstr.length() < 1) {
                        tmpstr = sc.nextLine();
                    }
                    System.out.println(tmpstr);
                    stmt.setString(i + 1, tmpstr);
                    tmpstr = "";

                    break;
                case "int":
                    try {
                        stmt.setInt(i + 1, sc.nextInt());
                    } catch (InputMismatchException e) {
                        System.out.println("Zadán špatný formát");
                        return;
                    }
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
                    stmt.setDate(i + 1, date);
                    break;
                case "char":
                    tmpstr = sc.next();
                    if (tmpstr.toUpperCase().equals("Z") || tmpstr.toUpperCase().equals("M")) {
                        stmt.setString(i + 1, tmpstr);
                    } else {
                        System.out.println("Zadane pohlavi neexistuje");
                        return;
                    }
                    break;
            }
        }

        stmt.executeUpdate();
    }

    public void odstranZaznam(int tableIndex, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tables.get(tableIndex).getName() + " WHERE id=?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void editZaznam(int tableIndex, int id, int atribut) throws SQLException {
        System.out.println("Zadej nové " + tables.get(tableIndex).getColumns().get(atribut - 1).getName());
        String tmp = sc.nextLine();
        PreparedStatement stmt = conn.prepareStatement("UPDATE " + tables.get(tableIndex).getName() + " SET " 
                                + tables.get(tableIndex).getColumns().get(atribut - 1).getName() + "='" + tmp + "' WHERE id=?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
    
    /**
     * Vypise vsechny pojistovny, ktere maji klienta registrovaneho v nemocnici.
     * @throws SQLException 
     */
    public StringBuilder PojistovnySPacienty() throws SQLException {
        StringBuilder output=new StringBuilder();
        PreparedStatement stmt = conn.prepareStatement("SELECT Zdravotni_Pojistovna.Nazev "
                + "FROM Zdravotni_Pojistovna WHERE Zdravotni_Pojistovna.kod "
                + "IN (SELECT Pacient.Zdravotni_Pojistovna_kod FROM Pacient) "
                + "ORDER BY Zdravotni_Pojistovna.Nazev");
        ResultSet rs = stmt.executeQuery();

        output.append("\n");
        output.append("Pojistovny\n");
        output.append("-------------------------------\n");
        
        while (rs.next()) {
            output.append(rs.getString("Nazev")+"\n");
        }
        return output;
    }
    
    /**
     * Vypise seznam doktoru a pocet jimi provedenych procedur.
     * @throws SQLException 
     */
    public StringBuilder pocetProcedurDoktoru() throws SQLException {
        StringBuilder output = new StringBuilder();
        PreparedStatement stmt = conn.prepareStatement("SELECT Doktor.Prijmeni, "
                + "COUNT(Doktor_Procedura.Doktor_id) FROM Doktor LEFT JOIN Doktor_Procedura ON "
                + "(Doktor_Procedura.Doktor_id = Doktor.id) GROUP BY Doktor.Prijmeni "
                + "ORDER BY Doktor.Prijmeni");
        ResultSet rs = stmt.executeQuery();

        output.append("\n");
        output.append("Prijmeni     |Pocet provedenych procedur\n");
        output.append("-------------|--------------------------\n");
        
        while (rs.next()) {
            String Prijmeni = rs.getString(1);
            String pocetProcedur = rs.getString(2);
            output.append(String.format("%-13s %s\n", Prijmeni, pocetProcedur));
        }
        return output;
    }
    
    /**
     * Vypise seznam pacientu a pocet vystavenych zprav pro kazdeho z nich.
     * @throws SQLException 
     */
    public StringBuilder pocetZpravPacientu() throws SQLException {
        StringBuilder output = new StringBuilder();
        PreparedStatement stmt = conn.prepareStatement("SELECT Pacient.Prijmeni, "
                + "ZpravaCount = (SELECT COUNT(Zprava.id) FROM Zprava WHERE Pacient.id = Zprava.Pacient_id) "
                + "FROM Pacient ORDER BY Pacient.Prijmeni");
        ResultSet rs = stmt.executeQuery();

        output.append("\n");
        output.append("Prijmeni     |Pocet vystavenych zprav\n");
        output.append("-------------|-----------------------\n");
        
        while (rs.next()) {
            String Prijmeni = rs.getString(1);
            String pocetZprav = rs.getString(2);
            output.append(String.format("%-13s %s\n", Prijmeni, pocetZprav));
        }
        return output;
    }

    public StringBuilder klinikaPocetProcedur() throws SQLException {
        StringBuilder output = new StringBuilder();
        PreparedStatement stmt = conn.prepareStatement("SELECT B.nazev,pocet FROM (\n" +
                "\t\tSELECT COUNT (A.Doktor_id)as pocet,Klinika_id FROM Doktor LEFT JOIN (\n" +
                "\t\tSELECT Doktor_id FROM Zprava RIGHT JOIN Doktor_Procedura ON(Zprava.Doktor_Procedura_id = Doktor_Procedura.id)\n" +
                "\t\t) A ON (A.Doktor_id=Doktor.id) GROUP BY Doktor.Klinika_id\n" +
                "\t\t) A RIGHT JOIN Klinika B ON (B.id= A.Klinika_id)");
        ResultSet rs = stmt.executeQuery();

        output.append("\n");
        output.append("Název               |Pocet procedur\n");
        output.append("--------------------|-----------------------\n");

        while (rs.next()) {
            String Nazev = rs.getString(1);
            String pocetProcedur = rs.getString(2);
            output.append(String.format("%-20s %s\n", Nazev, pocetProcedur));
        }
        return output;
    }

    public StringBuilder pacientiBezProcedur() throws SQLException {
        StringBuilder output = new StringBuilder();
        PreparedStatement stmt = conn.prepareStatement("SELECT A.jmeno, A.Prijmeni FROM Pacient A,(SELECT Pacient.id FROM Pacient EXCEPT SELECT Pacient_id FROM Zprava)B WHERE A.id = B.id");
        ResultSet rs = stmt.executeQuery();

        output.append("\n");
        output.append("Jméno a přijmení    \n");
        output.append("------------------\n");

        while (rs.next()) {
            String Jmeno = rs.getString(1);
            output.append(Jmeno);
        }
        return output;
    }

    public StringBuilder pohlaviPacientu() throws SQLException {
        StringBuilder output = new StringBuilder();
        PreparedStatement stmt = conn.prepareStatement("SELECT A.pocetM, B.pocetZ FROM \n" +
                "\t\t(SELECT COUNT(id) as pocetM FROM Pacient WHERE (Pohlavi='M'))A LEFT JOIN\n" +
                "\t\t(SELECT COUNT(id) as pocetZ FROM Pacient WHERE (Pohlavi='Z')) B ON (A.pocetM>=0 OR B.pocetZ>=0)");
        ResultSet rs = stmt.executeQuery();

        output.append("\n");
        output.append("Pocet muzu  | Pocet zen    \n");
        output.append("--------------------------\n");

        while (rs.next()) {
            String muzi = rs.getString(1);
            String zeny = rs.getString(2);
            output.append(String.format("%-13s %s\n", muzi, zeny));
        }
        return output;
    }


}
