package App.dal;

import App.model.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepoDB implements Repository<UserDTO> {

    private Connection _conn = DBConnection.getInstance().getConnection();

    @Override
    public List<UserDTO> getAll() {
        try {
            Statement statement = _conn.createStatement();
            ResultSet setOfUsers = statement.executeQuery("SELECT * FROM Users");
            List<UserDTO> allUsers = new ArrayList<>();
            if (setOfUsers.next()) {
                do {
                    allUsers.add(parseResultSet(setOfUsers));
                }
                while (setOfUsers.next());
            }
            return allUsers;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UserDTO> getList(long id) {
        try {
            Statement statement = _conn.createStatement();
            ResultSet setOfUsers = statement.executeQuery("select UserID from task_userlist where TaskID = " + id);
            List<UserDTO> allUsers = new ArrayList<>();
            if (setOfUsers.next()) {
                do {
                    allUsers.add(getById(setOfUsers.getLong("UserID")));
                }
                while (setOfUsers.next());
            }
            return allUsers;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long getCount() {
        try {
            Statement count = _conn.createStatement();
            ResultSet getCount = count.executeQuery("select COUNT(ID) from users;");
            return getCount.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public boolean create(UserDTO userDTO) {
        try {
            PreparedStatement statement = _conn.prepareStatement("INSERT into Users VALUES (?,?,?,?,?)");
            statement.setLong(1, userDTO.getId());
            statement.setString(2, userDTO.getName());
            statement.setString(3, userDTO.getLogin());
            statement.setString(4, userDTO.getPassword());
            statement.setBoolean(5, userDTO.isReadyToOrder());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public UserDTO getById(long id) {
        try {
            Statement getByIdStatement = _conn.createStatement();
            ResultSet userById = getByIdStatement.executeQuery("SELECT * FROM Users WHERE ID = " + id);
            if (!userById.next())
                return null;
            return parseResultSet(userById);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(long ID, UserDTO userDTO) {
        try {
            PreparedStatement statement = _conn.prepareStatement("UPDATE Users SET userName=?,login=?,password=?,isReadyToOrder=? WHERE Users.ID=?");
            statement.setString(1, userDTO.getName());
            statement.setString(2, userDTO.getLogin());
            statement.setString(3, userDTO.getPassword());
            statement.setBoolean(4, userDTO.isReadyToOrder());
            statement.setLong(5, ID);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeById(long id) {
        try {
            _conn.setAutoCommit(false);
            Statement removeUser = _conn.createStatement();
            Statement removeExecutor = _conn.createStatement();
            removeUser.executeUpdate("DELETE * FROM Users WHERE ID = " + id);
            removeExecutor.executeUpdate("delete * from task_userlist where UserID = " + id);
            _conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                _conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    private UserDTO parseResultSet(ResultSet resultSet) {
        try {
            long ID = resultSet.getLong("ID");
            String name = resultSet.getString("userName");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            boolean isReadyToOrder = resultSet.getBoolean("isReadyToOrder");
            return new UserDTO(ID, name, login, password, isReadyToOrder);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

