package App.model;

import App.ui.UITask;
import App.utils.Priority;
import App.utils.Status;
import javafx.beans.property.SimpleLongProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDTO extends EntityDTO {

    private long _ownerID;
    private List<Long> _userList;
    private String _title;
    private TaskCalendar _deadline;
    private boolean _isPersonal;
    private Status _status;
    private Priority _prio;

    public TaskDTO(long ownerID, List<Long> userList, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        _ownerID = ownerID;
        _userList = userList;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
        __modified = LocalDateTime.now();
    }

    public TaskDTO(long id, long ownerID, List<Long> userList, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        __id = id;
        _ownerID = ownerID;
        _userList = userList;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
        __modified = LocalDateTime.now();
    }

    public TaskDTO(UITask uiTask) {
        __id = uiTask.getId();
        _ownerID = uiTask.getOwner();
        _userList = new ArrayList<>();
        _title = uiTask.getTitle();
        for (SimpleLongProperty userID : uiTask.getUserList()) _userList.add(userID.get());
        _deadline = new TaskCalendar(uiTask.getDeadline());
        _isPersonal = uiTask.isPersonal();
        _status = Status.valueOf(uiTask.getStatus());
        _prio = Priority.valueOf(uiTask.getPrio());
        __modified = uiTask.getModified();
    }

    public long getId() {
        return __id;
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
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(__id);
        sb.append("/").append(_ownerID);
        sb.append("/").append(_userList);
        sb.append("/").append(_title);
        sb.append("/").append(_deadline);
        sb.append("/").append(_isPersonal);
        sb.append("/").append(_status);
        sb.append("/").append(_prio);
        return sb.toString();
    }
}
