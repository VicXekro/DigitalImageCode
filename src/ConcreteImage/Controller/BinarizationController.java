package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import Control.*;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/8/2017.
 */
public class BinarizationController implements Initializable {

    private Image imageToBinarize;
    private StackPane parentStackPane;
    private HomePageController homePageController;

    @FXML
    private JFXTextField binarizationThreshold1;
    @FXML
    private JFXTextField binarizationThreshold2;
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void binarizationValidateButtonOnAction(ActionEvent event) {
        if(binarizationCheckValuesEntered()) {
            int a = Integer.parseInt(binarizationThreshold1.getText());
            int b = Integer.parseInt(binarizationThreshold2.getText());
            MorphologyOperation.binarization(imageToBinarize,a,b);
            ToolsProvider.reloadImage(imageToBinarize,parentStackPane, homePageController);
        }
    }

    //method to check the entries in the binarization panel
    public boolean binarizationCheckValuesEntered(){
        boolean checker = true;
        String msg = "- Value entered in From should be less than value entered in To" +
                "\n- All values should pe positive" +
                "\n- Only Whole numbers accepted"+
                "\n- Values entered should be less or equal to 255.";
        try{
            int a = Integer.parseInt(binarizationThreshold1.getText());
            int b = Integer.parseInt(binarizationThreshold2.getText());
            if (a<0 || b <0) {
                Control.ToolsProvider.displayErrorDialog(msg, stackPane);
                checker = false;
            }
            else if (a>b){
                Control.ToolsProvider.displayErrorDialog(msg,stackPane);
                checker = false;
            }else if(a>255 || b>255){
                Control.ToolsProvider.displayErrorDialog(msg,stackPane);
                checker = false;
            }
        }catch (NumberFormatException e){
            Control.ToolsProvider.displayErrorDialog(msg,stackPane);
            checker = false;
        }
        return checker;
    }

    public void setImageToBinarize(Image imageToBinarize) {
        this.imageToBinarize = imageToBinarize;
    }

    public void setParentStackPane(StackPane parentStackPane) {
        this.parentStackPane = parentStackPane;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
}
