package ui;

import database.Database;
import database.Table;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlastikaApp {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        /*Connect database*/
        String connectionUrl
                = "jdbc:sqlserver://147.230.21.34:1433;"
                + "database=DBS2020_JosefFryc;"
                + "user=student;"
                + "password=student;";
        String dbsName = "plastika";

        Database database = new Database(dbsName);
        Connection conn = database.connect(connectionUrl);
        if (conn != null) {
            System.out.println("Databáze připojena");
        } else {
            System.out.println("Připojení databáze neproběhlo úspěšně");
            return;
        }
        /**
         * ****************************************
         */

        /*Zjištění existujících tabulek*/
        database.initTables();
        /**
         * ******************************
         */

        //Vypis tabulek a sloupcu v nich
        for (Table e : database.getTables()) {
            System.out.println();
            System.out.println(e.getName().toUpperCase());
            for (int i = 0; i < e.getColumns().size(); i++) {
                System.out.println(e.getColumns().get(i).getName() + " " + e.getColumns().get(i).getDatatype());
            }
        }

        boolean konec = false;
        int volba;
        int volba2;

        while (!konec) {
            mainMenu();
            volba = nacistVolbu();
            switch (volba) {
                case 1:
                    //pridat zaznam
                    vyberTabulku(database);
                    volba2 = nacistVolbu();
                    if (volba2 > database.getTables().size()) {
                        System.out.println("Neplatná volba");
                    }
                    if (volba2 != 0) {
                        database.pridejZaznam(volba2 - 1);
                    }
                    break;
                case 2:
                    //editovat zaznam
                    vyberTabulku(database);
                    volba2 = nacistVolbu();
                    if (volba2 > database.getTables().size()) {
                        System.out.println("Neplatná volba");
                    }

                    if (volba2 != 0) {
                        System.out.println("Zadej id záznamu, který má být upraven");
                        System.out.println("0. Zpět");
                        volba = nacistVolbu();
                        if (volba > 0) {
                            System.out.println(database.findViaId(volba, volba2 - 1));
                            System.out.println();
                            System.out.println("Vyber atribut, který má být upraven");
                            System.out.println(database.getTables().get(volba2 - 1).getColumnNames(true).toString().replace(" ", "\n"));
                            int atribut = nacistVolbu();
                            if (atribut >= 1) {
                                database.editZaznam(volba2 - 1, volba, atribut);
                            } else {
                                System.out.println("Neplatná volba");
                            }
                        } else {
                            System.out.println("Neplatná volba");
                        }
                    }
                    break;
                case 3:
                    //odstranit záznam
                    vyberTabulku(database);
                    volba2 = nacistVolbu();
                    if (volba2 > database.getTables().size()) {
                        System.out.println("Neplatná volba");
                    }
                    if (volba2 != 0) {
                        System.out.println("Zadej id záznamu, který má být smazán");
                        System.out.println("0. Zpět");
                        volba = nacistVolbu();
                        if (volba > 0) {
                            database.odstranZaznam(volba2 - 1, volba);
                        }
                        if (volba < 0) {
                            System.out.println("Neplatná volba");
                        }
                    }
                    break;
                case 4:
                    //získat záznam podle id
                    vyberTabulku(database);
                    volba2 = nacistVolbu();
                    if (volba2 > database.getTables().size()) {
                        System.out.println("Neplatná volba");
                    }
                    if (volba2 != 0) {
                        System.out.println("Zadej id");
                        System.out.println(database.findViaId(sc.nextInt(), volba2 - 1));
                    }
                    break;
                case 5:
                    //získat všechny záznamy z dané tabulky
                    vyberTabulku(database);
                    volba2 = nacistVolbu();
                    if (volba2 > database.getTables().size()) {
                        System.out.println("Neplatná volba");
                    }
                    if (volba2 != 0) {
                        System.out.println(database.listTable(volba2 - 1, false));
                    }
                    break;
                case 6:
                    sqlMenu();
                    volba2 = nacistVolbu();
                    switch(volba2){
                        case 1:
                            System.out.println(database.PojistovnySPacienty());
                            break;
                        case 2:
                            System.out.println(database.pocetProcedurDoktoru());
                            break;
                        case 3:
                            System.out.println(database.pocetZpravPacientu());
                            break;
                        case 4:
                            System.out.println(database.klinikaPocetProcedur());
                            break;
                        case 5:
                            System.out.println(database.pacientiBezProcedur());
                            break;
                        case 6:
                            System.out.println(database.pohlaviPacientu());
                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("Neplatna volba");
                    }
                    break;
                case 0:
                    konec = true;
                    break;
                default:
                    System.out.println("Neplatna volba");
            }
        }

    }

    public static void vyberTabulku(Database database) {
        System.out.println();
        System.out.println("Vyber tabulku");
        database.listTables();
        System.out.println("0. Zpět");
    }

    public static void mainMenu() {
        System.out.println();
        System.out.println("Hlavní menu");
        System.out.println("1.Přidat záznam");
        System.out.println("2.Editovat záznam");
        System.out.println("3.Odstranit záznam");
        System.out.println("4.Získat záznam podle id");
        System.out.println("5.Získat všechny záznamy");
        System.out.println("6.SQL dotazy");
        System.out.println("0.Konec");
        System.out.println();
    }

    private static int nacistVolbu() {
        int volba = -1;
        System.out.print("Zadej zvolenou polozku menu: ");
        try {
            volba = sc.nextInt();
        } catch (InputMismatchException e) {
            // neplatna volba
            volba = -1;
        } finally {
            sc.nextLine();
        }
        return volba;
    }
    
    public static void sqlMenu(){
        System.out.println();
        System.out.println("SQL dotazy:");
        System.out.println("1. Vypis pojistovny, ktere maji pacienty v nemocnicni databazi (SELECT in WHERE)");
        System.out.println("2. Vypis doktoru a poctu procedur, ktere provedli (LEFT JOIN, GROUP BY)");
        System.out.println("3. Vypis pacientu a poctu zprav, ktere jim byly vystaveny (SELECT in SELECT)");
        System.out.println("4. Vypis klinik a poctu procedur, ktere se na nich byly provedeny (LEFT JOIN, RIGHT JOIN, GROUP BY, COUNT)");
        System.out.println("5. Vypiš všechny pacienty, kteri jeste nemeli proceduru");
        System.out.println("6. Vypis rozdeleni pohlavi mezi pacienty");
        System.out.println("0. Zpet");
        System.out.println();
    }
}
