package App.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NotifyControllerYesNo {

    private UITask _uiTask;

    @FXML
    private Button btnYes,
            btnNo;
    @FXML
    private Label labelTitle,
            labelTask;

    @FXML
    private void initialize() {

    }

    public void setUiTask(UITask uiTask) {
        _uiTask = uiTask;
    }

    @FXML
    public void onButtonClicked(ActionEvent actionEvent) {
    }
}
