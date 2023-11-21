package carsharing;

import java.sql.*;

public class CarSharingCars extends CarSharingCompanies implements CarSharingCarsDAO {

    public CarSharingCars(String DB_NAME) {
        super(DB_NAME);
        createCarTable();
    }

    public void createCarTable() {
        try {
            connect();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CAR(" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(30) UNIQUE NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "CONSTRAINT fk_id FOREIGN KEY (COMPANY_ID)" +
                    "REFERENCES COMPANY(ID)" +
                    ");");
            disconnect();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void printTable(int compID) {
        connect();
        String intro = "Cars List: ";
        int i = 1;
        try {
            ResultSet entities = statement.executeQuery("SELECT * FROM CAR " +
                    "WHERE COMPANY_ID = "+compID);
            if (entities.next()) {
                do {
                    System.out.print(intro);
                    intro = "";
                    int id = entities.getInt("ID");
                    String name = entities.getString("NAME");
                    System.out.printf("%n%d. %s", i, name);
                    i++;
                } while (entities.next());
            } else {
                System.out.println("The car list is empty!");
            }
            System.out.println("\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void newCar(String newCarName, int compID) {
        try {
            connect();
            int lastID = 0;
            ResultSet entities = statement.executeQuery("SELECT * FROM CAR");
            while (entities.next()) {
                lastID = entities.getInt("ID");
            }
            statement.executeUpdate("INSERT INTO CAR(NAME, COMPANY_ID) VALUES" +
                    "('" + newCarName + "', " + compID + ");");
            entities.close();
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
