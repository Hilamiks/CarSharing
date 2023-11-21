package carsharing;

import java.sql.*;

public class CarSharingDB implements CompanyDAO {
    private final String JDBC_DRIVER = "org.h2.Driver";

    private Connection connection = null;

    private Statement statement = null;

    private static String DB_URL;

    public CarSharingDB (String DB_NAME) {
        try {
            DB_URL = "jdbc:h2:./src/carsharing/db/"+DB_NAME;
            Class.forName(JDBC_DRIVER);
            createTable();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable() {
        try {
            connect();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY(" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(30) UNIQUE NOT NULL" +
                    ");");
            disconnect();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void clear() {
        try {
            connect();
            statement.executeUpdate("DROP TABLE COMPANY");
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            //System.out.println("connect to db");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    void disconnect() {
        try {
            statement.close();
            connection.close();
            //System.out.println("close the db");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    void quit() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void printTable() {
        connect();
        String intro = "Companies List: ";
        try {
            ResultSet companies = statement.executeQuery("SELECT * FROM COMPANY");
            if (companies.next()) {
                do {
                    System.out.print(intro);
                    intro = "";
                    int id = companies.getInt("ID");
                    String name = companies.getString("NAME");
                    System.out.printf("%n%d. %s", id, name);
                } while (companies.next());
            } else {
                System.out.println("The company list is empty!");
            }
            System.out.println("\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void newCompany(String newCompName) {
        try {
            connect();
            int lastID = 0;
            ResultSet companies = statement.executeQuery("SELECT * FROM COMPANY");
            while (companies.next()) {
                lastID = companies.getInt("ID");
            }
            statement.executeUpdate("INSERT INTO COMPANY (NAME) VALUES" +
                    "('" + newCompName + "')");
            companies.close();
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
