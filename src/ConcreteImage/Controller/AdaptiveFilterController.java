package ConcreteImage.Controller;

import ConcreteImage.Model.AdaptiveFilterMethods;
import ConcreteImage.Model.Image;
import Control.*;
import Control.ToolsProvider;
import Model.Filter;
import Model.ImageWindow;
import com.jfoenix.controls.JFXButton;
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
 * Created by Xekro on 6/8/2017.
 */
public class AdaptiveFilterController implements Initializable {

    private Image imageToFilter;
    private StackPane parentStackPane;
    private HomePageController homePageController;

    @FXML
    private JFXButton adaptiveFilterValidateButton;
    @FXML
    private JFXComboBox<String> adaptiveFilterFilteringMethodComboBox;
    @FXML
    private JFXComboBox<String> adaptiveFilterBorderFillingComboBox;
    @FXML
    private JFXTextField adaptiveFilterNumberOfViewTextField;
    @FXML
    private JFXComboBox<Integer> adaptiveFilterImageFactorComboBox;
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adaptiveFilterBorderFillingComboBox.getItems().setAll(Control.ToolsProvider.initializeBorderFilllingCombo());
        adaptiveFilterFilteringMethodComboBox.getItems().setAll(initializeAdaptiveFilteringMethod());
        adaptiveFilterImageFactorComboBox.getItems().setAll(initializeImageFactor());
    }

    @FXML
    void adaptiveFilterValidateButtonOnAction(ActionEvent event) {
        if(adaptiveFilterBorderFillingComboBox.getSelectionModel().getSelectedItem()==null || adaptiveFilterFilteringMethodComboBox.getSelectionModel().getSelectedItem()==null||
                adaptiveFilterImageFactorComboBox.getSelectionModel().getSelectedItem()==null || adaptiveFilterNumberOfViewTextField.getText().equals(""))
            ToolsProvider.displayErrorDialog("All fields should be filled", stackPane);
        else{
            int n = AdaptiveFilterMethods.imageWindowSize(imageToFilter);
            int N = Integer.parseInt(adaptiveFilterNumberOfViewTextField.getText());
            int Q = adaptiveFilterImageFactorComboBox.getSelectionModel().getSelectedItem();
            ImageWindow imageWindow = new ImageWindow(n);
            imageWindow.setBorderFeeling(adaptiveFilterBorderFillingComboBox.getSelectionModel().getSelectedItem());
            imageWindow.setImageFactor(Q);
            imageWindow.setNumberOfViews(N);
            imageWindow.setAdaptiveFilterMethod(adaptiveFilterFilteringMethodComboBox.getSelectionModel().getSelectedItem());
            try {
                Image image = AdaptiveFilterMethods.determineAdaptiveFilterToUse(imageWindow,imageToFilter);
                ConcreteImage.Controller.ToolsProvider.reloadImage(image,parentStackPane,homePageController);
            }catch (OutOfMemoryError error){
                ConcreteImage.Controller.ToolsProvider.displayAlertMemory();
            }

        }
    }

    //method to initialize adaptiveFilteringMethod ComboBox
    public LinkedList<String> initializeAdaptiveFilteringMethod(){
        LinkedList<String> store = new LinkedList<>();
        store.add(Filter.LEE_FILTER);
        store.add(Filter.ALTERNATED_LEE_FILTER);
        store.add(Filter.MAP_FILTER);
        store.add(Filter.GAMMA_FILTER);
        store.add(Filter.FROST_FILTER);
        return  store;
    }

    //method to initialize adaptiveFilterImageFactor ComboBox
    public LinkedList<Integer> initializeImageFactor(){
        LinkedList<Integer> store = new LinkedList<>();
        store.add(0); store.add(1); store.add(2);
        return store;
    }

    public void setImageToFilter(Image imageToFilter) {
        this.imageToFilter = imageToFilter;
    }

    public void setParentStackPane(StackPane parentStackPane) {
        this.parentStackPane = parentStackPane;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
}
