package Control;

import Model.Image;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 4/15/2017.
 */
public class DisplayImageAnimationController implements Initializable {

    private Image imageToAnimate;
    private AnchorPane [][] arrayOfColor;
    private boolean isZOISelected;
    private String displayMethod;
    int i=0,j=0;

    @FXML
    private GridPane gridPane;

    @FXML
    private JFXButton ZOIBinarizeButton;
    @FXML
    private ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        arrayOfColor = ToolsProvider.ImageOverview(imageToAnimate,arrayOfColor);
        progressBar.setVisible(false);
        ZOIBinarizeButton.setVisible(false);
        if(isZOISelected)
            ZOIBinarizeButton.setVisible(true);
    }

    @FXML
    void startAnimationOnAction(ActionEvent event) {
        gridPane.getChildren().clear();
        if(displayMethod.equals(DisplayImageController.PIXEL_BY_PIXEL))
            pixelByPixelMethod();
        else if(displayMethod.equals(DisplayImageController.LINE_PER_LINE))
            linePerLineMethod();
        else if(displayMethod.equals(DisplayImageController.GLOBAL))
            globalMethod();
    }

    @FXML
    void ZOIBinarizeOnAction(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Zone of Interest: Binary form");
        stage.setScene(new Scene(new LoaderProvider().display8BitVersionOf16BitLoader(imageToAnimate)));
        stage.show();
    }

    public Image getImageToAnimate() {
        return imageToAnimate;
    }

    public void setImageToAnimate(Image imageToAnimate) {
        this.imageToAnimate = imageToAnimate;
    }

    public AnchorPane[][] getArryOfColor() {
        return arrayOfColor;
    }

    public void setArryOfColor(AnchorPane[][] arrayOfColor) {
        this.arrayOfColor = arrayOfColor;
    }

    public void setZOISelected(boolean ZOISelected) {
        isZOISelected = ZOISelected;
    }

    public void setDisplayMethod(String displayMethod) {
        this.displayMethod = displayMethod;
    }

    //method in charge of the animation for the pixelBypixel method
    public void pixelByPixelMethod(){
        progressBar.setVisible(true);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int k = 0;
                for (i = 0; i <imageToAnimate.getNL() ; i++) {
                    for ( j = 0; j <imageToAnimate.getNC() ; j++) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                gridPane.add(arrayOfColor[i][j],j,i);
                            }
                        });
                        updateProgress(k+1, imageToAnimate.getNL()*imageToAnimate.getNC());
                        k++;
                        Thread.sleep(100);
                    }
                }
                return null;
            }
        };
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e->{progressBar.setVisible(false); i=0; j=0;});
        new Thread(task).start();
    }

    //method in charge of the animation for the line per line method.
    public void linePerLineMethod(){
        progressBar.setVisible(true);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int k = 0;
                for (i = 0; i <imageToAnimate.getNL() ; i++) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                for(j = 0; j<imageToAnimate.getNC();j++ ){
                                    gridPane.add(arrayOfColor[i][j],j,i);
                                }
                            }
                        });
                    updateProgress(k+1,imageToAnimate.getNL());
                    k++;
                    Thread.sleep(100);
                }
                return null;
            }
        };
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e->{progressBar.setVisible(false);i=0; j=0;});
        new Thread(task).start();
    }

    //method in charge of the animation for the global method
    public void globalMethod(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<imageToAnimate.getNL(); i++){
                    for (int j = 0; j <imageToAnimate.getNC() ; j++) {
                        gridPane.add(arrayOfColor[i][j],j,i);
                    }
                }
            }
        });
    }

}
