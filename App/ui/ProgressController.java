package App.ui;

import App.bll.Observer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class ProgressController implements Observer {
    @FXML
    ProgressIndicator progressBar;
    @FXML
    Label labelWait,
            progressLabel;

    @Override
    public void handleEvent(Long progress) {
        Platform.runLater(() -> {
            progressBar.setProgress(progress / 100.0);
            progressLabel.setText(progress + "%");
        });
    }
}

