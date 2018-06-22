package ConcreteImage.Controller;

import ConcreteImage.Model.FirstOrderTextureAnalysisMethods;
import ConcreteImage.Model.Image;
import Control.*;
import Control.ToolsProvider;
import Model.ImageWindow;
import Model.Texture;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/14/2017.
 */
public class FirstOrderTextureController implements Initializable{

    private Image image;
    private StackPane stackPaneDisplayer;
    private HomePageController homePageController;

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox<String> firstOrderStatisticalParameter;
    @FXML
    private JFXTextField firstOrderImageWindowNL;
    @FXML
    private JFXComboBox<String> firstOrderBorderFilling;
    @FXML
    private JFXTextField firstOrderImageWindowNC;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstOrderBorderFilling.getItems().setAll(Control.ToolsProvider.initializeBorderFilllingCombo());
        firstOrderStatisticalParameter.getItems().setAll(firstOrderParameter());
    }

    @FXML
    void fisrtOrderValidateButtonOnAction(ActionEvent event) {
        if (checkFirstOrderEntries()){
            homePageController.getProgressHbox().setVisible(true);
            int a = Integer.parseInt(firstOrderImageWindowNC.getText()), b = Integer.parseInt(firstOrderImageWindowNL.getText());
            ImageWindow imageWindow = new ImageWindow(a,b);
            imageWindow.setBorderFeeling(firstOrderBorderFilling.getSelectionModel().getSelectedItem());
            String firsOrderParameter = firstOrderStatisticalParameter.getSelectionModel().getSelectedItem();
            try {
                firstOrderParameterToUse(firsOrderParameter, imageWindow);
                ConcreteImage.Controller.ToolsProvider.reloadImage(image, stackPaneDisplayer, homePageController);
            }catch (OutOfMemoryError e){
                ConcreteImage.Controller.ToolsProvider.displayAlertMemory();
            }
        }
    }

    private void firstOrderParameterToUse(String parameter, ImageWindow imageWindow)throws OutOfMemoryError{
        if(parameter.equals(Texture.FIRST_ORDER_AVERAGE))
            image = FirstOrderTextureAnalysisMethods.averageCalculationProcess(image,imageWindow);
        else if(parameter.equals(Texture.FIRST_ORDER_STANDARD_DEVIATION))
            image = FirstOrderTextureAnalysisMethods.standardCalculationWholeProcess(image,imageWindow);
        else if(parameter.equals(Texture.FIRST_ORDER_ENTROPY))
            image = FirstOrderTextureAnalysisMethods.entropyCalculationWholeProcess(image,imageWindow);
        else if(parameter.equals(Texture.FIRST_ORDER_KURTOSIS))
            image = FirstOrderTextureAnalysisMethods.kurtosisCalculationWholeProcess(image,imageWindow);
        else if(parameter.equals(Texture.FIRST_ORDER_SKEWNESS))
            image = FirstOrderTextureAnalysisMethods.skewnessCalculationWholeProcess(image,imageWindow);
    }

    private LinkedList<String> firstOrderParameter(){
        LinkedList<String> operation = new LinkedList<>();
        operation.add(Texture.FIRST_ORDER_AVERAGE);
        operation.add(Texture.FIRST_ORDER_ENTROPY);
        operation.add(Texture.FIRST_ORDER_KURTOSIS);
        operation.add(Texture.FIRST_ORDER_SKEWNESS);
        operation.add(Texture.FIRST_ORDER_STANDARD_DEVIATION);
        return operation;
    }

    private boolean checkFirstOrderEntries(){
        String s = "- All fields should be filled\n" +
                "- For Number of Columns and Number of Lines:\n" +
                "\t>Only positive whole numbers greater than zero are accepted";
        boolean b = true;
        try{
            int a = Integer.parseInt(firstOrderImageWindowNC.getText());
            int c = Integer.parseInt(firstOrderImageWindowNL.getText());
            if(a<=0 || c<= 0){
                b = false;
                Control.ToolsProvider.displayErrorDialog(s,stackPane);
            }
            else if(firstOrderBorderFilling.getSelectionModel().getSelectedItem()==null || firstOrderStatisticalParameter.getSelectionModel().getSelectedItem()==null)
                b = false;
        }catch (NumberFormatException nfe){
            b = false;
            ToolsProvider.displayErrorDialog(s,stackPane);
        }
        return b;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setStackPaneDisplayer(StackPane stackPaneDisplayer) {
        this.stackPaneDisplayer = stackPaneDisplayer;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
}
