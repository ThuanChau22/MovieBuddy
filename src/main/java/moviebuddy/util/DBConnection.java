package moviebuddy.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private DBConnection() {
    }

    // Open a connection
    public static Connection connect() throws IOException, ClassNotFoundException, SQLException {
        Connection conn = null;

        // Get current path to .properties file
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll("%20", " ");
        String dbConfigPath = rootPath + "db.properties";

        // Get database credential from .properties file
        Properties props = new Properties();
        props.load(new FileInputStream(dbConfigPath));
        Class.forName(props.getProperty("driver"));
        String endpoint = props.getProperty("endpoint");
        String port = props.getProperty("port");
        String dbname = props.getProperty("dbname");
        String url = "jdbc:mysql://" + endpoint + ":" + port + "/" + dbname;
        String user = props.getProperty("user");
        String pass = props.getProperty("pass");
        conn = DriverManager.getConnection(url, user, pass);
        return conn;
    }

    // Close a statement
    public static void close(PreparedStatement st) throws SQLException {
        if (st != null) {
            st.close();
        }
    }

    // Close a connection
    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}