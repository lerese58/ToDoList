package App.dal;

import java.sql.*;
import java.util.ArrayList;

public class UserRepoDB implements Repository<DBUser> {

    private DBConnection _dbConnection;
    private Connection _conn;

    @Override
    public ArrayList<DBUser> getAll() {
        _dbConnection = DBConnection.getInstance();
        _conn = _dbConnection.getConnection();
        try {
            Statement statement = _conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");
            ArrayList<DBUser> allUsers = new ArrayList<>();
            while (resultSet.next())
                allUsers.add(parseResultSet(resultSet));
            return allUsers;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DBUser getById(long id) {
        _dbConnection = DBConnection.getInstance();
        _conn = _dbConnection.getConnection();
        try {
            Statement getByIdStatement = _conn.createStatement();
            ResultSet userById = getByIdStatement.executeQuery("SELECT * FROM Users WHERE Users.ID = " + id);
            return parseResultSet(userById);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean removeById(long id) {
        _dbConnection = DBConnection.getInstance();
        _conn = _dbConnection.getConnection();
        try {
            Statement statement = _conn.createStatement();
            statement.executeUpdate("DELETE * FROM Users WHERE Users.ID = " + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //@Override
    //public boolean remove(DBUser dbUser) {
    //    return removeById(dbUser.getId());
    //}

    @Override
    public boolean update(long ID, DBUser dbUser) {
        _dbConnection = DBConnection.getInstance();
        PreparedStatement statement;
        try {
            if (getById(ID) == null) {
                statement = _conn.prepareStatement("INSERT into Users VALUES (?,?,?,?,?)");
                statement.setLong(1, dbUser.getId());
                statement.setString(2, dbUser.getName());
                statement.setString(3, dbUser.getLogin());
                statement.setString(4, dbUser.getPassword());
                statement.setBoolean(5, dbUser.isReadyToOrder());
                statement.executeUpdate();
                return true;
            } else {
                statement = _conn.prepareStatement("UPDATE Users SET userName=?,login=?,password=?,isReadyToOrder=? WHERE Users.ID=?");
                statement.setString(1, dbUser.getName());
                statement.setString(2, dbUser.getLogin());
                statement.setString(3, dbUser.getPassword());
                statement.setBoolean(4, dbUser.isReadyToOrder());
                statement.setLong(5, ID);
                statement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private DBUser parseResultSet(ResultSet resultSet) {
        try {
            long ID = resultSet.getLong("ID");
            String name = resultSet.getString("userName");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            boolean isReadyToOrder = resultSet.getBoolean("isReadyToOrder");
            return new DBUser(ID, name, login, password, isReadyToOrder);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

