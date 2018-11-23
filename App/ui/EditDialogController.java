package App.ui;

import App.bll.UserService;
import App.bll.UserServiceImpl;
import App.model.TaskCalendar;
import App.utils.Priority;
import App.utils.Status;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tornadofx.control.DateTimePicker;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditDialogController {

    @FXML
    CheckBox personalCheck;
    @FXML
    ListView<UIUser> listViewUsers;
    @FXML
    ChoiceBox<Priority> menuPriority;
    private UserService _userService;
    private ObservableList<UIUser> _executorsList;
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

    public EditDialogController() {
        _userService = new UserServiceImpl();
        _executorsList = FXCollections.observableArrayList();
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
        setFields();
    }

    private void setFields() {
        if (_uiTask != null) {
            txtTitle.setText(_uiTask.getTitle());
            dateTimePicker.setDateTimeValue(new TaskCalendar(_uiTask.getDeadline()).getDateTime());
            menuPriority.setValue(Priority.valueOf(_uiTask.getPrio()));
            personalCheck.setSelected(_uiTask.isPersonal());
            _executorsList.clear();
            for (SimpleLongProperty userId : _uiTask.getUserList())
                _executorsList.add(new UIUser(_userService.getById(userId.get())));
            listViewUsers.setVisible(true);
            listViewUsers.setItems(_executorsList);
            return;
        }
        txtTitle.setText("");
        txtTitle.setPromptText("Input title");
        dateTimePicker.setDateTimeValue(null);
        dateTimePicker.setPromptText("HH:mm dd:MM:yyyy");
        menuPriority.setValue(null);
        personalCheck.setSelected(false);
        _executorsList.clear();
        listViewUsers.setVisible(false);
    }

    @FXML
    private void addExecutor() {
        if (_userService.getByLogin(txtUserAdd.getText()) == null) {
            txtUserAdd.setText("");
            txtUserAdd.setPromptText("No user found with this name");
        } else {
            UIUser newExecutor = new UIUser(_userService.getByLogin(txtUserAdd.getText()));
            for (UIUser uiUser : _executorsList) {
                if (uiUser.getId().equals(newExecutor.getId())) {
                    txtUserAdd.setText("");
                    txtUserAdd.setPromptText("Already added");
                    return;
                }
            }
            _executorsList.add(newExecutor);
            txtUserAdd.setText("");
            listViewUsers.setVisible(true);
            listViewUsers.setItems(_executorsList);
        }
    }

    @FXML
    private void removeExecutor() {
        UIUser selected = listViewUsers.getSelectionModel().getSelectedItem();
        if (selected.getId() == MainDialogController._currentUserID) {
            txtUserAdd.setText("");
            txtUserAdd.setPromptText("You can't remove yourself");
            return;
        }
        _executorsList.removeAll(selected);
        listViewUsers.setItems(_executorsList);
    }

    @FXML
    public boolean editTask(ActionEvent actionEvent) {
        boolean titleFieldIsEmpty = txtTitle.getText().isEmpty();
        Button source = (Button) actionEvent.getSource();
        if (source.getId().equals("btnCancel")) {
            closeDialog();
            return false;
        } else if (!titleFieldIsEmpty) {
            ArrayList<Long> listExecutor = new ArrayList<>();
            for (UIUser uiUser : _executorsList)
                listExecutor.add(uiUser.getId());
            if (!(listExecutor.contains(MainDialogController._currentUserID)))
                listExecutor.add(MainDialogController._currentUserID);
            setUiTask(new UITask(MainDialogController._currentUserID,               //ownerID
                    listExecutor,                                                   //userList
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
        _executorsList.clear();
        closeDialog();
    }
}
