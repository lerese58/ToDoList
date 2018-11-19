package App.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static volatile DBConnection instance;

    private Connection _connection;

    private DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String user = "root";
            String url = "jdbc:mysql://localhost:3306/todolist?useSSL=false";
            String pass = "rootroot";
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
