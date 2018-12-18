package App.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/MainDialog.fxml"));
        Parent fxmlMain = loader.load();//MainController()
        MainController mainController = loader.getController();
        mainController.setMainStage(primaryStage);
        Scene scene = new Scene(fxmlMain, 640, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ToDoList");
        primaryStage.setMinWidth(480);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }
}
