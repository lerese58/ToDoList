package App.dal;

import net.ucanaccess.jdbc.UcanaccessDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static volatile DBConnection instance;
    private Connection _connection;
    private String db = "Database41.accdb";

    private DBConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            _connection = DriverManager.getConnection(UcanaccessDriver.URL_PREFIX + db);
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
