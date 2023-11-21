package carsharing;

import java.sql.*;

public class CarSharingCompanies implements CarSharingDAO {
    private final String JDBC_DRIVER = "org.h2.Driver";

    Connection connection = null;

    Statement statement = null;

    static String DB_URL;

    public CarSharingCompanies (String DB_NAME) {
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
            statement.executeUpdate("DROP TABLE COMPANY;" +
                    "DROP TABLE CAR;");
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

    public boolean printTable() {
        connect();
        String intro = "Choose a company: ";
        boolean hasData = false;
        try {
            ResultSet entities = statement.executeQuery("SELECT * FROM COMPANY");
            if (entities.next()) {
                do {
                    System.out.print(intro);
                    intro = "";
                    int id = entities.getInt("ID");
                    String name = entities.getString("NAME");
                    System.out.printf("%n%d. %s", id, name);
                } while (entities.next());
                hasData = true;
            } else {
                System.out.println("The company list is empty!");
                hasData = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return hasData;
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

    public String getNameFromID(int id) {
        connect();
        String name = "";
        try {
            ResultSet entities = statement.executeQuery("SELECT * FROM COMPANY " +
                    "WHERE ID = " + id);
            if (entities.next()) {
                name = entities.getString("NAME");
            }else {
                System.out.println("No Such Company");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return name;
    }
}
