package Control;

import Model.Image;
import Model.ImageWindow;
import Model.Texture;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 5/28/2017.
 */
public class TextureAnalysisResultController implements Initializable {

    private Image original8bitImage;
    private Image texturedImage;
    private AnchorPane[][] originalImageOverview;
    private AnchorPane[][] texturedImageOverview;
    private ImageWindow imageWindow;
    private String firstOrderParameter;
    private String secondOrderParameter;
    private int secondOrderPixelInterDistance;
    private String secondOrderDirection;

    @FXML
    private JFXButton texturedImageButton;
    @FXML
    private VBox rigthVBox;
    @FXML
    private GridPane originalImageGridpane;
    @FXML
    private GridPane texturedImageGridPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideRightComponent(true);
        originalImageOverview = ToolsProvider.ImageOverview(original8bitImage,originalImageOverview);
        for (int i = 0; i <original8bitImage.getNL() ; i++) {
            for (int j = 0; j <original8bitImage.getNC() ; j++) {
                originalImageGridpane.add(originalImageOverview[i][j],j,i);
            }
        }
    }

    @FXML
    void startOnAction(ActionEvent event) {
        hideRightComponent(false);
        texturedImageGridPane.getChildren().clear();
        if (firstOrderParameter!=null)
            firstOrderParameterToUse(firstOrderParameter);
        else if(secondOrderParameter!=null)
            secondOrderParameterToUse();
        texturedImageOverview = ToolsProvider.ImageOverview(texturedImage,texturedImageOverview);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <texturedImage.getNL() ; i++) {
                    for (int j = 0; j <texturedImage.getNC() ; j++) {
                        texturedImageGridPane.add(texturedImageOverview[i][j],j,i);
                    }
                }
            }
        });
    }

    @FXML
    void texturedImageDataOnAction(ActionEvent event) {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(new LoaderProvider().display8BitVersionOf16BitLoader(texturedImage)));
        stage.setTitle("Image Data");
        stage.show();
    }

    public void hideRightComponent(boolean b){
        rigthVBox.setVisible(!b);
        texturedImageButton.setVisible(!b);
    }

    private void firstOrderParameterToUse(String parameter){
        if(parameter.equals(Texture.FIRST_ORDER_AVERAGE))
            texturedImage = Texture.averageCalculationProcess(original8bitImage,imageWindow);
        else if(parameter.equals(Texture.FIRST_ORDER_ENTROPY))
            texturedImage = Texture.entropyCalculationWholeProcess(original8bitImage,imageWindow);
        else if(parameter.equals(Texture.FIRST_ORDER_STANDARD_DEVIATION))
            texturedImage = Texture.standardCalculationWholeProcess(original8bitImage,imageWindow);
        else if(parameter.equals(Texture.FIRST_ORDER_SKEWNESS))
            texturedImage = Texture.skewnessCalculationWholeProcess(original8bitImage,imageWindow);
        else if (parameter.equals(Texture.FIRST_ORDER_KURTOSIS))
            texturedImage = Texture.kurtosisCalculationWholeProcess(original8bitImage,imageWindow);
    }

    private void secondOrderParameterToUse(){
        if(secondOrderParameter.equals(Texture.SECOND_ORDER_ENERGY))
            texturedImage = Texture.sEnergy(original8bitImage,imageWindow,secondOrderDirection,secondOrderPixelInterDistance);
        else if(secondOrderParameter.equals(Texture.SECOND_ORDER_CONTRAST))
            texturedImage = Texture.sContrast(original8bitImage,imageWindow,secondOrderDirection,secondOrderPixelInterDistance);
        else if(secondOrderParameter.equals(Texture.SECOND_ORDER_DISSIMINARITY))
            texturedImage = Texture.sDissiminarity(original8bitImage,imageWindow,secondOrderDirection,secondOrderPixelInterDistance);
    }

    public void setOriginal8bitImage(Image original8bitImage) {
        this.original8bitImage = original8bitImage;
    }

    public void setImageWindow(ImageWindow imageWindow) {
        this.imageWindow = imageWindow;
    }

    public void setFirstOrderParameter(String firstOrderParameter) {
        this.firstOrderParameter = firstOrderParameter;
    }

    public void setSecondOrderParameter(String secondOrderParameter) {
        this.secondOrderParameter = secondOrderParameter;
    }

    public void setSecondOrderPixelInterDistance(int secondOrderPixelInterDistance) {
        this.secondOrderPixelInterDistance = secondOrderPixelInterDistance;
    }

    public void setSecondOrderDirection(String secondOrderDirection) {
        this.secondOrderDirection = secondOrderDirection;
    }
}
