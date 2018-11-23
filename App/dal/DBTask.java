package App.dal;

import App.bll.BLTask;
import App.model.TaskCalendar;
import App.utils.Priority;
import App.utils.Status;

import java.util.List;

public class DBTask {
    private final long _id;
    private long _ownerID;
    private List<Long> _userList;
    private String _title;
    private TaskCalendar _deadline;
    private boolean _isPersonal;
    private Status _status;
    private Priority _prio;

    public DBTask(long ownerID, List<Long> users, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        _id = Math.abs(((Long) ownerID).hashCode() + title.hashCode() + deadline.toString().hashCode() + ((Boolean) isPersonal).hashCode() + status.toString().hashCode() + prio.toString().hashCode());
        _ownerID = ownerID;
        _userList = users;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
    }

    public DBTask(long id, long ownerID, List<Long> users, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        _id = id;
        _ownerID = ownerID;
        _userList = users;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
    }

    public DBTask(BLTask blTask) {
        _id = blTask.getId();
        _ownerID = blTask.getOwnerID();
        _userList = blTask.getUserList();
        _deadline = blTask.getDeadline();
        _isPersonal = blTask.isPersonal();
        _status = blTask.getStatus();
        _title = blTask.getTitle();
        _prio = blTask.getPrio();
    }

    public long getId() {
        return _id;
    }

    public long getOwnerID() {
        return _ownerID;
    }

    public List<Long> getUserList() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBTask dbTask = (DBTask) o;
        return _id == dbTask.getId();//TODO
    }

    @Override
    public String toString() {
        String sb = String.valueOf(getId()) +
                "/" + getOwnerID() +
                "/" + getUserList() +
                "/" + getTitle() +
                "/" + getDeadline().toString() +
                "/" + isPersonal() +
                "/" + getStatus().toString() +
                "/" + getPrio().toString();
        return sb;
    }
}
