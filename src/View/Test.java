package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by Xekro on 3/19/2017.
 */
public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("StartPageView.fxml"));
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }

    public static void main (String[]args){
        launch(args);
    }
}
