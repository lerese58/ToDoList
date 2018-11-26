package App.ui;

import App.bll.UserService;
import App.bll.UserServiceImpl;
import App.model.TaskCalendar;
import App.utils.NotifyStatus;
import App.utils.Priority;
import App.utils.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;

import java.util.regex.Pattern;

public class EditController {

    @FXML
    CheckBox personalCheck;
    @FXML
    ListView<UIUser> listViewUsers;
    @FXML
    ChoiceBox<Priority> menuPriority;
    private UserService _userService;
    private ObservableMap<Long, NotifyStatus> _executorsMap;
    private UITask _uiTask;
    @FXML
    private Button
            btnOK,
            btnRemove,
            btnCancel;
    @FXML
    private TextField
            txtTitle,
            txtUserAdd;
    @FXML
    private DateTimePicker dateTimePicker;

    public EditController() {
        _userService = new UserServiceImpl();
        _executorsMap = FXCollections.observableHashMap();
        Pattern _correctTime = Pattern.compile("^\\d{2}:\\d{2} \\d{2}.\\d{2}.\\d{4}$");
    }

    @FXML
    private void initialize() {
        menuPriority.setItems(FXCollections.observableArrayList(Priority.values()));
        menuPriority.setOnInputMethodTextChanged(event -> menuPriority.setValue(menuPriority.getSelectionModel().getSelectedItem()));
    }

    public UITask getUiTask() {
        return _uiTask;
    }

    public void setUiTask(UITask uiTask) {
        _uiTask = uiTask;
        setFields();//initialize???
    }

    private void setFields() {
        if (_uiTask != null) {
            txtTitle.setText(_uiTask.getTitle());
            dateTimePicker.setDateTimeValue(new TaskCalendar(_uiTask.getDeadline()).getDateTime());
            menuPriority.setValue(Priority.valueOf(_uiTask.getPrio()));
            personalCheck.setSelected(_uiTask.isPersonal());
            _executorsMap.clear();
            _uiTask.getUserList().forEach((userID, notifyStatus) -> {
                if (notifyStatus.equals(NotifyStatus.CONFIRMED))
                    _executorsMap.put(userID, notifyStatus);
            });
            listViewUsers.setVisible(true);
            ObservableList<UIUser> observableList = FXCollections.observableArrayList();
            _executorsMap.forEach((userID, notifyStatus) -> observableList.add(new UIUser(_userService.getById(userID))));
            listViewUsers.setItems(observableList);
            return;
        }
        txtTitle.setText("");
        txtTitle.setPromptText("Input title");
        dateTimePicker.setDateTimeValue(null);
        dateTimePicker.setPromptText("HH:mm dd:MM:yyyy");
        menuPriority.setValue(null);
        personalCheck.setSelected(false);
        _executorsMap.clear();
        listViewUsers.setVisible(false);
    }

    @FXML
    private void addExecutor() {
        if (_userService.getByLogin(txtUserAdd.getText()) == null) {
            txtUserAdd.setText("");
            txtUserAdd.setPromptText("No user found with this name");
        } else {
            UIUser newExecutor = new UIUser(_userService.getByLogin(txtUserAdd.getText()));
            if (_executorsMap.containsKey(newExecutor.getId())) {
                txtUserAdd.setText("");
                txtUserAdd.setPromptText("Already added");
                return;
            }
            _executorsMap.put(newExecutor.getId(), NotifyStatus.NON_SEEN);
            txtUserAdd.setText("");
            listViewUsers.setVisible(true);
            ObservableList<UIUser> observableList = FXCollections.observableArrayList();
            _executorsMap.forEach((userID, notifyStatus) -> observableList.add(new UIUser(_userService.getById(userID))));
            listViewUsers.setItems(observableList);
        }
    }

    @FXML
    private void removeExecutor() {
        UIUser selected = listViewUsers.getSelectionModel().getSelectedItem();
        if (selected.getId().equals(MainController._currentUserID)) {
            txtUserAdd.setText("");
            txtUserAdd.setPromptText("You can't remove yourself");
            return;
        }
        _executorsMap.remove(selected.getId());
        ObservableList<UIUser> observableList = FXCollections.observableArrayList();
        observableList.clear();
        _executorsMap.forEach((userID, notifyStatus) -> observableList.add(new UIUser(_userService.getById(userID))));
        listViewUsers.setItems(observableList);
    }

    @FXML
    public boolean editTask(ActionEvent actionEvent) {
        boolean titleFieldIsEmpty = txtTitle.getText().isEmpty();
        Button source = (Button) actionEvent.getSource();
        if (source.getId().equals("btnCancel")) {
            closeDialog();
            return false;
        } else if (!titleFieldIsEmpty) {
            if (!(_executorsMap.containsKey(MainController._currentUserID)))
                _executorsMap.put(MainController._currentUserID, NotifyStatus.CONFIRMED);
            else _executorsMap.replace(MainController._currentUserID, NotifyStatus.CONFIRMED);
            setUiTask(new UITask(MainController._currentUserID,                     //ownerID
                    _executorsMap,                                                  //userList
                    txtTitle.getText(),                                             //title
                    new TaskCalendar(dateTimePicker.getDateTimeValue()).toString(), //Deadline
                    personalCheck.isSelected(),                                     //isPersonal
                    Status.IN_PROGRESS.toString(),                                  //status
                    menuPriority.getSelectionModel().getSelectedItem().toString()));//priority
            closeDialog();
            return true;
        } else {
            txtTitle.setPromptText("Input title");
        }
        return false;
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButton() {
        setUiTask(null);
        _executorsMap.clear();
        closeDialog();
    }
}
