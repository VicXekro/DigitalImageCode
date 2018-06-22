package Control;

import Model.Image;
import Model.ImageWindow;
import Model.Texture;
import View.LoaderProvider;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 5/28/2017.
 */
public class TextureAnalysisController implements Initializable{

    private Image imageToTexture;
    private String firstOrderOperation;

    /**First Order Components*/
    @FXML
    private JFXComboBox<String> fisrtOrderStatisticalParameter;
    @FXML
    private JFXTextField firstOrderImageWindowNL;
    @FXML
    private JFXComboBox<String> firstOrderBorderFilling;
    @FXML
    private JFXTextField firstOrderImageWindowNC;
    @FXML
    private StackPane stackPane;

    /**Second Order Components*/
    @FXML
    private JFXComboBox<String> secondOrderBorderFillingCombobox;
    @FXML
    private JFXComboBox<String> secondOrderTextureParameterCombobox;
    @FXML
    private JFXTextField secondOrderImageWindowNC;
    @FXML
    private JFXTextField secondOrderInterPixelDistance;
    @FXML
    private JFXComboBox<String> secondOrderDirectionCombobox;
    @FXML
    private JFXTextField secondOrderImageWindowNL;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageToTexture = ToolsProvider.convertImage(imageToTexture);
        firstOrderBorderFilling.getItems().setAll(ToolsProvider.initializeBorderFilllingCombo());
        fisrtOrderStatisticalParameter.getItems().setAll(firstOrderParameter());
        secondOrderBorderFillingCombobox.getItems().setAll(ToolsProvider.initializeBorderFilllingCombo());
        secondOrderTextureParameterCombobox.getItems().setAll(secondOrderParameter());
        secondOrderDirectionCombobox.getItems().setAll(Texture.directionList());
    }

    /**First Order Methods*/
    @FXML
    void fisrtOrderValidateButtonOnAction(ActionEvent event) {
        if (checkFirstOrderEntries()){
            int a = Integer.parseInt(firstOrderImageWindowNC.getText()), b = Integer.parseInt(firstOrderImageWindowNL.getText());
            ImageWindow imageWindow = new ImageWindow(a,b);
            imageWindow.setBorderFeeling(firstOrderBorderFilling.getSelectionModel().getSelectedItem());
            firstOrderOperation = fisrtOrderStatisticalParameter.getSelectionModel().getSelectedItem();
            Stage stage = new Stage();
            stage.setTitle("Textured Image Result First Order: "+ firstOrderOperation);
            stage.setResizable(false);
            stage.setScene(new Scene(new LoaderProvider().textureAnalysisResultFirstOrderLoader(imageToTexture,imageWindow,firstOrderOperation)));
            stage.show();
        }
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
                ToolsProvider.displayErrorDialog(s,stackPane);
            }
            else if(firstOrderBorderFilling.getSelectionModel().getSelectedItem()==null || fisrtOrderStatisticalParameter.getSelectionModel().getSelectedItem()==null)
                b = false;
        }catch (NumberFormatException nfe){
            b = false;
            ToolsProvider.displayErrorDialog(s,stackPane);
        }
        return b;
    }

    public Image getImageToTexture() {
        return imageToTexture;
    }

    public void setImageToTexture(Image imageToTexture) {
        this.imageToTexture = imageToTexture;
    }

    /**first order parameter load*/
    private LinkedList<String> firstOrderParameter(){
        LinkedList<String> operation = new LinkedList<>();
        operation.add(Texture.FIRST_ORDER_AVERAGE);
        operation.add(Texture.FIRST_ORDER_ENTROPY);
        operation.add(Texture.FIRST_ORDER_KURTOSIS);
        operation.add(Texture.FIRST_ORDER_SKEWNESS);
        operation.add(Texture.FIRST_ORDER_STANDARD_DEVIATION);
        return operation;
    }

    /**Second Order methods*/
    @FXML
    void secondOrderValidateButtonOnAction(ActionEvent event) {
        if(checkSecondOrderEntries()){
            int a = Integer.parseInt(secondOrderImageWindowNC.getText()), b = Integer.parseInt(secondOrderImageWindowNL.getText()),
                    interPixelDistance = Integer.parseInt(secondOrderInterPixelDistance.getText());
            ImageWindow imageWindow = new ImageWindow(a,b);
            imageWindow.setBorderFeeling(secondOrderBorderFillingCombobox.getSelectionModel().getSelectedItem());
            String secondOrderTextureParameter = secondOrderTextureParameterCombobox.getSelectionModel().getSelectedItem();
            String direction = secondOrderDirectionCombobox.getSelectionModel().getSelectedItem();
            Stage stage = new Stage();
            stage.setScene(new Scene(new LoaderProvider().textureAnalysisResultSecondOrderLoader(imageToTexture,imageWindow,secondOrderTextureParameter,direction,interPixelDistance)));
            stage.setTitle("Textured Image Result Second Order: "+ secondOrderTextureParameter);
            stage.setResizable(false);
            stage.show();
        }
    }

    private LinkedList<String> secondOrderParameter(){
        LinkedList<String> parameter = new LinkedList<>();
        parameter.add(Texture.SECOND_ORDER_CONTRAST);
        parameter.add(Texture.SECOND_ORDER_ENERGY);
        parameter.add(Texture.SECOND_ORDER_ENTROPY);
       // parameter.add(Texture.SECOND_ORDER_CORRELATION);
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
                ToolsProvider.displayErrorDialog(s,stackPane);
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
}
