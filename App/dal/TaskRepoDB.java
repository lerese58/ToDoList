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
            ResultSet setOfTasks = getAllStatement.executeQuery("select * from task");
            if (setOfTasks.next()) {
                do {
                    taskList.add(parseResultSet(setOfTasks));

                }
                while (setOfTasks.next());
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
            ResultSet taskById = getByIdStatement.executeQuery("select * from task where task.ID = " + id);
            if (!taskById.next())
                return null;
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
            Statement removeTask = _conn.createStatement();
            Statement removeExecutors = _conn.createStatement();
            removeTask.executeUpdate("delete from task where ID = " + id);
            removeExecutors.executeUpdate("delete from task_userlist where TaskID = " + id);//TODO: transaction
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
                PreparedStatement insertStatement = _conn.prepareStatement("insert into task values (?,?,?,?,?,?,?)");
                insertStatement.setLong(1, dbTask.getId());
                insertStatement.setLong(2, dbTask.getOwnerID());
                insertStatement.setString(3, dbTask.getTitle());
                insertStatement.setString(4, dbTask.getDeadline().toString());
                insertStatement.setBoolean(5, dbTask.isPersonal());
                insertStatement.setString(6, dbTask.getStatus().toString());
                insertStatement.setString(7, dbTask.getPrio().toString());
                insertStatement.executeUpdate();
                changeUserList(ID, dbTask); //TODO: transaction
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else { // edit task
            try {
                PreparedStatement updateStatement = _conn.prepareStatement("UPDATE task SET ownerID=?,Title=?,Deadline=?,isPersonal=?,Status=?,Prio=?" +
                        " WHERE ID = ?");
                updateStatement.setLong(1, dbTask.getOwnerID());
                updateStatement.setString(2, dbTask.getTitle());
                updateStatement.setString(3, dbTask.getDeadline().toString());
                updateStatement.setBoolean(4, dbTask.isPersonal());
                updateStatement.setString(5, dbTask.getStatus().toString());
                updateStatement.setString(6, dbTask.getPrio().toString());
                updateStatement.setLong(7, ID);
                changeUserList(ID, dbTask);
                updateStatement.executeUpdate();
                changeUserList(ID, dbTask); // TODO : Transaction
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void changeUserList(long ID, DBTask dbTask) { // TODO: transaction
        try {
            Statement taskUsers = _conn.createStatement();
            ResultSet usersTask = taskUsers.executeQuery("SELECT UserID FROM task_userlist WHERE TaskID = " + ID);
            ArrayList<Long> dbUserList = new ArrayList<>();
            while (usersTask.next())
                dbUserList.add(usersTask.getLong("UserID"));                  //saved users for task
            if (dbUserList.size() > dbTask.getUserList().size()) {                      //if some users were deleted
                dbUserList.removeIf(userID -> dbTask.getUserList().contains(userID));   //need to DELETE this users from Task_UserList
                Statement deleteUsers = _conn.createStatement();
                for (Long userId : dbUserList) {
                    deleteUsers.executeUpdate("DELETE FROM task_userlist WHERE UserID = " + userId);
                }
            } else if (dbUserList.size() < dbTask.getUserList().size()) {                  //if some users were added
                ArrayList<Long> tmp = (ArrayList<Long>) dbTask.getUserList().clone();
                tmp.removeIf(dbUserList::contains);                                      //need to INSERT this users to Task_UsersList
                Statement insertUsers = _conn.createStatement();
                for (Long userId : tmp) {
                    insertUsers.executeUpdate("INSERT into task_userlist (TaskID, UserID, NotifyStatus) VALUES (" + dbTask.getId() + "," + userId + "," + "'ADDED'" + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DBTask parseResultSet(ResultSet resultSet) {
        try {
            long ID = resultSet.getInt("ID");
            long ownerID = resultSet.getInt("ownerID");
            Statement getUsers = _conn.createStatement();
            ResultSet users = getUsers.executeQuery("SELECT UserID FROM Task_UserList WHERE TaskID = " + ID);
            ArrayList<Long> usersForThis = new ArrayList<>();
            while (users.next()) {
                usersForThis.add((users.getLong("UserID")));
            }
            String title = resultSet.getString("Title");
            String dateTime = resultSet.getString("Deadline");
            TaskCalendar deadline = new TaskCalendar(dateTime);
            boolean isPersonal = resultSet.getBoolean("isPersonal");
            Status status = Status.valueOf(resultSet.getString("Status"));
            Priority prio = Priority.valueOf(resultSet.getString("Prio"));
            return new DBTask(ID, ownerID, usersForThis, title, deadline, isPersonal, status, prio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
