package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CarSharingCustomers extends CarSharingDB implements CarSharingCustomersDAO {


    public CarSharingCustomers(String name) {
        super(name);
        createCustomerTable();
    }

    void createCustomerTable() {
        try {
            connect();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CUSTOMER(" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(30) UNIQUE NOT NULL," +
                    "RENTED_CAR_ID INT DEFAULT NULL," +
                    "CONSTRAINT fk_car_id FOREIGN KEY (RENTED_CAR_ID)" +
                    "REFERENCES CAR(ID)" +
                    ");");
            disconnect();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public boolean printCustomers() {
        connect();
        String intro = "Customer List: ";
        boolean result = false;
        try {
            ResultSet entities = statement.executeQuery("SELECT * FROM CUSTOMER");
            if (entities.next()) {
                do {
                    System.out.print(intro);
                    intro = "";
                    int id = entities.getInt("ID");
                    String name = entities.getString("NAME");
                    System.out.printf("%n%d. %s", id, name);
                } while (entities.next());
                result = true;
            } else {
                System.out.println("The customer list is empty!");
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return result;
    }

    @Override
    public void addCustomer(String newCustomerName) {
        try {
            connect();
            int lastID = 0;
            ResultSet customers = statement.executeQuery("SELECT * FROM CUSTOMER");
            while (customers.next()) {
                lastID = customers.getInt("ID");
            }
            statement.executeUpdate("INSERT INTO CUSTOMER (NAME) VALUES" +
                    "('" + newCustomerName + "')");
            customers.close();
            disconnect();
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rentCar(int customerID, int carID) {
        connect();
        try{
            ResultSet checker = statement.executeQuery("SELECT RENTED_CAR_ID FROM CUSTOMER " +
                    "WHERE ID = " + customerID);
            if (checker.next()) {
                statement.executeUpdate("UPDATE CUSTOMER " +
                        "SET RENTED_CAR_ID = " + carID + " " +
                        "WHERE ID = " + customerID + ";" +
                        "UPDATE CAR " +
                        "SET RENTED = TRUE WHERE ID = " + carID + ";");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        disconnect();
    }

    @Override
    public void checkRentalStatus(int customerID) {
        connect();
        try {
            ResultSet checker = statement.executeQuery("SELECT RENTED_CAR_ID FROM CUSTOMER " +
                    "WHERE ID = " + customerID);
            if (!checker.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                int carID = checker.getInt("RENTED_CAR_ID");
                ResultSet car = statement.executeQuery("SELECT * FROM CAR " +
                        "WHERE ID = "+carID+";");
                if (car.next()) {
                    System.out.println("Your rented car: ");
                    String name = car.getString("NAME");
                    int compID = car.getInt("COMPANY_ID");
                    System.out.println(name);
                    ResultSet company = statement.executeQuery("SELECT * FROM COMPANY " +
                            "WHERE ID = " + compID + ";");
                    if (company.next()) {
                        String compName = company.getString("NAME");
                        System.out.println("Company:");
                        System.out.println(compName);
                    }
                } else {
                    System.out.println("You didn't rent a car!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void returnCar(int customerID) {
        connect();
        try {
            ResultSet checker = statement.executeQuery("SELECT RENTED_CAR_ID FROM CUSTOMER " +
                    "WHERE ID = " + customerID);
            if (!checker.next() || checker.getInt("RENTED_CAR_ID") == 0) {
                System.out.println("You didn't rent a car!");
            } else {
                statement.executeUpdate("UPDATE CAR " +
                        "SET RENTED = FALSE " +
                        "WHERE ID = " + checker.getInt("RENTED_CAR_ID"));
                statement.executeUpdate("UPDATE CUSTOMER " +
                        "SET RENTED_CAR_ID = NULL " +
                        "WHERE ID = "+customerID);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        disconnect();
    }

    public boolean rentAvailable(int customerID) {
        boolean result = true;
        connect();
        try {
            ResultSet checker = statement.executeQuery("SELECT RENTED_CAR_ID FROM CUSTOMER " +
                    "WHERE ID = " + customerID);
            if (checker.next() && !(checker.getInt("RENTED_CAR_ID") == 0)) {
                System.out.println("You've already rented a car!");
                result = false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        disconnect();
        return result;
    }
}
