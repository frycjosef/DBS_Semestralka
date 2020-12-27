package ui;

import database.Database;
import database.Table;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlastikaApp {
    public static Scanner sc = new Scanner (System.in);
    public static void main(String[] args) throws SQLException {
        /*Connect database*/
        String connectionUrl =
                "jdbc:sqlserver://147.230.21.34:1433;"
                        + "database=DBS2020_JosefFryc;"
                        + "user=student;"
                        + "password=student;";
        String dbsName="plastika";

        Database database= new Database(dbsName);
        Connection conn=database.connect(connectionUrl);
        if(conn!=null){
            System.out.println("Databáze připojena");
        }else {
            System.out.println("Připojení databáze neproběhlo úspěšně");
            return;
        }
        /*******************************************/

        /*Zjištění existujících tabulek*/
        database.initTables();
        /*********************************/

        /* Vypis tabulek a sloupcu v nich
        for (Table e:database.getTables()){
            System.out.println();
            System.out.println(e.getName().toUpperCase());
            for(int i=0;i<e.getColumns().size();i++){
                System.out.println(e.getColumns().get(i));
            }
        }*/



        boolean konec = false;
        int volba;
        int volba2;

        while(!konec){
            mainMenu();
            volba=nacistVolbu();
            switch (volba){
                case 1:
                    //pridat zaznam
                    break;
                case 2:
                    //editovat zaznam
                    break;
                case 3:
                    //odstranit zaznam
                    break;
                case 4:
                    System.out.println();
                    System.out.println("Vyber tabulku");
                    database.listTables();
                    System.out.println("0. Zpět");
                    volba2=nacistVolbu();
                    if(volba2>database.getTables().size()){
                        System.out.println("Neplatná volba");
                    }
                    if(volba2!=0){
                        System.out.println("Zadej id");
                        System.out.println(database.findViaId(sc.nextInt(), volba2-1));
                    }
                    break;
                case 5:
                    System.out.println();
                    System.out.println("Vyber tabulku");
                    database.listTables();
                    System.out.println("0. Zpět");
                    volba2=nacistVolbu();
                    if(volba2>database.getTables().size()){
                        System.out.println("Neplatná volba");
                    }
                    if(volba2!=0){
                        System.out.println(database.listTable(volba2-1));
                    }
                    break;
                case 0:
                    konec=true;
                    break;
                default:
                    System.out.println("Neplatna volba");
            }
        }


    }

    public static void mainMenu(){
        System.out.println();
        System.out.println("Hlavní menu");
        System.out.println("1.Přidat záznam");
        System.out.println("2.Editovat záznam");
        System.out.println("3.Odstranit záznam");
        System.out.println("4.Získat záznam podle id");
        System.out.println("5.Získat všechny záznamy");
        System.out.println("0.Konec");
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
}
