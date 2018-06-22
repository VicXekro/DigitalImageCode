package SeparateBand;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;

/**
 * Created by Xekro on 3/3/2017.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("src/SeparateBand/UIView.fxml");
        FXMLLoader loader = new FXMLLoader();
        //Parent parent = FXMLLoader.load(getClass().getResource("UIView.fxml"));
        Parent parent = loader.load(fileInputStream);
        Controller controller = loader.getController();
        SImage sImage = new SImage((byte)32,3,3,"h",3,"auto");
        controller.setsImage(sImage);
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }

    public static void main (String[]args){
        launch(args);
    }
}
