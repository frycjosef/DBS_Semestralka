package ui;

import database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlastikaApp {
    public static Scanner sc = new Scanner (System.in);
    public static void main(String[] args) {
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

        boolean konec = false;
        int volba;

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
                    //ziskat zaznam podle id
                    break;
                case 5:
                    //ziskat vsechny zaznamy
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
