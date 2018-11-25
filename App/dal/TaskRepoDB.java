package App.dal;

import App.model.TaskCalendar;
import App.model.TaskDTO;
import App.model.UserDTO;
import App.utils.NotifyStatus;
import App.utils.Priority;
import App.utils.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepoDB implements Repository<TaskDTO> {

    private Connection _conn = DBConnection.getInstance().getConnection();
    private final Repository<UserDTO> _userRepoDB = new UserRepoDB();

    @Override
    public List<TaskDTO> getAll() { //TODO: get ALL tasks, mb i need to get only for this user
        List<TaskDTO> taskList = new ArrayList<>();
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
    public boolean create(TaskDTO taskDTO) {
        try {
            _conn.setAutoCommit(false);
            PreparedStatement insertStatement = _conn.prepareStatement("insert into task values (?,?,?,?,?,?,?)");
            insertStatement.setLong(1, taskDTO.getId());
            insertStatement.setLong(2, taskDTO.getOwnerID());
            insertStatement.setString(3, taskDTO.getTitle());
            insertStatement.setString(4, taskDTO.getDeadline().toString());
            insertStatement.setBoolean(5, taskDTO.isPersonal());
            insertStatement.setString(6, taskDTO.getStatus().toString());
            insertStatement.setString(7, taskDTO.getPrio().toString());
            insertStatement.executeUpdate();
            changeUserList(taskDTO.getId(), taskDTO);
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

    @Override
    public TaskDTO getById(long id) {
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
    public boolean update(long ID, TaskDTO taskDTO) {
        try {
            _conn.setAutoCommit(false);
            PreparedStatement updateStatement = _conn.prepareStatement("UPDATE task SET ownerID=?,Title=?,Deadline=?,isPersonal=?,Status=?,Prio=? WHERE ID = ?");
            updateStatement.setLong(1, taskDTO.getOwnerID());
            updateStatement.setString(2, taskDTO.getTitle());
            updateStatement.setString(3, taskDTO.getDeadline().toString());
            updateStatement.setBoolean(4, taskDTO.isPersonal());
            updateStatement.setString(5, taskDTO.getStatus().toString());
            updateStatement.setString(6, taskDTO.getPrio().toString());
            updateStatement.setLong(7, ID);
            updateStatement.executeUpdate();
            changeUserList(ID, taskDTO);
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

    @Override
    public boolean removeById(long id) {
        try {
            _conn.setAutoCommit(false);
            Statement removeTask = _conn.createStatement();
            Statement removeExecutors = _conn.createStatement();
            removeTask.executeUpdate("delete from task where ID = " + id);
            removeExecutors.executeUpdate("delete from task_userlist where TaskID = " + id);
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

    private void changeUserList(long ID, TaskDTO taskDTO) { // TODO: transaction
        try {
            Statement taskUsers = _conn.createStatement();
            ResultSet usersTask = taskUsers.executeQuery("SELECT UserID, NotifyStatus FROM task_userlist WHERE TaskID = " + ID);
            Map<Long, NotifyStatus> usersForTaskFromDB = new HashMap<>();
            while (usersTask.next()) {
                usersForTaskFromDB.put(usersTask.getLong("UserID"), NotifyStatus.valueOf(usersTask.getString("NotifyStatus")));
            }
            Map<Long, NotifyStatus> needToUpdateInDB = new HashMap<>();
            Map<Long, NotifyStatus> needToInsertIntoDB = new HashMap<>();
            List<Long> needToDeleteFromDB = new ArrayList<>();

            taskDTO.getUserList().forEach((userID, notifyStatus) -> {
                if (!usersForTaskFromDB.containsKey(userID))
                    needToInsertIntoDB.put(userID, notifyStatus);
            });
            usersForTaskFromDB.forEach((userID, notifyStatus) -> {
                if (taskDTO.getUserList().containsKey(userID))
                    if (!taskDTO.getUserList().get(userID).equals(notifyStatus))
                        needToUpdateInDB.put(userID, taskDTO.getUserList().get(userID));
            });
            usersForTaskFromDB.forEach((userID, notifyStatus) -> {
                if (!taskDTO.getUserList().containsKey(userID))
                    needToDeleteFromDB.add(userID);
            });
            Statement insertUsers = _conn.createStatement();
            Statement deleteUsers = _conn.createStatement();
            Statement updateUsers = _conn.createStatement();
            needToInsertIntoDB.forEach((userID, notifyStatus) -> {
                try {
                    insertUsers.executeUpdate("INSERT into task_userlist VALUES ("
                            + taskDTO.getId() + "," + userID + "," + "'" + notifyStatus.toString() + "')");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            needToDeleteFromDB.forEach(userID -> {
                try {
                    deleteUsers.executeUpdate("DELETE FROM task_userlist " +
                            "WHERE UserID = " + userID + " AND TaskID = " + taskDTO.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            needToUpdateInDB.forEach((userID, notifyStatus) -> {
                try {
                    updateUsers.executeUpdate("update task_userlist set NotifyStatus = '" + notifyStatus.toString()
                            + "' where TaskID = " + taskDTO.getId() + " and UserID = " + userID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private TaskDTO parseResultSet(ResultSet resultSet) {
        try {
            long ID = resultSet.getInt("ID");
            long ownerID = resultSet.getInt("ownerID");
            Statement getUsers = _conn.createStatement();
            ResultSet users = getUsers.executeQuery("SELECT UserID, NotifyStatus FROM Task_UserList WHERE TaskID = " + ID);
            Map<Long, NotifyStatus> usersForThis = new HashMap<>();
            while (users.next()) {
                usersForThis.put(users.getLong("UserID"), NotifyStatus.valueOf(users.getString("NotifyStatus")));
            }
            String title = resultSet.getString("Title");
            String dateTime = resultSet.getString("Deadline");
            TaskCalendar deadline = new TaskCalendar(dateTime);
            boolean isPersonal = resultSet.getBoolean("isPersonal");
            Status status = Status.valueOf(resultSet.getString("Status"));
            Priority prio = Priority.valueOf(resultSet.getString("Prio"));
            return new TaskDTO(ID, ownerID, usersForThis, title, deadline, isPersonal, status, prio);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
