package App.dal;

import App.model.TaskCalendar;
import App.utils.Priority;
import App.utils.Status;

import java.sql.*;
import java.util.ArrayList;

public class TaskRepoDB implements Repository<DBTask> {
    private DBConnection _dbConnection;
    private Connection _conn;

    @Override
    public ArrayList<DBTask> getAll() { //TODO: get ALL tasks, mb i need to get only for this user
        _dbConnection = DBConnection.getInstance();
        _conn = _dbConnection.getConnection();
        ArrayList<DBTask> taskList = new ArrayList<>();
        try {
            Statement getAllStatement = _conn.createStatement();
            ResultSet setOfTasks = getAllStatement.executeQuery("SELECT * FROM Tasks");
            while (setOfTasks.next()) {
                taskList.add(parseResultSet(setOfTasks));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return taskList;
    }

    @Override
    public DBTask getById(long id) {
        _dbConnection = DBConnection.getInstance();
        _conn = _dbConnection.getConnection();
        try {
            Statement getByIdStatement = _conn.createStatement();
            ResultSet taskById = getByIdStatement.executeQuery("SELECT * FROM Tasks WHERE ID = " + id);
            return parseResultSet(taskById);
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
            Statement removeStatement = _conn.createStatement();
            removeStatement.executeUpdate("DELETE * FROM Tasks WHERE ID = " + id);//for link (Tasks-Task_UserList) set on 'ON UPDATE CASCADE'
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(long ID, DBTask dbTask) { // TODO: have an idea to do some part of code in BLL
        _dbConnection = DBConnection.getInstance();
        _conn = _dbConnection.getConnection();
        if (getById(ID) == null) { // create task
            try {
                PreparedStatement insertStatement = _conn.prepareStatement("INSERT into Tasks VALUES (?,?,?,?,?,?,?)");
                insertStatement.setLong(1, dbTask.getId());
                insertStatement.setLong(2, dbTask.getOwnerID());
                insertStatement.setString(3, dbTask.getTitle());
                Timestamp deadline = new Timestamp((dbTask.getDeadline().getDateTime().getNano()) / 1000);//TODO: ????
                insertStatement.setTimestamp(4, deadline);
                insertStatement.setBoolean(5, dbTask.isPersonal());
                insertStatement.setString(6, dbTask.getStatus().toString());
                insertStatement.setString(7, dbTask.getPrio().toString());
                insertStatement.executeUpdate(); //for link (Tasks-Task_UserList) set on 'ON UPDATE CASCADE'
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else { // edit task
            try {
                PreparedStatement updateStatement = _conn.prepareStatement("UPDATE Tasks SET ownerID=?,Title=?,Deadline=?,isPersonal=?,Status=?,Priority=?" +
                        " WHERE ID = ?");
                updateStatement.setLong(1, dbTask.getOwnerID());
                updateStatement.setString(2, dbTask.getTitle());
                Timestamp deadline = new Timestamp(dbTask.getDeadline().getDateTime().getNano() / 1000);//TODO: ????
                updateStatement.setTimestamp(3, deadline);
                updateStatement.setBoolean(4, dbTask.isPersonal());
                updateStatement.setString(5, dbTask.getStatus().toString());
                updateStatement.setString(6, dbTask.getPrio().toString());
                updateStatement.setLong(7, ID);
                changeUserList(ID, dbTask);
                updateStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void changeUserList(long ID, DBTask dbTask) {
        try {
            Statement taskUsers = _conn.createStatement();
            ResultSet usersTask = taskUsers.executeQuery("SELECT UserID FROM Task_UserList WHERE TaskID = " + ID);
            ArrayList<Long> dbUserList = new ArrayList<>();
            while (usersTask.next())
                dbUserList.add(usersTask.getLong("UserID"));                  //saved users for task
            if (dbUserList.size() > dbTask.getUserList().size()) {                      //if some users were deleted
                dbUserList.removeIf(userID -> dbTask.getUserList().contains(userID));   //need to DELETE this users from Task_UserList
                Statement deleteUsers = _conn.createStatement();
                for (Long userId : dbUserList) {
                    deleteUsers.executeUpdate("DELETE * FROM Task_UserList WHERE UserID = " + userId);
                }
            } else if (dbUserList.size() < dbTask.getUserList().size()) {                  //if some users were added
                ArrayList<Long> tmp = (ArrayList<Long>) dbTask.getUserList().clone();
                tmp.removeIf(dbUserList::contains);                                      //need to INSERT this users to Task_UsersList
                Statement insertUsers = _conn.createStatement();
                for (Long userId : tmp) {
                    insertUsers.executeUpdate("INSERT into Tasks VALUES (" + dbTask.getId() + "," + userId + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DBTask parseResultSet(ResultSet resultSet) {
        try {
            long ID = resultSet.getLong("ID");
            long ownerID = resultSet.getLong("ownerID");
            Statement getUsers = _conn.createStatement();
            ResultSet users = getUsers.executeQuery("SELECT UserID FROM Task_UserList WHERE TaskID = " + ID);
            ArrayList<Long> usersForThis = new ArrayList<>();
            while (users.next()) {
                usersForThis.add(users.getLong("UserID"));
            }
            String title = resultSet.getString("Title");
            //Timestamp dateTime = resultSet.getTimestamp("Deadline");
            String dateTime = resultSet.getString("Deadline");
            TaskCalendar deadline = new TaskCalendar(dateTime);
            boolean isPersonal = resultSet.getBoolean("isPersonal");
            Status status = Status.valueOf(resultSet.getString("Status"));
            Priority prio = Priority.valueOf(resultSet.getString("Priority"));
            return new DBTask(ID, ownerID, usersForThis, title, deadline, isPersonal, status, prio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
