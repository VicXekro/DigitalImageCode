package Control;

import Model.Image;
import Model.NegativeNumberException;
import View.LoaderProvider;
import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 3/29/2017.
 */
public class DisplayImageController implements Initializable{

    public static String PIXEL_BY_PIXEL = "Pixel by Pixel";
    public static String LINE_PER_LINE = "Line per Line";
    public static String GLOBAL = "Global";

    private Image imageToDisplay;
    private HomePageController homePageController;
    private AnchorPane [][] arrayOfColor;

    private boolean isStartX = true, isStartY = true, isEndX=true, isEndY=true;

    @FXML
    private CheckBox ZOICheckBoxValidator;
    @FXML
    private AnchorPane ZOIPanel;
    @FXML
    private JFXComboBox<String> displayModeComboBox;
    @FXML
    private GridPane gridPaneOverview;
    @FXML
    private JFXTextField endXTextfieldZOI;
    @FXML
    private JFXTextField endYTextfieldZOI;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox<String> displayModeZOI;
    @FXML
    private JFXTextField startYTextfieldZOI;
    @FXML
    private JFXTextField startXTextfieldZOI;
    @FXML
    private Label ZOILabelError;
    @FXML
    private JFXButton dispayZOIButton;
    @FXML
    private JFXButton getThe8bitButton;
    @FXML
    private JFXButton customDisplayButton;
    private Group group;
    private Image image8Bit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image8Bit = ToolsProvider.convertImage(imageToDisplay);
        //setImage8Bit(imageToDisplay);
        //group = homePageController.getGroup();
        if(imageToDisplay.getNbrOfBands()==1)
            customDisplayButton.setDisable(false);
        else
            customDisplayButton.setDisable(true);
        getThe8bitButton.setVisible(false);
        ZOICheckBoxValidator.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ZOIPanel.setDisable(!newValue);
            }
        });
        displayModeComboBox.getItems().setAll(initDisplayMethod());
        displayModeZOI.getItems().setAll(initDisplayMethod());
        colorAssigner();
        for(int i=0; i<imageToDisplay.getNL();i++){
            for(int j=0; j<imageToDisplay.getNC();j++){
                gridPaneOverview.add(arrayOfColor[i][j],j,i);
            }
        }
        if(imageToDisplay.getFormat().equals(Image.FORMAT_16BITS_INTENSITIES)||imageToDisplay.getFormat().equals(Image.FORMAT_16BITS_AMPLITUDE))
            getThe8bitButton.setVisible(true);
    }

    @FXML//method to display the panel in charge of the animation
    void displayOnAction(ActionEvent event) {
        if(displayModeComboBox.getSelectionModel().getSelectedItem()==null)
            ToolsProvider.displayErrorDialog("You should select a display method first",stackPane);
        else {
            Stage stage = new Stage();
            stage.setTitle("Display Image: " + displayModeComboBox.getSelectionModel().getSelectedItem());
            stage.setResizable(false);stage.initStyle(StageStyle.UTILITY);
            stage.setScene(new Scene(new LoaderProvider().displayImageAnimationLoader(image8Bit, displayModeComboBox.getSelectionModel().getSelectedItem(), false)));
            stage.show();
        }
    }

    @FXML
    void displayZOIOnAction(ActionEvent event) {
        if(startXTextfieldZOI.getText().equals("")||startYTextfieldZOI.getText().equals("")||endXTextfieldZOI.getText().equals("")||
                endYTextfieldZOI.getText().equals(""))
            ToolsProvider.displayErrorDialog("All coordinates should be filled",stackPane);
        else if(Integer.parseInt(startXTextfieldZOI.getText())>Integer.parseInt(endXTextfieldZOI.getText())||
                Integer.parseInt(startYTextfieldZOI.getText())>Integer.parseInt(endYTextfieldZOI.getText())||
                Integer.parseInt(startXTextfieldZOI.getText())>imageToDisplay.getNC()|| Integer.parseInt(endXTextfieldZOI.getText())>imageToDisplay.getNC()||
                Integer.parseInt(startYTextfieldZOI.getText())>imageToDisplay.getNL()|| Integer.parseInt(endYTextfieldZOI.getText())>imageToDisplay.getNL())
            ToolsProvider.displayErrorDialog(". Start X should be less than End X and both should be less than the number of column" +
                    "\n. Start Y should be less than End Y and both should be less than the number of line",stackPane);
        else if(displayModeZOI.getSelectionModel().getSelectedItem()==null)
            ToolsProvider.displayErrorDialog("You should choose a display method before continuing",stackPane);
        else{
            Image imageZOI = ToolsProvider.getTheZOI(image8Bit,Integer.parseInt(startXTextfieldZOI.getText()),Integer.parseInt(startYTextfieldZOI.getText()),Integer.parseInt(endXTextfieldZOI.getText()),
                                                        Integer.parseInt(endYTextfieldZOI.getText()));
            Stage stage = new Stage();
            stage.setTitle("Display Zone Of Interest: "+displayModeZOI.getSelectionModel().getSelectedItem());
            stage.setResizable(false); stage.initStyle(StageStyle.UTILITY);
            stage.setScene(new Scene(new LoaderProvider().displayImageAnimationLoader(imageZOI,displayModeZOI.getSelectionModel().getSelectedItem(),true)));
            stage.show();
        }
    }

    @FXML
    void startXOnKeyReleased(KeyEvent event) {
        String startX = startXTextfieldZOI.getText();
        try {
            int sX = Integer.parseInt(startX);
            ToolsProvider.checkIfNumberNegative(sX);
            isStartX = true;
            if(isStartX && isStartY && isEndX && isEndY) {
                dispayZOIButton.setDisable(false);
                ZOILabelError.setText("");
            }
        }catch (NumberFormatException nfe){
            isStartX = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        } catch (NegativeNumberException e) {
            isStartX = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        }
    }

    @FXML
    void startYOnKeyReleased(KeyEvent event) {
        String startY = startYTextfieldZOI.getText();
        try {
            int sY = Integer.parseInt(startY);
            ToolsProvider.checkIfNumberNegative(sY);
            isStartY = true;
            if(isStartX && isStartY && isEndX && isEndY) {
                dispayZOIButton.setDisable(false);
                ZOILabelError.setText("");
            }
        }catch (NumberFormatException nfe){
            isStartY = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        } catch (NegativeNumberException e) {
            isStartY = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        }
    }

    @FXML
    void endXOnKeyReleased(KeyEvent event) {
        String endX = endXTextfieldZOI.getText();
        try {
            int eX = Integer.parseInt(endX);
            ToolsProvider.checkIfNumberNegative(eX);
            isEndX = true;
            if(isStartX && isStartY && isEndX && isEndY) {
                dispayZOIButton.setDisable(false);
                ZOILabelError.setText("");
            }
        }catch (NumberFormatException nfe){
            isEndX = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        } catch (NegativeNumberException e) {
            isEndX = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        }
    }

    @FXML
    void endYOnKeyReleased(KeyEvent event) {
        String endY = endYTextfieldZOI.getText();
        try{
            int eY = Integer.parseInt(endY);
            ToolsProvider.checkIfNumberNegative(eY);
            isEndY = true;
            if(isStartX && isStartY && isEndX && isEndY) {
                dispayZOIButton.setDisable(false);
                ZOILabelError.setText("");
            }
        }catch (NumberFormatException nfe){
            isEndY = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        } catch (NegativeNumberException e) {
           isEndY = false;
            ZOILabelError.setText("Only positive whole \nnumber accepted");
            dispayZOIButton.setDisable(true);
        }
    }

    public Image getImageToDisplay() {
        return imageToDisplay;
    }

    public void setImageToDisplay(Image imageToDisplay) {
        this.imageToDisplay = imageToDisplay;
    }

    public HomePageController getHomePageController() {
        return homePageController;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    public LinkedList<String> initDisplayMethod(){
        LinkedList<String> store = new LinkedList<>();
        store.add(PIXEL_BY_PIXEL);
        store.add(LINE_PER_LINE);
        store.add(GLOBAL);
        return store;
    }

    public void colorAssigner() {
        arrayOfColor = ToolsProvider.ImageOverview(image8Bit,arrayOfColor);
    }

    @FXML//method in charge of displaying the panel for the 8 bit version
    void getThe8bitButtonOnAction(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Image 8 bit version");
        stage.setScene(new Scene(new LoaderProvider().display8BitVersionOf16BitLoader(image8Bit)));
        stage.show();
    }

    @FXML
    void customDisplayOnAction(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Custom Display");
        stage.setScene(new Scene(new LoaderProvider().customDisplayPreloader(image8Bit)));
        stage.setResizable(false);
        stage.show();
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    //method to set the value of image8Bit.
    public void setImage8Bit(Image imageToDisplay){
        if(imageToDisplay.getFormat().equals(Image.FORMAT_16BITS_INTENSITIES))
            image8Bit = ToolsProvider.convert16BitIntensitiesIn8bit(imageToDisplay);
        else if(imageToDisplay.getFormat().equals(Image.FORMAT_16BITS_AMPLITUDE))
            image8Bit = ToolsProvider.convert16bitAmplitudeIn8bit(imageToDisplay);
        else
            image8Bit = imageToDisplay;
    }
}
