package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import Control.*;
import Control.ToolsProvider;
import Model.Mask;
import Model.Morphology;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/8/2017.
 */
public class ComplexMorphologycalOperationController implements Initializable{

    private Image imageToTransform;
    private StackPane parenStackPane;
    private String operation;
    private HomePageController homePageController;

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXTextField complexOperationNC;
    @FXML
    private JFXButton complexOperationValidateButton;
    @FXML
    private Label complexOperationThresholdLabel;
    @FXML
    private JFXButton complexOperationOKButton;
    @FXML
    private JFXComboBox<String> complexOperationBorderFilling;
    @FXML
    private JFXTextField complexOperationOrder;
    @FXML
    private JFXTextField complexOperationThreshold2;
    @FXML
    private GridPane complexOperationGridPane;
    @FXML
    private JFXTextField complexOperationNL;
    @FXML
    private JFXTextField complexOperationThreshold1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        complexOperationBorderFilling.getItems().setAll(Control.ToolsProvider.initializeBorderFilllingCombo());
        if(!(operation.equals(Morphology.WHITETOPHAT)) && !(operation.equals(Morphology.BLACKTOPHAT)))
            setThresholdPartVisible(false);
    }

    @FXML
    void complexOperationOKButtonOnAction(ActionEvent event) {
        if(checkComplexOperationEntry()){
            setDownComplexOperationComponentVisible(true);
            int a = Integer.parseInt(complexOperationNC.getText());
            int b = Integer.parseInt(complexOperationNL.getText());
            for (int i = 0; i <b ; i++) {
                for (int j = 0; j < a ; j++) {
                    TextField t = new TextField();
                    t.setPrefWidth(30); t.setStyle("-fx-font-family: Roboto");
                    complexOperationGridPane.add(t,j,i);
                }
            }
            setUpComplexOperationComponentDisable(true);
        }
    }

    @FXML
    void complexOperationNewMaskButtonOnAction(ActionEvent event) {
        clearComplexOperationComponent();
        setUpComplexOperationComponentDisable(false);
        setDownComplexOperationComponentVisible(false);
    }

    @FXML
    void complexOperationValidateButtonOnAction(ActionEvent event) {
        if(checkMaskValue()) {
            int a = Integer.parseInt(complexOperationNC.getText()), c = Integer.parseInt(complexOperationOrder.getText());
            int b = Integer.parseInt(complexOperationNL.getText()), k = 0;
            int[][] maskTable = new int[b][a];
            for (int i = 0; i < b; i++) {
                for (int j = 0; j < a; j++) {
                    TextField t = (TextField) complexOperationGridPane.getChildren().get(k);
                    maskTable[i][j] = Integer.parseInt(t.getText());
                    k++;
                }
            }
            Mask mask = new Mask(a, b, a / 2, b / 2, maskTable, c);
            mask.setBorderFeeling(complexOperationBorderFilling.getSelectionModel().getSelectedItem());
            mask.setMorphologicalOperation(operation);
            if (!complexOperationThreshold1.getText().equals("") && !complexOperationThreshold2.getText().equals("")) {
                int threshold1 = Integer.parseInt(complexOperationThreshold1.getText()), threshold2 = Integer.parseInt(complexOperationThreshold2.getText());
                mask.setThreshold1(threshold1);
                mask.setThreshold2(threshold2);
                mask.setThreshHoldDefine(true);
            }
            operationToPerform(mask);
            ConcreteImage.Controller.ToolsProvider.reloadImage(imageToTransform,parenStackPane,homePageController);
        }
    }

    private void clearComplexOperationComponent(){
        complexOperationNC.clear();
        complexOperationNL.clear();
        complexOperationOrder.clear();
        complexOperationGridPane.getChildren().clear();
        complexOperationThreshold1.clear();
        complexOperationThreshold2.clear();
        complexOperationBorderFilling.getSelectionModel().clearSelection();
    }

    private void setUpComplexOperationComponentDisable(boolean b){
        complexOperationNC.setDisable(b);
        complexOperationNL.setDisable(b);
        complexOperationOKButton.setDisable(b);
        complexOperationBorderFilling.setDisable(b);
        complexOperationOrder.setDisable(b);
        complexOperationThreshold1.setDisable(b);
        complexOperationThreshold2.setDisable(b);
    }

    private void setDownComplexOperationComponentVisible(boolean b){
        complexOperationGridPane.setVisible(b);
        complexOperationValidateButton.setVisible(b);
    }

    public void operationToPerform(Mask mask){
        if(operation.equals(Morphology.EROSION))
            for (int i = 0; i < mask.getOrder(); i++)
                imageToTransform = MorphologyOperation.imageErosion(mask, imageToTransform);
        else if(operation.equals(Morphology.DILATION))
            for (int i = 0; i < mask.getOrder(); i++)
                imageToTransform = MorphologyOperation.imageDilation(mask, imageToTransform);
        else if(operation.equals(Morphology.OPENING))
            imageToTransform = MorphologyOperation.imageOpening(mask,imageToTransform);
        else if(operation.equals(Morphology.CLOSURE))
            imageToTransform = MorphologyOperation.imageClosure(mask, imageToTransform);
        else if(operation.equals(Morphology.WHITETOPHAT))
            imageToTransform = MorphologyOperation.imageWhiteTopHat(mask,imageToTransform);
        else if (operation.equals(Morphology.BLACKTOPHAT))
            imageToTransform = MorphologyOperation.imageBlackTopHat(mask,imageToTransform);
    }

    //method to check the conformity of entries for complex operations.
    private boolean checkComplexOperationEntry(){
        String msg = "- All values entered should be positive whole numbers." +
                "\n-All fields should be filled";
        if(operation.equals(Morphology.BLACKTOPHAT)||operation.equals(Morphology.WHITETOPHAT))
            msg+="\n-For Threshold definition; values should positive and be less or equal to 255";
        boolean b = true;
        try {
            int a = Integer.parseInt(complexOperationNC.getText()), d = Integer.parseInt(complexOperationOrder.getText());
            int c = Integer.parseInt(complexOperationNL.getText());
            if(c<= 0 || a<=0 || complexOperationBorderFilling.getSelectionModel().getSelectedItem()==null || d<=0){
                Control.ToolsProvider.displayErrorDialog(msg,stackPane);
                b = false;
            }
            else if(operation.equals(Morphology.BLACKTOPHAT)||operation.equals(Morphology.WHITETOPHAT)){
                int e = Integer.parseInt(complexOperationThreshold1.getText());
                int f = Integer.parseInt(complexOperationThreshold2.getText());
                if(e< 0 || f < 0){
                    Control.ToolsProvider.displayErrorDialog(msg,stackPane);
                    b = false;
                }
            }
        }catch (NumberFormatException e){
            Control.ToolsProvider.displayErrorDialog(msg,stackPane);
            b = false;
        }
        return b;
    }

    private boolean checkMaskValue(){
        boolean b = true;
        String msg = "- Only -1 and 1 are accepted" +
                "\n1 for the enable cells and -1 for the disable cells";
        for (Node n: complexOperationGridPane.getChildren()) {
            TextField t = (TextField) n;
            try {
                int a = Integer.parseInt(t.getText());
                if(a!= -1 && a!=1){
                    Control.ToolsProvider.displayErrorDialog(msg,stackPane);
                    b = false;
                    break;
                }
            }catch (NumberFormatException e){
                ToolsProvider.displayErrorDialog(msg,stackPane);
                b = false;
            }
        }
        return b;
    }

    private void setThresholdPartVisible(boolean b){
        complexOperationThresholdLabel.setVisible(b);
        complexOperationThreshold1.setVisible(b);
        complexOperationThreshold2.setVisible(b);
    }

    public void setImageToTransform(Image imageToTransform) {
        this.imageToTransform = imageToTransform;
    }

    public void setParenStackPane(StackPane parenStackPane) {
        this.parenStackPane = parenStackPane;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }
}
