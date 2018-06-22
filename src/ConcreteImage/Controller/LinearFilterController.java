package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import Model.Filter;
import Model.LinearMask;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import Control.ToolsProvider;

/**
 * Created by Xekro on 6/5/2017.
 */
public class LinearFilterController implements Initializable{

    private Image imageToFilter;
    private StackPane displayerStackPane;
    private HomePageController homePageController;

    @FXML
    private JFXButton customLinearMaskOKButton;
    @FXML
    private JFXButton customLinearMaskValidateButton;
    @FXML
    private JFXTextField customLinearMaskNCTextField;
    @FXML
    private JFXComboBox<String> predefLinearMaskBorderFillingComboBox;
    @FXML
    private JFXComboBox<String> predefLinearMaskTypeComboBox;
    @FXML
    private JFXToggleButton linearMaskToggleButton;
    @FXML
    private AnchorPane customLinearFilterAnchorPane;
    @FXML
    private JFXTextField customLinearMaskNLTextField;
    @FXML
    private JFXComboBox<String> customLinearMaskBorderFillingComboBox;
    @FXML
    private GridPane customLinearMaskgridPane;
    @FXML
    private AnchorPane predefineLinearFilterAnchorPane;
    @FXML
    private JFXComboBox<LinearMask> predefLinearMaskDirectionComboBox;
    @FXML
    private Pane customLinearMaskControlCreationPanel;
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        linearMaskToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    predefineLinearFilterAnchorPane.setVisible(false);
                    customLinearFilterAnchorPane.setVisible(true);
                    linearMaskToggleButton.setText("Custom Linear Filter");
                }else {
                    predefineLinearFilterAnchorPane.setVisible(true);
                    customLinearFilterAnchorPane.setVisible(false);
                    linearMaskToggleButton.setText("Predefine Linear Filter");
                }
            }
        });

        customLinearMaskBorderFillingComboBox.getItems().setAll(Control.ToolsProvider.initializeBorderFilllingCombo());
        predefLinearMaskBorderFillingComboBox.getItems().setAll(Control.ToolsProvider.initializeBorderFilllingCombo());
        predefLinearMaskTypeComboBox.getItems().setAll(initLinearMaskType());
        predefLinearMaskTypeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                predefLinearMaskDirectionComboBox.getSelectionModel().clearSelection();
                predefLinearMaskDirectionComboBox.getItems().setAll(initLinearMaskDirection(newValue));
            }
        });
        customLinearMaskSetDownControlVisible(false);
    }

    @FXML
    void customLinearMaskNewLinearMaskButtonOnAction(ActionEvent event) {
        customLinearMaskgridPane.getChildren().clear();
        customLinearMaskSetDownControlVisible(false);
        customLinearMaskControlCreationPanel.setDisable(false);
        customLinearMaskNCTextField.clear();
        customLinearMaskNLTextField.clear();
        customLinearMaskBorderFillingComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void predefLinearMaskValidateButtonOnAction(ActionEvent event) {
        if(predefLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem()==null || predefLinearMaskDirectionComboBox.getSelectionModel().getSelectedItem()==null
                 || predefLinearMaskTypeComboBox.getSelectionModel().getSelectedItem()==null)
        ToolsProvider.displayErrorDialog("All fields should be filled", stackPane);
        else{
            LinearMask linearMask = predefLinearMaskDirectionComboBox.getSelectionModel().getSelectedItem();
            linearMask.setBorderFeeling(predefLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem());
            try {
                Image image = ConcreteImage.Controller.ToolsProvider.linearConvolution(imageToFilter, linearMask);
                ConcreteImage.Controller.ToolsProvider.reloadImage(image, displayerStackPane, homePageController);
            }catch (OutOfMemoryError e){
                ConcreteImage.Controller.ToolsProvider.displayAlertMemory();
            }
        }
    }

    @FXML
    void customLinearMaskValidateButtonOnAction(ActionEvent event) {
        if(!customLinearMaskCheckLinearMaskValidity())
            ToolsProvider.displayErrorDialog("- All cells should be filled with a value. \n" +
                    "- Only whole numbers accepted.",stackPane);
        else {
            int NL = Integer.parseInt(customLinearMaskNLTextField.getText());
            int NC = Integer.parseInt(customLinearMaskNCTextField.getText()), k = 0;
            int[][] mask = new int[NL][NC];
            for (int i = 0; i < NL; i++) {
                for (int j = 0; j < NC; j++) {
                    TextField t = (TextField) customLinearMaskgridPane.getChildren().get(k);
                    mask[i][j] = Integer.parseInt(t.getText());
                    k++;
                }
            }
            LinearMask linearMask = new LinearMask(NC, NL, NC / 2, NL / 2, mask);
            linearMask.setBorderFeeling(customLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem());
            Image image = ConcreteImage.Controller.ToolsProvider.linearConvolution(imageToFilter,linearMask);
            ConcreteImage.Controller.ToolsProvider.reloadImage(image,displayerStackPane,homePageController);
        }
    }

    @FXML
    void customLinearMaskOKButtonOnAction(ActionEvent event) {
        if(customLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem() == null || customLinearMaskNCTextField.getText().equals("")||
                customLinearMaskNLTextField.getText().equals(""))
            ToolsProvider.displayErrorDialog("All fields should be filled",stackPane);
        else{
            int NC = Integer.parseInt(customLinearMaskNCTextField.getText()), NL = Integer.parseInt(customLinearMaskNLTextField.getText());
            customLinearMaskSetDownControlVisible(true);
            for (int i = 0; i < NL; i++) {
                for (int j = 0; j < NC; j++) {
                    TextField textField = new TextField();
                    textField.setPrefWidth(30);
                    textField.setStyle("-fx-font-family: Roboto");
                    customLinearMaskgridPane.add(textField, j, i);
                }
            }
            customLinearMaskControlCreationPanel.setDisable(true);
        }
    }

    //method to set the VBox containing the gridPane and the Validate button visible or not on the CustomLinearMask
    private void customLinearMaskSetDownControlVisible(boolean b) {
        customLinearMaskValidateButton.setVisible(b);
        customLinearMaskgridPane.setVisible(b);
    }

    //Check the different entries of linear mask
    public boolean customLinearMaskCheckLinearMaskValidity(){
        for (Node node: customLinearMaskgridPane.getChildren()){
            TextField textField = (TextField) node;
            if(textField.getText().equals(""))
                return false;
            else {
                try {
                    int a = Integer.parseInt(textField.getText());
                }catch (NumberFormatException nfe){
                    return false;
                }
            }
        }
        return true;
    }

    //method to load the different linear mask type
    private LinkedList<String> initLinearMaskType(){
        LinkedList<String> store = new LinkedList<>();
        store.add(Filter.ROBERT);
        store.add(Filter.SOBEL);
        store.add(Filter.PREWITT);
        store.add(Filter.KIRSH);
        store.add(Filter.AVERAGE);
        return store;
    }

    //method to load the different linear mask in function of type
    private LinkedList<LinearMask> initLinearMaskDirection(String type){
        LinkedList<LinearMask> store = new LinkedList<>();
        if(type.equals(Filter.SOBEL)){
            store.clear();
            store.add(Filter.SOBEL_HORIZONTAL);
            store.add(Filter.SOBEL_VERTICAL);
            store.add(Filter.SOBEL_45);
            store.add(Filter.SOBEL_135);
        }
        else if(type.equals(Filter.ROBERT)){
            store.clear();
            store.add(Filter.ROBERT_HORIZONTAL);
            store.add(Filter.ROBERT_VERTICAL);
            store.add(Filter.ROBERT_45);
            store.add(Filter.ROBERT_135);
        }
        else if(type.equals(Filter.KIRSH)){
            store.clear();
            store.add(Filter.KIRSH_HORIZONTAL);
            store.add(Filter.KIRSH_VERTICAL);
            store.add(Filter.KIRSH_45);
            store.add(Filter.KIRSH_135);
        }
        else if(type.equals(Filter.PREWITT)){
            store.clear();
            store.add(Filter.PREWITT_HORIZONTAL);
            store.add(Filter.PREWITT_VERTICAL);
            store.add(Filter.PREWITT_45);
            store.add(Filter.PREWITT_135);
        }
        else if(type.equals(Filter.AVERAGE)){
            store.clear();
            store.add(Filter.AVERAGE_MASK);
        }
        return store;
    }

    public void setImageToFilter(Image imageToFilter) {
        this.imageToFilter = imageToFilter;
    }

    public void setDisplayerStackPane(StackPane displayerStackPane) {
        this.displayerStackPane = displayerStackPane;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
}
