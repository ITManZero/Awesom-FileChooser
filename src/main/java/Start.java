import com.ite.fc.Controllers.Controller;
import com.ite.fc.paths.DefaultFolders;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        /*
         * load file chooser fxml file
         */

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fileChooser.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        /*
         * specifies scene and scene(file chooser) theme
         */
        scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
        stage.setScene(scene);

        /*
         * setting up file chooser ex: file extension filter,title
         */

        Controller controller = loader.getController();
        controller.intiStageEvent();
        controller.setInitialPath(DefaultFolders.Download);
        controller.setTitle("Awesome file chooser!");
        controller.setFileExtFilter(true);

        /*
         * show file chooser on screen
         * setting up stage
         */
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}