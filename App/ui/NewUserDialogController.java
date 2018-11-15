package App.ui;

import App.bll.BLUser;
import App.bll.UserService;
import App.bll.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class NewUserDialogController {

    private final UserService _userService;
    @FXML
    private Button btnOK,
            btnCancel;//isCancelButton
    @FXML
    private TextField txtName,
            txtLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private CheckBox checkBoxIsOpen;

    public NewUserDialogController() {
        _userService = new UserServiceImpl();
    }

    @FXML
    private void initialize() {
        txtName.setText("");
        txtLogin.setText("");
        txtPassword.setText("");
    }

    private int isDataCorrect() throws FileNotFoundException { // 0 - for correct data, 1 - login already created, 2 - some fields is empty
        boolean alreadyCreated = (_userService.getByLogin(txtLogin.getText()) != null);
        boolean nameEmpty = txtName.getText().isEmpty();
        boolean loginEmpty = txtLogin.getText().isEmpty();
        boolean passwordEmpty = txtPassword.getText().isEmpty();
        if (nameEmpty || loginEmpty || passwordEmpty)
            return 2;
        else if (alreadyCreated)
            return 1;
        else return 0;
    }

    public void onButtonClick(ActionEvent actionEvent) throws FileNotFoundException {
        if (!(actionEvent.getSource() instanceof Button))
            return;
        Button source = (Button) actionEvent.getSource();
        if (source.getId().equals("btnCancel")) {
            closeDialog();
        } else if (source.getId().equals("btnOK")) {
            if (isDataCorrect() == 0) {
                UIUser uiUser = new UIUser(txtName.getText(), txtLogin.getText(), txtPassword.getText(), checkBoxIsOpen.isSelected());
                _userService.update(uiUser.getId(), new BLUser(uiUser));
                closeDialog();
            } else if (isDataCorrect() == 1) {
                txtLogin.setText("");
                txtLogin.setPromptText("User with this login already registered");
                txtPassword.setText("");
            } else if (isDataCorrect() == 2) {
                txtName.setPromptText("This field is required");
                txtLogin.setPromptText("This field is required");
                txtPassword.setPromptText("This field is required");
            }
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
