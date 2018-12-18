package App.ui;

import App.bll.UserService;
import App.bll.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private final UserService _userService;
    private UIUser _uiUser;
    private Stage _newUserStage;
    private Parent _newUserParent;

    @FXML
    private Button
            btnOK,
            btnCancel;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Hyperlink hyperReg;

    public LoginController() {
        _userService = new UserServiceImpl();
        FXMLLoader _newUserLoader = new FXMLLoader();
        _newUserLoader.setLocation(getClass().getResource("fxml/NewUserDialog.fxml"));
        try {
            _newUserParent = _newUserLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UIUser getUiUser() {
        return _uiUser;
    }

    private void setUiUser(UIUser uiUser) {
        _uiUser = uiUser;
    }

    @FXML
    private void initialize() {
        txtLogin.selectHome();
    }

    @FXML
    public void onButtonClick(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        if (source.getId().equals("btnCancel"))
            onCancelButton();
        if (source.getId().equals("btnOK")) {
            if (txtLogin.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                txtLogin.setPromptText("Input login");
                txtPassword.setPromptText("Input password");
            } else if (_userService.getByLoginPassword(txtLogin.getText(), txtPassword.getText()) != null) {
                setUiUser(new UIUser(_userService.getByLoginPassword(txtLogin.getText(), txtPassword.getText())));
                closeDialog();
            } else if (_userService.getByLoginPassword(txtLogin.getText(), txtPassword.getText()) == null) {
                txtLogin.setText("");
                txtPassword.setText("");
                txtLogin.setPromptText("Incorrect login");
                txtPassword.setPromptText("Incorrect password");
            }
        }

    }

    @FXML
    private void showNewUserDialog() {
        if (_newUserStage == null) { //lazy initialization
            _newUserStage = new Stage();
            _newUserStage.setMinWidth(370.0);
            _newUserStage.setMinHeight(140.0);
            _newUserStage.setResizable(false);
            _newUserStage.setScene(new Scene(_newUserParent));
            _newUserStage.initModality(Modality.WINDOW_MODAL);
        }
        _newUserStage.show(); //_newUserDialogController.initialize();
    }

    private void onCancelButton() {
        System.exit(0);
    }

    private void closeDialog() {
        Stage stage = ((Stage) btnCancel.getScene().getWindow());
        stage.close();
    }
}