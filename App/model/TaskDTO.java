package App.model;

import App.ui.UITask;
import App.utils.NotifyStatus;
import App.utils.Priority;
import App.utils.Status;

import java.util.HashMap;
import java.util.Map;

public class TaskDTO extends EntityDTO {

    private long _ownerID;
    private Map<Long, NotifyStatus> _userList;
    private String _title;
    private TaskCalendar _deadline;
    private boolean _isPersonal;
    private Status _status;
    private Priority _prio;

    public TaskDTO(long ownerID, Map<Long, NotifyStatus> userList, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        _ownerID = ownerID;
        _userList = userList;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
    }

    public TaskDTO(long id, long ownerID, Map<Long, NotifyStatus> userList, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        __id = id;
        _ownerID = ownerID;
        _userList = userList;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
    }

    public TaskDTO(UITask uiTask) {
        __id = uiTask.getId();
        _ownerID = uiTask.getOwner();
        _userList = new HashMap<>();
        uiTask.getUserList().forEach((userID, notifyStatus) -> _userList.put(userID, notifyStatus));
        _title = uiTask.getTitle();
        _deadline = new TaskCalendar(uiTask.getDeadline());
        _isPersonal = uiTask.isPersonal();
        _status = Status.valueOf(uiTask.getStatus());
        _prio = Priority.valueOf(uiTask.getPrio());
    }

    public long getId() {
        return __id;
    }

    public long getOwnerID() {
        return _ownerID;
    }

    public Map<Long, NotifyStatus> getUserList() {
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
        sb.append("/").append(_title);
        sb.append("/").append(_deadline);
        sb.append("/").append(_isPersonal);
        sb.append("/").append(_status);
        sb.append("/").append(_prio);
        return sb.toString();
    }
}
