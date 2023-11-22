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

        CarSharingCompanies companiesDB = new CarSharingCompanies(DB_NAME);
        CarSharingCars carsDB = new CarSharingCars(DB_NAME);
        CarSharingCustomers customersDB = new CarSharingCustomers(DB_NAME);

        boolean running = true;

        while (running) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
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
                        if (companiesDB.printTable()) {
                            System.out.println("\n0. Back");
                            int chosenCompany = scanner.nextInt();
                            boolean manipulatingCompany = true;
                            while (manipulatingCompany) {
                                if (chosenCompany != 0) {
                                    String compName = companiesDB.getNameFromID(chosenCompany);
                                    System.out.println(compName + " company:");
                                    System.out.println("1. Car list");
                                    System.out.println("2. Create a car");
                                    System.out.println("0. Back");
                                    int companyOp = scanner.nextInt();
                                    if (companyOp == 1) {
                                        carsDB.printTable(chosenCompany);
                                    } else if (companyOp == 2) {
                                        System.out.println("Enter the car name:");
                                        String carName = scanner.nextLine();
                                        while (carName.isBlank()) {
                                            carName = scanner.nextLine();
                                        }
                                        carsDB.newCar(carName, chosenCompany);
                                    } else {
                                        manipulatingCompany = false;
                                    }
                                } else {
                                    manipulatingCompany = false;
                                }
                            }
                        }
                        //System.out.println("companies printed...");
                    } else if (operation == 2) {
                       // System.out.println("creating new company...");
                        System.out.println("Enter the company name: ");
                        String newCompName = scanner.nextLine();
                        while (newCompName.isBlank()) {
                            newCompName = scanner.nextLine();
                        }
                        companiesDB.newCompany(newCompName);
                        //System.out.println("New company created");
                    } else if (operation == 0) {
                        manipulatingTables = false;
                    }
                }
            } else if (command == 2) {
                while (customersDB.printCustomers()) {
                    System.out.println("\n0. Back");
                    int customerID = scanner.nextInt();
                    if (customerID == 0) {
                        break;
                    } else {
                        boolean customerOps = true;
                        while (customerOps) {
                            System.out.println("1. Rent a car");
                            System.out.println("2. Return a rented car");
                            System.out.println("3. My rented car");
                            System.out.println("0. Back");
                            int customerCommand = scanner.nextInt();
                            if (customerCommand == 0) {
                                customerOps = false;
                            } else if (customerCommand == 1) {
                                if (customersDB.rentAvailable(customerID)) {
                                    if (companiesDB.printTable()) {
                                        System.out.println("\n0. Back");
                                        int chosenComp = scanner.nextInt();
                                        if (chosenComp == 0) {

                                        } else if (carsDB.printTable(chosenComp)) {
                                            System.out.println("\n0. Back");
                                            int carID = scanner.nextInt();
                                            if (carID == 0) {

                                            } else {
                                                customersDB.rentCar(customerID, carID);
                                            }
                                        }
                                    }
                                }
                            } else if (customerCommand == 2) {
                                customersDB.returnCar(customerID);
                            } else if (customerCommand == 3) {
                                customersDB.checkRentalStatus(customerID);
                            }
                        }
                    }
                }
            } else if (command == 3) {
                System.out.println("Enter the customer name:");
                String newCustomerName = scanner.nextLine();
                while (newCustomerName.isBlank()) {
                    newCustomerName = scanner.nextLine();
                }
                customersDB.addCustomer(newCustomerName);
            }
        }

    }
}