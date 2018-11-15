package App.bll;

import App.dal.DBTask;
import App.model.TaskCalendar;
import App.ui.UITask;
import App.utils.Priority;
import App.utils.Status;
import javafx.beans.property.SimpleLongProperty;

import java.util.ArrayList;

public class BLTask {
    private final long _id;
    private long _ownerID;
    private ArrayList<Long> _userList;
    private String _title;
    private TaskCalendar _deadline;
    private boolean _isPersonal;
    private Status _status;
    private Priority _prio;

    public BLTask(long ownerID, ArrayList<Long> users, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        _id = Math.abs(title.hashCode() + deadline.toString().hashCode() + ((Boolean) isPersonal).hashCode() + status.toString().hashCode() + prio.toString().hashCode());
        _ownerID = ownerID;
        _userList = users;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
    }

    public BLTask(long id, long ownerID, ArrayList<Long> users, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        _id = id;
        _ownerID = ownerID;
        _userList = users;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
    }

    public BLTask(UITask uiTask) {
        _id = uiTask.getId();
        _ownerID = uiTask.getOwnerID();
        _userList = new ArrayList<>();
        for (SimpleLongProperty userID : uiTask.getUserList()) _userList.add(userID.getValue());
        _title = uiTask.getTitle();
        _deadline = new TaskCalendar(uiTask.getDeadline());
        _isPersonal = uiTask.isPersonal();
        _status = Status.valueOf(uiTask.getStatus());
        _prio = Priority.valueOf(uiTask.getPrio());
    }

    public BLTask(DBTask dbTask) {
        _id = dbTask.getId();
        _ownerID = dbTask.getOwnerID();
        _userList = dbTask.getUserList();
        _title = dbTask.getTitle();
        _deadline = dbTask.getDeadline();
        _isPersonal = dbTask.isPersonal();
        _status = dbTask.getStatus();
        _prio = dbTask.getPrio();
    }

    public long getId() {
        return _id;
    }

    public long getOwnerID() {
        return _ownerID;
    }

    public ArrayList<Long> getUserList() {
        return _userList;
    }

    public String getTitle() {
        return _title;
    }

    public TaskCalendar getDeadline() {
        return _deadline;
    }

    public boolean isPersonal() {
        return _isPersonal;
    }

    public Status getStatus() {
        return _status;
    }

    public Priority getPrio() {
        return _prio;
    }

    @Override
    public String toString() {
        return String.valueOf(getId()) +
                "/" + getOwnerID() +
                "/" + getUserList() +
                "/" + getTitle() +
                "/" + getDeadline().toString() +
                "/" + isPersonal() +
                "/" + getStatus().toString() +
                "/" + getPrio().toString();
    }
}
