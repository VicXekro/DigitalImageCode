package Control;

import Model.Filter;
import Model.Image;
import Model.ImageWindow;
import Model.LinearMask;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 5/2/2017.
 */
public class FilteringImageResultController implements Initializable {

    private Image original8bitImage;
    private LinearMask linearMask;
    private ImageWindow imageWindow;
    private Image convolutedImage;
    private AnchorPane[][] originalImageOverview;
    private AnchorPane[][] convolutedImageOverView;

    @FXML
    private VBox leftVBox;
    @FXML
    private GridPane convolutedImageGridPane;
    @FXML
    private VBox rigthVBox;
    @FXML
    private JFXButton convolutedImageButton;
    @FXML
    private GridPane originalImageGridpane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideComponent(false);
        originalImageOverview = ToolsProvider.ImageOverview(original8bitImage,originalImageOverview);
        for (int i = 0; i <original8bitImage.getNL() ; i++) {
            for (int j = 0; j <original8bitImage.getNC() ; j++) {
                originalImageGridpane.add(originalImageOverview[i][j],j,i);
            }
        }
    }

    @FXML
    void startOnAction(ActionEvent event) {
        rigthVBox.setVisible(true);
        convolutedImageButton.setVisible(true);
        if(linearMask!=null)
            convolutedImage = ToolsProvider.linearConvolution(original8bitImage,linearMask);
        else if (imageWindow!=null){
            convolutedImage = Filter.determineAdaptiveFilterToUse(imageWindow,original8bitImage);
        }
        convolutedImageOverView = ToolsProvider.ImageOverview(convolutedImage,convolutedImageOverView);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <convolutedImage.getNL() ; i++) {
                    for (int j = 0; j <convolutedImage.getNC() ; j++) {
                        convolutedImageGridPane.add(convolutedImageOverView[i][j],j,i);
                    }
                }
            }
        });

    }

    @FXML
    void convolutedImageDataOnAction(ActionEvent event) {
        Stage stage = new Stage();
        stage.setResizable(false);stage.initStyle(StageStyle.UTILITY);
        stage.setScene(new Scene(new LoaderProvider().display8BitVersionOf16BitLoader(convolutedImage)));
        stage.setTitle("Convoluted Image Data");
        stage.show();
    }

    public void hideComponent(boolean b){
        rigthVBox.setVisible(b);
        convolutedImageButton.setVisible(b);
    }

    public Image getOriginal8bitImage() {
        return original8bitImage;
    }

    public void setOriginal8bitImage(Image original8bitImage) {
        this.original8bitImage = original8bitImage;
    }

    public LinearMask getLinearMask() {
        return linearMask;
    }

    public void setLinearMask(LinearMask linearMask) {
        this.linearMask = linearMask;
    }

    public ImageWindow getImageWindow() {
        return imageWindow;
    }

    public void setImageWindow(ImageWindow imageWindow) {
        this.imageWindow = imageWindow;
    }
}
