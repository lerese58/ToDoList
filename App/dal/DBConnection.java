package App.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
 * TODO: get user,url,pass from another file/config
 */

public class DBConnection {

    private static volatile DBConnection instance;
    private static String user = "root";
    private static String url = "jdbc:mysql://localhost:3306/todolist?useSSL=false";
    private static String pass = "rootroot";

    private Connection _connection;

    private DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            _connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        DBConnection localInstance = instance;
        if (localInstance == null) {
            synchronized (DBConnection.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DBConnection();
                }
            }
        }
        return localInstance;
    }

    public Connection getConnection() {
        return _connection;
    }
}
