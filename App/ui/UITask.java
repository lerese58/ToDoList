package App.ui;

import App.bll.UserService;
import App.bll.UserServiceImpl;
import App.model.TaskCalendar;
import App.model.TaskDTO;
import App.utils.Priority;
import App.utils.Status;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UITask {
    UserService _userService = new UserServiceImpl();
    private SimpleLongProperty _id;
    private SimpleLongProperty _ownerID;
    private SimpleStringProperty _title;
    private List<SimpleLongProperty> _userList;
    private SimpleStringProperty _deadline;
    private SimpleBooleanProperty _isPersonal;
    private SimpleStringProperty _status;
    private SimpleStringProperty _prio;
    private LocalDateTime _modified;

    public UITask(Long id, Long ownerID, ArrayList<Long> users, String title, TaskCalendar deadline, Boolean isPersonal, Status status, Priority prio) {
        _id = new SimpleLongProperty(id);
        _ownerID = new SimpleLongProperty(ownerID);
        _userList = new ArrayList<>();
        for (Long userID : users) _userList.add(new SimpleLongProperty(userID));
        _title = new SimpleStringProperty(title);
        _deadline = new SimpleStringProperty(deadline.toString());
        _isPersonal = new SimpleBooleanProperty(isPersonal);
        _status = new SimpleStringProperty(status.toString());
        _prio = new SimpleStringProperty(prio.toString());
        _modified = LocalDateTime.now();
    }

    public UITask(Long ownerID, List<Long> users, String title, String deadline, Boolean isPersonal, String status, String prio) {
        _id = new SimpleLongProperty(Math.abs(title.hashCode() + deadline.hashCode() + status.hashCode() + prio.hashCode()));
        _ownerID = new SimpleLongProperty(ownerID);
        _userList = new ArrayList<>();
        for (Long userID : users) _userList.add(new SimpleLongProperty(userID));
        _title = new SimpleStringProperty(title);
        _deadline = new SimpleStringProperty(deadline);
        _isPersonal = new SimpleBooleanProperty(isPersonal);
        _status = new SimpleStringProperty(status);
        _prio = new SimpleStringProperty(prio);
        _modified = LocalDateTime.now();
    }

    public UITask(TaskDTO taskDTO) {
        _id = new SimpleLongProperty(taskDTO.getId());
        _ownerID = new SimpleLongProperty(taskDTO.getOwnerID());
        _userList = new ArrayList<>();
        for (Long userID : taskDTO.getUserList()) _userList.add(new SimpleLongProperty(userID));
        _title = new SimpleStringProperty(taskDTO.getTitle());
        _deadline = new SimpleStringProperty(taskDTO.getDeadline().toString());
        _isPersonal = new SimpleBooleanProperty(taskDTO.isPersonal());
        _status = new SimpleStringProperty(taskDTO.getStatus().toString());
        _prio = new SimpleStringProperty(taskDTO.getPrio().toString());
        _modified = taskDTO.getModified();
    }

    public long getId() {
        return _id.get();
    }

    public void setId(long id) {
        _id.set(id);
    }

    public SimpleLongProperty idProperty() {
        return _id;
    }

    public Long getOwner() {
        return _ownerID.get();
    }

    public void setOwnerID(Long ownerID) {
        _ownerID.set(ownerID);
    }

    public SimpleLongProperty ownerIdProperty() {
        return _ownerID;
    }

    public String getTitle() {
        return _title.get();
    }

    public void setTitle(String title) {
        _title.set(title);
    }

    public SimpleStringProperty titleProperty() {
        return _title;
    }

    public List<SimpleLongProperty> getUserList() {
        return _userList;
    }

    public void setUserList(List<SimpleLongProperty> userList) {
        _userList = userList;
    }

    public String getDeadline() {
        return _deadline.get();
    }

    public void setDeadline(String deadline) {
        _deadline.set(deadline);
    }

    public SimpleStringProperty deadlineProperty() {
        return _deadline;
    }

    public boolean isPersonal() {
        return _isPersonal.get();
    }

    public void setPersonal(boolean isPersonal) {
        _isPersonal.set(isPersonal);
    }

    public SimpleBooleanProperty isPersonalProperty() {
        return _isPersonal;
    }

    public String getStatus() {
        return _status.get();
    }

    public void setStatus(String status) {
        _status.set(status);
    }

    public SimpleStringProperty statusProperty() {
        return _status;
    }

    public String getPrio() {
        return _prio.get();
    }

    public void setPrio(String prio) {
        _prio.set(prio);
    }

    public SimpleStringProperty prioProperty() {
        return _prio;
    }

    public LocalDateTime getModified() {
        return _modified;
    }
}