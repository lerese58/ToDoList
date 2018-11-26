package App.ui;

import App.bll.UserService;
import App.bll.UserServiceImpl;
import App.utils.NotifyStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/*
 * TODO: set btnEdit
 */
public class TaskInfoController {

    private UITask _uiTask;
    private ObservableMap<Long, NotifyStatus> _execMap;
    private UserService _userService;

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

    public TaskInfoController() {
        _userService = new UserServiceImpl();
        _execMap = FXCollections.observableHashMap();
    }

    public TaskInfoController(UserService userService) {
        _userService = userService;
        _execMap = FXCollections.observableHashMap();
    }

    public void setUiTask(UITask uiTask) {
        _uiTask = uiTask;
        setFields();
    }

    @FXML
    private void initialize() {
        _execMap.clear();
    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button))
            return;
        Button clicked = (Button) source;
        switch (clicked.getId()) {
            case "btnOK":
                closeDialog();
                break;
            case "btnEdit":
                closeDialog();//TODO
                break;
            case "btnCancel":
                closeDialog();
                break;
        }

    }

    private void setFields() {
        if (_uiTask != null) {
            titleLabel.setText(_uiTask.getTitle());
            ownerLabel.setText(String.valueOf(_uiTask.getOwner()));
            deadlineLabel.setText(_uiTask.getDeadline());
            personalLabel.setText(_uiTask.isPersonal() ? "Yes" : "No");
            statusLabel.setText(_uiTask.getStatus());
            prioLabel.setText(_uiTask.getPrio());
            tableColumnExecutors.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
            _execMap.clear();
            _uiTask.getUserList().forEach((userID, notifyStatus) -> {
                if (notifyStatus.equals(NotifyStatus.CONFIRMED))
                    _execMap.put(userID, notifyStatus);
            });
            ObservableList<UIUser> observableList = FXCollections.observableArrayList();
            _execMap.forEach((userID, notifyStatus) -> observableList.add(new UIUser(_userService.getById(userID))));
            tableExecutors.setItems(observableList);
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
        _execMap.clear();
    }
}
