package View;

import Control.SeperateBandController;
import Model.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Xekro on 3/24/2017.
 */
public class Test2 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayImageView.fxml"));
        /*SeperateBandController controller = new SeperateBandController();
        Image image = new Image(3,3,2,"8-bits","Automatically");
        long [] magneticBand = {2,34,46,113,34,255,243,123,133,56,76,98,109,90,76,132,243,123};
        image.setMagneticBand(magneticBand);
        controller.setOriginalImage(image);
        loader.setController(controller);*/
        Parent parent = loader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }

    public static void main (String [] args){
        System.out.println(Integer.MAX_VALUE);
    }
}
