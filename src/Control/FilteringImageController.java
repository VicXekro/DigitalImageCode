package Control;

import Model.*;
import View.LoaderProvider;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.function.ToLongBiFunction;

/**
 * Created by Xekro on 5/7/2017.
 */
public class FilteringImageController implements Initializable{

    private Image imageToFilter;
    private boolean isCustomNC = true, isCustomNL = true;

    /**Linear Mask Controls*/
    @FXML
    private JFXButton customLinearMaskValidateButton;
    @FXML
    private JFXComboBox<String> predefLinearMaskBorderFillingComboBox;
    @FXML
    private JFXComboBox<String> predefLinearMaskTypeComboBox;
    @FXML
    private AnchorPane customLinearFilterAnchorPane;
    @FXML
    private JFXComboBox<String> customLinearMaskBorderFillingComboBox;
    @FXML
    private Pane customLinearMaskControlCreationPanel;
    @FXML
    private GridPane customLinearMaskgridPane;
    @FXML
    private Label customLinearMaskNCLabelError;
    @FXML
    private VBox customLinearMaskgridPaneVBox;
    @FXML
    private JFXTextField customLinearMaskNCTextField;
    @FXML
    private JFXToggleButton linearMaskToggleButton;
    @FXML
    private JFXTextField customLinearMaskNLTextField;
    @FXML
    private Label customLinearMaskNLLabelError;
    @FXML
    private AnchorPane predefineLinearFilterAnchorPane;
    @FXML
    private JFXComboBox<LinearMask> predefLinearMaskDirectionComboBox;
    @FXML
    private JFXButton customLinearMaskOKButton;
    @FXML
    private StackPane stackPane;

    /**Adaptative Filter Controller*/
    @FXML
    private JFXComboBox<String> adaptiveFilterFilteringMethodComboBox;
    @FXML
    private JFXTextField adaptiveFilterNumberOfViewTextField;
    @FXML
    private JFXComboBox<String> adaptiveFilterBorderFillingComboBox;
    @FXML
    private JFXButton adaptiveFilterValidateButton;
    @FXML
    private JFXComboBox<Integer> adaptiveFilterImageFactorComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageToFilter = ToolsProvider.convertImage(imageToFilter);
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
        customLinearMaskBorderFillingComboBox.getItems().setAll(ToolsProvider.initializeBorderFilllingCombo());
        predefLinearMaskBorderFillingComboBox.getItems().setAll(ToolsProvider.initializeBorderFilllingCombo());
        predefLinearMaskTypeComboBox.getItems().setAll(initLinearMaskType());
        predefLinearMaskTypeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                predefLinearMaskDirectionComboBox.getSelectionModel().clearSelection();
                predefLinearMaskDirectionComboBox.getItems().setAll(initLinearMaskDirection(newValue));
            }
        });
        customLinearMaskSetRightControlVisible(false);

        //Adaptive Filter
        adaptiveFilterBorderFillingComboBox.getItems().setAll(ToolsProvider.initializeBorderFilllingCombo());
        adaptiveFilterFilteringMethodComboBox.getItems().setAll(initializeAdaptiveFilteringMethod());
        adaptiveFilterImageFactorComboBox.getItems().setAll(initializeImageFactor());
    }

    @FXML
    void predefLinearMaskValidateButtonOnAction(ActionEvent event) {
        if(predefLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem()==null || predefLinearMaskDirectionComboBox.getSelectionModel().getSelectedItem()==null
                || predefLinearMaskTypeComboBox.getSelectionModel().getSelectedItem()==null)
            ToolsProvider.displayErrorDialog("All fields should be filled", stackPane);
        else{
            LinearMask linearMask = predefLinearMaskDirectionComboBox.getSelectionModel().getSelectedItem();
            linearMask.setBorderFeeling(predefLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);stage.setResizable(false);
            stage.setTitle("Image Filtering Result: "+ linearMask.getName());
            stage.setScene(new Scene(new LoaderProvider().imageLinearFilteringResultLoader(imageToFilter,linearMask)));
            stage.show();
        }
    }

    @FXML
    void customLinearMaskNCTextFieldOnKeyReleased(KeyEvent event) {
        String NC = customLinearMaskNCTextField.getText();
        try {
            int a = Integer.parseInt(NC);
            ToolsProvider.checkIfNumberNegative(a);
            isCustomNC = true;
            if(isCustomNC && isCustomNL) {
                customLinearMaskNCLabelError.setText("");
                customLinearMaskOKButton.setDisable(false);
            }
        }catch (NumberFormatException nfe){
            isCustomNC = false;
            customLinearMaskNCLabelError.setText("Only positive whole \nnumber accepted");
            customLinearMaskOKButton.setDisable(true);
        } catch (NegativeNumberException e) {
            isCustomNC = false;
            customLinearMaskNCLabelError.setText("Only positive whole \nnumber accepted");
            customLinearMaskOKButton.setDisable(true);
        }
    }
    @FXML
    void customLinearMaskNLTextFieldOnKeyReleased(KeyEvent event) {
        String NL = customLinearMaskNLTextField.getText();
        try{
            int a = Integer.parseInt(NL);
            ToolsProvider.checkIfNumberNegative(a);
            isCustomNL = true;
            if(isCustomNL && isCustomNC){
                customLinearMaskNLLabelError.setText("");
                customLinearMaskOKButton.setDisable(false);
            }
        }catch (NumberFormatException nfe){
            isCustomNL = false;
            customLinearMaskNLLabelError.setText("Only positive whole \nnumber accepted");
            customLinearMaskOKButton.setDisable(true);
        } catch (NegativeNumberException e) {
            isCustomNL = false;
            customLinearMaskNLLabelError.setText("Only positive whole \nnumber accepted");
            customLinearMaskOKButton.setDisable(true);
        }
    }

    @FXML
    void customLinearMaskOKButtonOnAction(ActionEvent event) {
        if(customLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem() == null || customLinearMaskNCTextField.getText().equals("")||
                customLinearMaskNLTextField.getText().equals(""))
            ToolsProvider.displayErrorDialog("All fields should be filled",stackPane);
        else{
            int NC = Integer.parseInt(customLinearMaskNCTextField.getText()), NL = Integer.parseInt(customLinearMaskNLTextField.getText());
            customLinearMaskSetRightControlVisible(true);
            for (int i = 0; i < NL; i++) {
                for (int j = 0; j < NC; j++) {
                    TextField textField = new TextField();
                    textField.setPrefWidth(40);
                    textField.setStyle("-fx-font-family: Roboto");
                    customLinearMaskgridPane.add(textField, j, i);
                }
            }
            customLinearMaskControlCreationPanel.setDisable(true);
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
            int[][]mask =  new int[NL][NC];
            for (int i = 0; i <NL ; i++) {
                for (int j = 0; j <NC ; j++) {
                    TextField t = (TextField)customLinearMaskgridPane.getChildren().get(k);
                    mask[i][j] = Integer.parseInt(t.getText());
                    k++;
                }
            }
            LinearMask linearMask = new LinearMask(NC,NL,NC/2, NL/2, mask);
            linearMask.setBorderFeeling(customLinearMaskBorderFillingComboBox.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setTitle("Filter Image: Result"); stage.initStyle(StageStyle.UTILITY);stage.setResizable(false);
            stage.setScene(new Scene(new LoaderProvider().imageLinearFilteringResultLoader(imageToFilter,linearMask)));
            stage.show();
        }
    }

    @FXML
    void customLinearMaskNewLinearMaskButtonOnAction(ActionEvent event) {
        customLinearMaskgridPane.getChildren().clear();
        customLinearMaskSetRightControlVisible(false);
        customLinearMaskControlCreationPanel.setDisable(false);
        customLinearMaskNCTextField.clear();
        customLinearMaskNLTextField.clear();
        customLinearMaskBorderFillingComboBox.getSelectionModel().clearSelection();
    }

    public Image getImageToFilter() {
        return imageToFilter;
    }

    public void setImageToFilter(Image imageToFilter) {
        this.imageToFilter = imageToFilter;
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

    //method to set the VBox containing the gridPane and the Validate button visible or not on the CustomLinearMask
    private void customLinearMaskSetRightControlVisible(boolean b) {
        customLinearMaskValidateButton.setVisible(b);
        customLinearMaskgridPaneVBox.setVisible(b);
    }

    /**Adaptive filter controller*/
    @FXML
    void adaptiveFilterValidateButtonOnAction(ActionEvent event) {
        if(adaptiveFilterBorderFillingComboBox.getSelectionModel().getSelectedItem()==null || adaptiveFilterFilteringMethodComboBox.getSelectionModel().getSelectedItem()==null||
                adaptiveFilterImageFactorComboBox.getSelectionModel().getSelectedItem()==null || adaptiveFilterNumberOfViewTextField.getText().equals(""))
            ToolsProvider.displayErrorDialog("All fields should be filled", stackPane);
        else{
            int n = Filter.imageWindowSize(imageToFilter);
            int N = Integer.parseInt(adaptiveFilterNumberOfViewTextField.getText());
            int Q = adaptiveFilterImageFactorComboBox.getSelectionModel().getSelectedItem();
            ImageWindow imageWindow = new ImageWindow(n);
            imageWindow.setBorderFeeling(adaptiveFilterBorderFillingComboBox.getSelectionModel().getSelectedItem());
            imageWindow.setImageFactor(Q);
            imageWindow.setNumberOfViews(N);
            imageWindow.setAdaptiveFilterMethod(adaptiveFilterFilteringMethodComboBox.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);stage.setResizable(false);
            stage.setTitle("Image Filtering Result: "+ adaptiveFilterFilteringMethodComboBox.getSelectionModel().getSelectedItem());
            stage.setScene(new Scene(new LoaderProvider().imageAdaptiveFilteringResultLoader(imageToFilter,imageWindow)));
            stage.show();
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
}
