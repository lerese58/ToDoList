package App.ui;

import App.bll.BLTask;
import App.bll.TaskService;
import App.bll.TaskServiceImpl;
import App.model.TaskCalendar;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;

/*
 * TODO: set a date picker + time
 * Todo: solve a problem with menuItemDelete
 * todo: create a dialog with properties
 * todo: imagine a design of mainDialog
 * todo: Github
 * todo: repair adding task & think about userList
 *
 * */
public class MainDialogController {

    public static long _currentUserID;
    private final TaskService _taskService;
    private ObservableList<UITask> _data;
    private Stage _mainStage;
    private Stage _editStage;
    private Stage _loginStage;
    private Parent _editParent;
    private Parent _loginParent;
    private FXMLLoader _editLoader;
    private FXMLLoader _loginLoader;
    private EditDialogController _editDialogController;
    private LoginDialogController _loginDialogController;
    //TODO: realize context menu
    @FXML
    private Button btnAdd,
            btnDelete,
            btnSearch;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<UITask> tableView;
    @FXML
    private TableColumn<UITask, String> columnTitle,
            columnDeadline,
            columnStatus,
            columnPriority;
    @FXML
    private TableColumn<UITask, Long> columnID,
            columnOwner;
    @FXML
    private TableColumn<UITask, Boolean> columnPersonal;

    @FXML
    private Label labelCount;

    public MainDialogController() {
        _taskService = new TaskServiceImpl();
        _data = FXCollections.observableArrayList();
        _editLoader = new FXMLLoader();
        _editLoader.setLocation(getClass().getResource("fxml/EditDialog.fxml"));
        _loginLoader = new FXMLLoader();
        _loginLoader.setLocation(getClass().getResource("fxml/LoginDialog.fxml"));
        try {
            _editParent = _editLoader.load();//EditDialogController()
            _loginParent = _loginLoader.load();//LoginDialogController()
        } catch (IOException e) {
            e.printStackTrace();
        }
        _editDialogController = _editLoader.getController();
        _loginDialogController = _loginLoader.getController();
    }

    public MainDialogController(TaskService taskService) {
        _taskService = taskService;
        _data = FXCollections.observableArrayList();
        _editLoader = new FXMLLoader();
        _editLoader.setLocation(getClass().getResource("fxml/EditDialog.fxml"));
        _loginLoader = new FXMLLoader();
        _loginLoader.setLocation(getClass().getResource("fxml/LoginDialog.fxml"));
        try {
            _editParent = _editLoader.load();//EditDialogController()
            _loginParent = _loginLoader.load();//LoginDialogController()
        } catch (IOException e) {
            e.printStackTrace();
        }
        _editDialogController = _editLoader.getController();
        _loginDialogController = _loginLoader.getController();
    }

    public void setMainStage(Stage stage) {
        _mainStage = stage;
    }

    private UIUser getUser() {
        return _loginDialogController.getUiUser();
    }

    private void fillTable() throws FileNotFoundException {
        columnID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnOwner.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnDeadline.setCellValueFactory(cellData -> cellData.getValue().deadlineProperty());
        columnPersonal.setCellValueFactory(cellData -> cellData.getValue().isPersonalProperty());
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        columnPriority.setCellValueFactory(cellData -> cellData.getValue().prioProperty());
        _data = FXCollections.observableArrayList();
        for (BLTask blTask : _taskService.getListForThisUser(_currentUserID)) {
            _data.add(new UITask(blTask));
        }
    }

    @FXML
    private void initialize() throws IOException {
        showLoginDialog();
        if (_loginDialogController.getUiUser() == null) {
            System.exit(0);
            return;
        }
        _currentUserID = getUser().getId();
        fillTable();
        _data.addListener((ListChangeListener<UITask>) c -> updateCount());
        updateList();
        columnID.setVisible(false);
        columnDeadline.setComparator(Comparator.comparing(TaskCalendar::new));
        labelCount.setText("Count of ToDo: " + _data.size());
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
        }
        _editDialogController.setUiTask(null);
        updateList();
        updateCount();
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
                break;
        }

    }

    private void onAddAction() {
        showEditDialog();
        if (!(_editDialogController.getUiTask() == null))
            _taskService.update(_editDialogController.getUiTask().getId(), new BLTask(_editDialogController.getUiTask()));
    }

    private void onDeleteAction(UITask selectedTask) {
        if (selectedTask == null)
            return;
        try {
            _taskService.removeById(selectedTask.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onEditAction(UITask selectedTask) {
        if (selectedTask == null)
            return;
        _editDialogController.setUiTask(selectedTask);
        showEditDialog();
        if (!(_editDialogController.getUiTask() == null))
            _taskService.update(selectedTask.getId(), new BLTask(_editDialogController.getUiTask()));
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
        _editStage.showAndWait(); //_editDialogController.initialize();
    }

    private void showLoginDialog() {
        if (_loginStage == null) { //lazy initialization
            _loginStage = new Stage();
            _loginStage.setMinWidth(370.0);
            _loginStage.setMinHeight(140.0);
            _loginStage.setResizable(false);
            _loginStage.setScene(new Scene(_loginParent));
            _loginStage.initOwner(_mainStage);
            _loginStage.setOnHiding(event -> getUser());
        }
        _loginStage.showAndWait(); //_loginDialogController.initialize()
    }

    private void updateCount() {
        labelCount.setText("Count of ToDo: " + _data.size());
    }

    private void updateList() {
        _data.clear();
        try {
            fillTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        _data.addListener((ListChangeListener<UITask>) c -> updateCount());
        tableView.setItems(_data);
        updateCount();
    }
}