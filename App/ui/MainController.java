package App.ui;

import App.bll.*;
import App.model.TaskCalendar;
import App.model.TaskDTO;
import App.utils.Priority;
import App.utils.Status;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;

/*
 * TODO: set a date picker + time
 * TODO: set tables executors/notReady
 * TODO: add ability to edit task status or not delete from db, just mark as 'DELETED'
 * TODO: add notifyStatus to App
 * TODO: choose task/user politics(remove owner from executors, remove yourself from executors, edit other user's task, ...)
 * */
public class MainController implements Observer {

    static Long _currentUserID;

    private final TaskService _taskService;
    private final UserService _userService;
    private ObservableList<UITask> _data;
    private Stage _mainStage;
    private Stage _editStage;
    private Stage _loginStage;
    private Stage _infoStage;
    private Stage _notifyStage;
    private Stage _spinnerStage;
    private Parent _editParent;
    private Parent _loginParent;
    private Parent _infoParent;
    private Parent _notifyOkParent;
    private Parent _notifyYesNoParent;
    private Parent _spinnerParent;
    private EditController _editController;
    private LoginController _loginController;
    private TaskInfoController _taskInfoController;
    private NotifyControllerOK _notifyControllerOK;
    private NotifyControllerYesNo _notifyControllerYesNo;

    @FXML
    private Button btnAdd,
            btnDelete;
    @FXML
    private TableView<UITask> tableView;
    @FXML
    private TableColumn<UITask, String>
            columnOwner,
            columnTitle,
            columnDeadline,
            columnStatus,
            columnPriority;
    @FXML
    private TableColumn<UITask, Long>
            columnID;
    @FXML
    private TableColumn<UITask, Boolean>
            columnPersonal;
    @FXML
    private Label labelCount;
    @FXML
    private Hyperlink settingsLink;

    public MainController() {
        loadResources();
        showLoginDialog();
        if (_loginController.getUiUser() == null)
            System.exit(0);
        _currentUserID = _loginController.getUiUser().getId();
        _taskService = new TaskServiceImpl(_currentUserID);
        _userService = new UserServiceImpl();
        _data = FXCollections.observableArrayList();
        _data.addListener((ListChangeListener<UITask>) c -> updateCount());
    }

    public static Long getCurrentUserID() {
        return _currentUserID;
    }

    public void setMainStage(Stage mainStage) {
        _mainStage = mainStage;
    }

    private void loadResources() {
        FXMLLoader _editLoader = new FXMLLoader();
        _editLoader.setLocation(getClass().getResource("fxml/EditDialog.fxml"));
        FXMLLoader _loginLoader = new FXMLLoader();
        _loginLoader.setLocation(getClass().getResource("fxml/LoginDialog.fxml"));
        FXMLLoader _infoLoader = new FXMLLoader();
        _infoLoader.setLocation(getClass().getResource("fxml/InfoDialog.fxml"));
        FXMLLoader _notifyOkLoader = new FXMLLoader();
        _notifyOkLoader.setLocation(getClass().getResource("fxml/NotifyDialogOK.fxml"));
        FXMLLoader _notifyYesNoLoader = new FXMLLoader();
        _notifyYesNoLoader.setLocation(getClass().getResource("fxml/NotifyDialogYesNo.fxml"));
        FXMLLoader _spinnerLoader = new FXMLLoader();
        _spinnerLoader.setLocation(getClass().getResource("fxml/ProgressDialog.fxml"));
        try {
            _editParent = _editLoader.load();              //EditController()
            _loginParent = _loginLoader.load();            //LoginController()
            _infoParent = _infoLoader.load();              //TaskInfoController()
            _notifyOkParent = _notifyOkLoader.load();      //NotifyControllerOK()
            _notifyYesNoParent = _notifyYesNoLoader.load();//NotifyControllerYesNo()
            _spinnerParent = _spinnerLoader.load();        //SpinnerController()
        } catch (IOException e) {
            e.printStackTrace();
        }
        _editController = _editLoader.getController();
        _loginController = _loginLoader.getController();
        _taskInfoController = _infoLoader.getController();
        _notifyControllerOK = _notifyOkLoader.getController();
        _notifyControllerYesNo = _notifyYesNoLoader.getController();
        //ProgressController _spinnerController = _spinnerLoader.getController();
    }

    @FXML
    private void initialize() {
        _taskService.getNotificationForUser(_currentUserID).forEach(task -> {
            if (getCurrentUser().isReadyToOrder())
                _notifyControllerOK.setUiTask(new UITask(task));
            else
                _notifyControllerYesNo.setUiTask(new UITask(task));
            showNotifyDialog(getCurrentUser().isReadyToOrder());
        }); //show notifications
        updateList();
        columnID.setVisible(false);
        columnDeadline.setComparator(Comparator.comparing(TaskCalendar::new));
        columnStatus.setComparator(Comparator.comparing(Status::valueOf));
        columnPriority.setComparator(Comparator.comparing(Priority::valueOf));
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2)
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    onInfoAction(tableView.getSelectionModel().getSelectedItem());
                }
        });                               //show infoDialog on double-click
    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel<UITask> sm = tableView.getSelectionModel();
        UITask selectedTask = sm.getSelectedItem();
        Object source = actionEvent.getSource();
        if (!(source instanceof Button))
            return;
        Button clickedBtn = (Button) source;
        switch (clickedBtn.getId()) {
            case "btnAdd":
                onAddAction();
                break;
            case "btnDelete":
                onDeleteAction(selectedTask);
                break;
            case "btnEdit":
                onEditAction(selectedTask);
                break;
            case "btnBackground":
                showProgressDialog();
                onBackground();
                break;
        }
        _editController.setUiTask(null);
        updateList();
    }

    private void onBackground() {
        /*BackgroundOperation wait = new BackgroundOperation(() -> {
            System.out.println("start");
            try {
                Thread.sleep(1000);
                System.out.println("running");
                Thread.sleep(1000);
                System.out.println("running");
                Thread.sleep(1000);
                System.out.println("running");
                Thread.sleep(1000);
                System.out.println("finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        wait.addObserver(this);
        wait.run();*/
        BackgroundOperation backgroundOperation = new BackgroundOperation(() -> {
            for (int i = 0; i < 100; i++) {
                _taskService.getListForUser(_currentUserID);
            }
        });
        backgroundOperation.addObserver(this);
        backgroundOperation.run();
    }

    @FXML
    public void menuItemSelected(ActionEvent actionEvent) {
        TableView.TableViewSelectionModel<UITask> sm = tableView.getSelectionModel();
        UITask selectedTask = sm.getSelectedItem();
        Object source = actionEvent.getSource();
        if (!(source instanceof MenuItem))
            return;
        MenuItem clicked = (MenuItem) source;
        switch (clicked.getId()) {
            case "menuItemDelete":
                onDeleteAction(selectedTask);
                break;
            case "menuItemEdit":
                onEditAction(selectedTask);
                break;
            case "menuItemProperties":
                onInfoAction(selectedTask);
                break;
        }
        _taskInfoController.setUiTask(null);
        updateList();
    }

    private UIUser getCurrentUser() {
        return _loginController.getUiUser();
    }

    private void fillTable() {
        columnID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnOwner.setCellValueFactory(cellData -> new UIUser(_userService.getById(cellData.getValue().getOwner())).loginProperty());
        columnDeadline.setCellValueFactory(cellData -> cellData.getValue().deadlineProperty());
        columnPersonal.setCellValueFactory(cellData -> cellData.getValue().isPersonalProperty());
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        columnPriority.setCellValueFactory(cellData -> cellData.getValue().prioProperty());
        _taskService.getListForUser(_currentUserID).forEach(taskDTO -> _data.add(new UITask(taskDTO)));
        tableView.setItems(_data);
    }

    private void onAddAction() {
        _editController.setUiTask(null);
        showEditDialog();
        if (_editController.getUiTask() == null)
            return;
        _taskService.create(new TaskDTO(_editController.getUiTask()));
    }

    private void onDeleteAction(UITask selected) {
        _taskService.removeById(selected.getId());
    }

    private void onEditAction(UITask selectedTask) {
        _editController.setUiTask(selectedTask);
        showEditDialog();
        if (_editController.getUiTask() == null)
            return;
        _taskService.update(selectedTask.getId(), new TaskDTO(_editController.getUiTask()));
    }

    private void onInfoAction(UITask uiTask) {
        _taskInfoController.setUiTask(uiTask);
        showInfoDialog();
    }

    @FXML
    private void showSettingDialog() {

    }

    private void showProgressDialog() {
        if (_spinnerStage == null) {
            _spinnerStage = new Stage();
            _spinnerStage.setResizable(false);
            _spinnerStage.initOwner(_mainStage);
            _spinnerStage.setScene(new Scene(_spinnerParent));
            _spinnerStage.initModality(Modality.WINDOW_MODAL);
        }
        _spinnerStage.show();
        //System.out.println("open");
    }

    private void showEditDialog() {
        if (_editStage == null) { //lazy initialization
            _editStage = new Stage();
            _editStage.setMinWidth(370.0);
            _editStage.setMinHeight(140.0);
            _editStage.setResizable(false);
            _editStage.setScene(new Scene(_editParent));
            _editStage.initModality(Modality.WINDOW_MODAL);
            _editStage.initOwner(_mainStage);
            _editStage.setOnHiding(event -> updateList());
        }
        _editStage.showAndWait();
    }

    private void showLoginDialog() {
        if (_loginStage == null) { //lazy initialization
            _loginStage = new Stage();
            _loginStage.setMinWidth(370.0);
            _loginStage.setMinHeight(140.0);
            _loginStage.setResizable(false);
            _loginStage.setScene(new Scene(_loginParent));
            _loginStage.initOwner(_mainStage);
        }
        _loginStage.showAndWait();
    }

    private void showInfoDialog() {
        if (_infoStage == null) {
            _infoStage = new Stage();
            _infoStage.setMinWidth(400);
            _infoStage.setMinHeight(400);
            _infoStage.setScene(new Scene(_infoParent));
            _infoStage.initOwner(_mainStage);
            _infoStage.setOnHiding(event -> updateList());
        }
        _infoStage.showAndWait();
    }

    private void showNotifyDialog(boolean isReadyToOrder) {
        if (_notifyStage == null) {
            _notifyStage = new Stage();
            _notifyStage.setMinWidth(240);
            _notifyStage.setMinHeight(360);
            _notifyStage.setResizable(false);
            if (isReadyToOrder) {
                _notifyStage.setScene(new Scene(_notifyOkParent));
                _notifyStage.setOnShowing(event -> _notifyControllerOK.setFields());

            } else {
                _notifyStage.setScene(new Scene(_notifyYesNoParent));
                _notifyStage.setOnShowing(event -> _notifyControllerYesNo.setFields());
            }
            _notifyStage.initOwner(_mainStage);
            _notifyStage.setOnHiding(event -> updateList());
        }
        _notifyStage.showAndWait();
    }

    private void updateCount() {
        labelCount.setText("Count of ToDo: " + _data.size() + ", current user: " + _userService.getById(_currentUserID).getLogin());
    }

    private void updateList() {
        _data.clear();
        fillTable();
        updateCount();
    }

    @Override
    public void handleEvent() {
        System.out.println("handleEvent");
        Platform.runLater(_spinnerStage::close);
    }
}
