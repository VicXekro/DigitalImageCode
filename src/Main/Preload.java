package Main;

import Control.PreloaderController;
import View.LoaderProvider;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Xekro on 5/27/2017.
 */
public class Preload extends javafx.application.Preloader{

    private ProgressBar progressBar;
    private Stage primaryStage;
    private SimpleDoubleProperty progressProperty;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new LoaderProvider().preloaderLoader();
        PreloaderController controller = new PreloaderController();
        loader.setController(controller);
        Parent parent = loader.load();
        progressBar = controller.getProgressBar();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("Digital Image Code Experience");
        primaryStage.show();
    }

    @Override
    public void handleApplicationNotification(javafx.application.Preloader.PreloaderNotification info) {
       // super.handleApplicationNotification(info);
        progressProperty = new SimpleDoubleProperty(((ProgressNotification)info).getProgress());
        progressBar.progressProperty().bind(progressProperty);
    }

    @Override
    public void handleStateChangeNotification(javafx.application.Preloader.StateChangeNotification info) {
        //super.handleStateChangeNotification(info);
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_START:
                primaryStage.hide();
                break;
        }
    }
}
