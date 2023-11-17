package carsharing;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    //  Database credentials
    //static final String USER = "sa";
    //static final String PASS = "";

    public static void main(String[] args) {
        // JDBC driver name and database URL
        StringBuilder nameGet = new StringBuilder(String.join(" ", args));
        String DB_NAME = nameGet.delete(nameGet.indexOf("-databaseFileName"), nameGet.indexOf(" ")).deleteCharAt(0).toString();
        if (DB_NAME.isBlank()) {
            DB_NAME = "base";
        }
        File file = new File("./src/carsharing/db/"+DB_NAME);
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:./src/carsharing/db/"+DB_NAME;

        try {
            //register database driver
            Class.forName(JDBC_DRIVER);

            //open connection
            System.out.println("Connecting to the database...");
            Connection con = DriverManager.getConnection(DB_URL);
            con.setAutoCommit(true);
            System.out.println("Creating statement...");

            //executing query
            Statement st = con.createStatement();
            String sql = "CREATE TABLE COMPANY (" +
                    "ID INT, " +
                    "NAME VARCHAR(50)" +
                    ");";
            System.out.println("created: "+sql);
            st.executeUpdate(sql);
            System.out.println("Database created");

            //cleaning up
            con.close();
        } catch (Exception ignored) {

        }
    }
}