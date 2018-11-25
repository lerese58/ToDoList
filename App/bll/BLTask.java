package App.bll;

import App.model.TaskCalendar;
import App.model.TaskDTO;
import App.ui.UITask;
import App.utils.NotifyStatus;
import App.utils.Priority;
import App.utils.Status;

import java.util.HashMap;
import java.util.Map;

public class BLTask {
    private final long _id;
    private long _ownerID;
    private Map<Long, NotifyStatus> _userList;
    private String _title;
    private TaskCalendar _deadline;
    private boolean _isPersonal;
    private Status _status;
    private Priority _prio;

    private UserService _userService = new UserServiceImpl();

    public BLTask(long ownerID, Map<Long, NotifyStatus> users, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
        _id = Math.abs(title.hashCode() + deadline.toString().hashCode() + ((Boolean) isPersonal).hashCode() + status.toString().hashCode() + prio.toString().hashCode());
        _ownerID = ownerID;
        _userList = users;
        _title = title;
        _deadline = deadline;
        _isPersonal = isPersonal;
        _status = status;
        _prio = prio;
    }

    public BLTask(long id, long ownerID, Map<Long, NotifyStatus> users, String title, TaskCalendar deadline, boolean isPersonal, Status status, Priority prio) {
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
        _ownerID = uiTask.getOwner();
        _userList = new HashMap<>();

        _title = uiTask.getTitle();
        _deadline = new TaskCalendar(uiTask.getDeadline());
        _isPersonal = uiTask.isPersonal();
        _status = Status.valueOf(uiTask.getStatus());
        _prio = Priority.valueOf(uiTask.getPrio());
    }

    public BLTask(TaskDTO taskDTO) {
        _id = taskDTO.getId();
        _ownerID = taskDTO.getOwnerID();
        _userList = taskDTO.getUserList();
        _title = taskDTO.getTitle();
        _deadline = taskDTO.getDeadline();
        _isPersonal = taskDTO.isPersonal();
        _status = taskDTO.getStatus();
        _prio = taskDTO.getPrio();
    }

    public long getId() {
        return _id;
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
        return String.valueOf(getId()) +
                "/" + getOwnerID() +
                "/" + getTitle() +
                "/" + getDeadline().toString() +
                "/" + isPersonal() +
                "/" + getStatus().toString() +
                "/" + getPrio().toString();
    }
}
