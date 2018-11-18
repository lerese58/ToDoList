package App.ui;

import App.bll.UserService;
import App.bll.UserServiceImpl;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

public class TaskInfoDialogController {

    private UITask _uiTask;
    private ObservableList<UIUser> _execList;
    private UserService _userService;
    private Parent _infoParent;

    @FXML
    private Label
            titleLabel,
            ownerLabel,
            deadlineLabel,
            personalLabel,
            statusLabel,
            prioLabel;
    @FXML
    private Button
            btnOK,
            btnEdit,
            btnCancel;
    @FXML
    private TableView<UIUser> tableExecutors;
    @FXML
    private TableColumn<UIUser, String> tableColumnExecutors;

    public TaskInfoDialogController() {
        _userService = new UserServiceImpl();
        _execList = FXCollections.observableArrayList();
        FXMLLoader _loginLoader = new FXMLLoader();
        _loginLoader.setLocation(getClass().getResource("fxml/NewUserDialog.fxml"));
        try {
            _infoParent = _loginLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TaskInfoDialogController(UserService userService) {
        _userService = userService;
        _execList = FXCollections.observableArrayList();
    }

    public UITask getUiTask() {
        return _uiTask;
    }

    public void setUiTask(UITask uiTask) {
        _uiTask = uiTask;
    }

    @FXML
    private void initialize() {
        titleLabel.setText(_uiTask.getTitle());
        ownerLabel.setText(String.valueOf(_uiTask.getOwnerID()));
        deadlineLabel.setText(_uiTask.getDeadline());
        personalLabel.setText(_uiTask.isPersonal() ? "Yes" : "No");
        statusLabel.setText(_uiTask.getStatus());
        prioLabel.setText(_uiTask.getPrio());
        tableColumnExecutors.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        for (SimpleLongProperty userId : _uiTask.getUserList()) {
            _execList.add(new UIUser(_userService.getById(userId.getValue())));
        }
    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button))
            return;
        Button clicked = (Button) source;
        switch (clicked.getId()) {
            case "btnOK":
                break;
            case "btnEdit":
                break;
            case "btnCancel":
                break;
        }

    }

}
