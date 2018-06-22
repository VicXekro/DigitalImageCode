package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import ConcreteImage.Model.SecondOrderTextureAnalysisMethods;
import Control.*;
import Control.ToolsProvider;
import Model.ImageWindow;
import Model.Texture;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/15/2017.
 */
public class SecondOrderTextureController implements Initializable {

    private Image imageToTexture;
    private StackPane stackPaneDisplayer;
    private HomePageController homePageController;

    @FXML
    private JFXComboBox<String> secondOrderBorderFillingCombobox;
    @FXML
    private JFXTextField secondOrderImageWindowNC;
    @FXML
    private JFXComboBox<String> secondOrderTextureParameterCombobox;
    @FXML
    private JFXTextField secondOrderInterPixelDistance;
    @FXML
    private JFXComboBox<String> secondOrderDirectionCombobox;
    @FXML
    private JFXTextField secondOrderImageWindowNL;
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        secondOrderBorderFillingCombobox.getItems().setAll(Control.ToolsProvider.initializeBorderFilllingCombo());
        secondOrderTextureParameterCombobox.getItems().setAll(secondOrderParameter());
        secondOrderDirectionCombobox.getItems().setAll(Texture.directionList());
    }

    @FXML
    void secondOrderValidateButtonOnAction(ActionEvent event) {
        if(checkSecondOrderEntries()){
            int a = Integer.parseInt(secondOrderImageWindowNC.getText()), b = Integer.parseInt(secondOrderImageWindowNL.getText()),
                    interPixelDistance = Integer.parseInt(secondOrderInterPixelDistance.getText());
            ImageWindow imageWindow = new ImageWindow(a,b);
            imageWindow.setBorderFeeling(secondOrderBorderFillingCombobox.getSelectionModel().getSelectedItem());
            String secondOrderTextureParameter = secondOrderTextureParameterCombobox.getSelectionModel().getSelectedItem();
            String direction = secondOrderDirectionCombobox.getSelectionModel().getSelectedItem();
            try {
                secondOderParameterToUse(secondOrderTextureParameter, imageWindow, direction, interPixelDistance);
                ConcreteImage.Controller.ToolsProvider.reloadImage(imageToTexture, stackPaneDisplayer, homePageController);
            }catch (OutOfMemoryError e){
                ConcreteImage.Controller.ToolsProvider.displayAlertMemory();
            }
        }
    }

    public void setImageToTexture(Image imageToTexture) {
        this.imageToTexture = imageToTexture;
    }

    public void setStackPaneDisplayer(StackPane stackPaneDisplayer) {
        this.stackPaneDisplayer = stackPaneDisplayer;
    }

    private LinkedList<String> secondOrderParameter(){
        LinkedList<String> parameter = new LinkedList<>();
        parameter.add(Texture.SECOND_ORDER_CONTRAST);
        parameter.add(Texture.SECOND_ORDER_ENERGY);
        parameter.add(Texture.SECOND_ORDER_ENTROPY);
        //parameter.add(Texture.SECOND_ORDER_CORRELATION);
        //parameter.add(Texture.SECOND_ORDER_CLUSTER_PROMINENCE);
        parameter.add(Texture.SECOND_ORDER_DISSIMINARITY);
        return parameter;
    }

    private boolean checkSecondOrderEntries(){
        String s = "- All fields should be filled\n" +
                "- For Number of Columns and Number of Lines and Interpixel distance:\n" +
                "\t>Only positive whole numbers greater than zero are accepted";
        boolean b = true;
        try{
            int a = Integer.parseInt(secondOrderImageWindowNC.getText());
            int c = Integer.parseInt(secondOrderImageWindowNL.getText());
            int d = Integer.parseInt(secondOrderInterPixelDistance.getText());
            if(a<=0 || c<= 0 || d<=0){
                b = false;
                Control.ToolsProvider.displayErrorDialog(s,stackPane);
            }
            else if(secondOrderBorderFillingCombobox.getSelectionModel().getSelectedItem()==null || secondOrderTextureParameterCombobox.getSelectionModel().getSelectedItem()==null
                    || secondOrderDirectionCombobox.getSelectionModel().getSelectedItem() == null)
                b = false;
        }catch (NumberFormatException nfe){
            b = false;
            ToolsProvider.displayErrorDialog(s,stackPane);
        }
        return b;
    }

    private void secondOderParameterToUse(String parameter,ImageWindow imageWindow,String direction, int interpixelDistance){
        if(parameter.equals(Texture.SECOND_ORDER_ENERGY))
            imageToTexture = SecondOrderTextureAnalysisMethods.sEnergy(imageToTexture,imageWindow,direction,interpixelDistance);
        if(parameter.equals(Texture.SECOND_ORDER_CONTRAST))
            imageToTexture = SecondOrderTextureAnalysisMethods.sContrast(imageToTexture,imageWindow,direction,interpixelDistance);
        if(parameter.equals(Texture.SECOND_ORDER_DISSIMINARITY))
            imageToTexture = SecondOrderTextureAnalysisMethods.sDissiminarity(imageToTexture,imageWindow,direction,interpixelDistance);
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
}
