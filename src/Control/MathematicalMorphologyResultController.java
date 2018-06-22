package Control;

import Model.Image;
import Model.Mask;
import Model.Morphology;
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
import java.util.StringTokenizer;

/**
 * Created by Xekro on 5/14/2017.
 */
public class MathematicalMorphologyResultController implements Initializable{

    private int thres1, thres2;

    private Image originalImage;
    private Image imageTransformed;
    private AnchorPane[][] originalImageOverView;
    private AnchorPane[][] transformedImageOverview;
    private String Operation;
    private Mask mask;

    @FXML
    private VBox rigthVBox;
    @FXML
    private GridPane transformedImageGridPane;
    @FXML
    private JFXButton transformedImageButton;
    @FXML
    private GridPane originalImageGridpane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideRightComponent(true);
        imageTransformed = originalImage;
        originalImageOverView = ToolsProvider.ImageOverview(originalImage,originalImageOverView);
        for (int i = 0; i <originalImage.getNL() ; i++) {
            for (int j = 0; j <originalImage.getNC() ; j++) {
                originalImageGridpane.add(originalImageOverView[i][j],j,i);
            }
        }
    }

    @FXML
    void startOnAction(ActionEvent event) {
        hideRightComponent(false);
        operationToPerform();
        transformedImageGridPane.getChildren().clear();
        transformedImageOverview = ToolsProvider.ImageOverview(imageTransformed,transformedImageOverview);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <imageTransformed.getNL() ; i++) {
                    for (int j = 0; j <imageTransformed.getNC() ; j++) {
                        transformedImageGridPane.add(transformedImageOverview[i][j],j,i);
                    }
                }
            }
        });
    }

    @FXML
    void transformedImageDataOnAction(ActionEvent event) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.setScene(new Scene(new LoaderProvider().display8BitVersionOf16BitLoader(imageTransformed)));
        stage.setTitle(Operation+ ": Image Date");
        stage.show();
    }

    public void hideRightComponent(boolean b){
        rigthVBox.setVisible(!b);
        transformedImageButton.setVisible(!b);
    }

    public void operationToPerform(){
        if(Operation.equals(Morphology.BINARIZATION))
            imageTransformed = Morphology.binarization(thres1,thres2,originalImage);
        else if(Operation.equals(Morphology.EROSION))
            for (int i = 0; i < mask.getOrder(); i++)
                imageTransformed = Morphology.imageErosion(mask, imageTransformed);
        else if(Operation.equals(Morphology.DILATION))
            for (int i = 0; i < mask.getOrder(); i++)
                imageTransformed = Morphology.imageDilation(mask, imageTransformed);
        else if(Operation.equals(Morphology.OPENING))
            imageTransformed = Morphology.imageOpening(mask,imageTransformed);
        else if(Operation.equals(Morphology.CLOSURE))
            imageTransformed = Morphology.imageClosure(mask, imageTransformed);
        else if(Operation.equals(Morphology.WHITETOPHAT))
            imageTransformed = Morphology.imageWhiteTopHat(mask,imageTransformed);
        else if (Operation.equals(Morphology.BLACKTOPHAT))
            imageTransformed = Morphology.imageBlackTopHat(mask,imageTransformed);
        else if(Operation.equals(Morphology.SKELETISATION))
            imageTransformed = Morphology.imageSkeletisation(mask,originalImage);
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public void setThreshold(int thres1, int thres2){
        this.thres1 = thres1;
        this.thres2 = thres2;
    }

    public void setMask(Mask mask) {
        this.mask = mask;
    }
}
