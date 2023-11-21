package carsharing;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Main {
    //  Database credentials
    //static final String USER = "sa";
    //static final String PASS = "";

    public static void main(String[] args) {

        StringBuilder nameGet = new StringBuilder(String.join(" ", args));

        String DB_NAME = nameGet.delete(nameGet.indexOf("-databaseFileName"), nameGet.indexOf(" ")).deleteCharAt(0).toString();

        if (DB_NAME.isBlank()) {
            DB_NAME = "base";
        }

        Scanner scanner = new Scanner(System.in);

        CarSharingDB dataBase = new CarSharingDB(DB_NAME);

        boolean running = true;

        while (running) {
            System.out.println("1. Log in as a manager");
            System.out.println("0. Exit");
            int command = scanner.nextInt();
            if (command == 0) {
                running = false;
            } else if (command == 1) {
                boolean manipulatingTables = true;
                while (manipulatingTables) {
                    System.out.println("1. Company list");
                    System.out.println("2. Create a company");
                    System.out.println("0. Back");
                    int operation = scanner.nextInt();
                    if (operation == 1) {
                        //System.out.println("printing company list...");
                        dataBase.printTable();
                        //System.out.println("companies printed...");
                    } else if (operation == 2) {
                       // System.out.println("creating new company...");
                        System.out.println("Enter the company name: ");
                        String newCompName = scanner.nextLine();
                        while (newCompName.isBlank()) {
                            newCompName = scanner.nextLine();
                        }
                        dataBase.newCompany(newCompName);
                        //System.out.println("New company created");
                    } else if (operation == 0) {
                        manipulatingTables = false;
                    }
                }
            }
        }

    }
}