package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CarSharingDB {
    final String JDBC_DRIVER = "org.h2.Driver";

    Connection connection = null;

    Statement statement = null;

    static String DB_URL;

    public CarSharingDB (String DB_NAME) {
        try {
            DB_URL = "jdbc:h2:./src/carsharing/db/"+DB_NAME;
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
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
}
