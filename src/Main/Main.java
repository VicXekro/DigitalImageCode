package Main;

import View.LoaderProvider;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;

/**
 * Created by Xekro on 5/27/2017.
 */
public class Main extends Application {

    @Override
    public void init() throws Exception {
        int a = 0, b = 0;
        for (int i = 0; i <100 ; i++) {
            b =(int) (Math.random()*10);
            b = ((b<0)?(b*-1):b);
            a += b;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(a));
            Thread.currentThread().sleep(25);
            //Thread.sleep(100);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new LoaderProvider().startPageLoader()));
        primaryStage.setTitle("Digital Image Code Experience");
        FileInputStream fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
        primaryStage.getIcons().add(new Image(fis));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main (String[]args){
        LauncherImpl.launchApplication(Main.class, Preload.class, args);
    }
}
