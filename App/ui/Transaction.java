package App.ui;

import App.dal.DBConnection;

import java.sql.SQLException;

public class Transaction {

    private static DBConnection _conn = DBConnection.getInstance();

    public static void run() {
        try {
            _conn.getConnection().setAutoCommit(false);
            _conn.getConnection().commit();
        } catch (SQLException e) {
            try {
                _conn.getConnection().rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }
    }
}
