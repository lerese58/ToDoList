package App.ui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
        /*Connection conn = DBConnection.getInstance().getConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet set = stat.executeQuery("select * from task");
            while (set.next())
                System.out.println(set.getString("title"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Repository<DBUser> userRepoDB = new UserRepoDB();
        userRepoDB.update(2L, new DBUser(2L, "second", "user", "1234", true));
        Repository<DBTask> taskRepoDB = new TaskRepoDB();
        ArrayList<Long> users = new ArrayList<>();
        users.add(1L);
        users.add(2L);
        taskRepoDB.update(3, new DBTask(3,1,users, "testT_UL", new TaskCalendar("00:00 31.12.2018"), true, Status.IN_PROGRESS, Priority.LOW));
        System.out.println(taskRepoDB.getAll());*/
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/MainDialog.fxml"));
        //loader.setLocation(getClass().getResource("fxml/EditDialog.fxml"));
        Parent fxmlMain = loader.load();//MainDialogController()
        MainDialogController mainController = loader.getController();
        mainController.setMainStage(primaryStage);
        Scene scene = new Scene(fxmlMain, 640, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ToDoList");
        primaryStage.setMinWidth(480);
        primaryStage.setMinHeight(300);
        primaryStage.show();//mainController.initialize()
    }
}
