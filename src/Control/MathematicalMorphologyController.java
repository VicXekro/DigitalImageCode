package Control;

import Model.Image;
import Model.Mask;
import Model.Morphology;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.tools.Tool;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * Created by Xekro on 5/14/2017.
 */
public class MathematicalMorphologyController implements Initializable{

    private Image imageToTransform;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private String operation = Morphology.BINARIZATION;

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXRadioButton erosionRadioButton;
    @FXML
    private JFXRadioButton closureRadioButton;
    @FXML
    private JFXRadioButton blackTopHatRadioButton;
    @FXML
    private JFXRadioButton whiteTopHatRadioButton;
    @FXML
    private JFXRadioButton dilationRadioButton;
    @FXML
    private JFXRadioButton binarizationRadioButton;
    @FXML
    private JFXRadioButton openingRadioButton;
    @FXML
    private JFXRadioButton skeletisationRadioButton;

    @FXML
    private JFXTextField binarizationThreshold1;
    @FXML
    private JFXTextField binarizationThreshold2;
    @FXML
    private AnchorPane binarizeAnchorPane;

    /**Complex Operation Controls*/
    @FXML
    private JFXButton complexOperationValidateButton;
    @FXML
    private GridPane complexOperationGridPane;
    @FXML
    private JFXTextField complexOperationNL;
    @FXML
    private AnchorPane complexOperationAnchorPane;
    @FXML
    private JFXTextField complexOperationNC;
    @FXML
    private JFXComboBox<String> complexOperationBorderFilling;
    @FXML
    private JFXButton complexOperationOKButton;
    @FXML
    private JFXTextField complexOperationOrder;
    @FXML
    private Label complexOperationThresholdLabel;
    @FXML
    private JFXTextField complexOperationThreshold2;
    @FXML
    private JFXTextField complexOperationThreshold1;

    /**Skeletisation Components*/
    @FXML
    private AnchorPane skeletisationAnchorPane;
    @FXML
    private JFXComboBox<String> skeletisationBorderFilling;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDownComplexOperationComponentVisible(false);
        complexOperationBorderFilling.getItems().setAll(ToolsProvider.initializeBorderFilllingCombo());
        skeletisationBorderFilling.getItems().setAll(ToolsProvider.initializeBorderFilllingCombo());
        complexOperationAnchorPane.setVisible(false); binarizeAnchorPane.setVisible(true);skeletisationAnchorPane.setVisible(false);
        binarizationRadioButton.setSelected(true);
        imageToTransform = ToolsProvider.convertImage(imageToTransform);
        setToggleGroup();
        setColor();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                JFXRadioButton tempRadioButton = (JFXRadioButton)newValue;
                if (tempRadioButton.equals(binarizationRadioButton)){
                    operation = Morphology.BINARIZATION;
                    if(!binarizeAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,1);
                    binarizationThreshold1.clear(); binarizationThreshold2.clear();
                }
                else if (tempRadioButton.equals(erosionRadioButton)){
                    operation = Morphology.EROSION;
                    if(!complexOperationAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,2);
                    setDownComplexOperationComponentVisible(false);
                    clearComplexOperationComponent();
                    setUpComplexOperationComponentDisable(false);
                    setComplexOperationBinarizationComponentVisible(false);
                }
                else if (tempRadioButton.equals(dilationRadioButton)){
                    operation = Morphology.DILATION;
                    if(!complexOperationAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,2);
                    setDownComplexOperationComponentVisible(false);
                    clearComplexOperationComponent();
                    setUpComplexOperationComponentDisable(false);
                    setComplexOperationBinarizationComponentVisible(false);
                }
                else if (tempRadioButton.equals(openingRadioButton)){
                    operation = Morphology.OPENING;
                    if(!complexOperationAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,2);
                    setDownComplexOperationComponentVisible(false);
                    clearComplexOperationComponent();
                    setUpComplexOperationComponentDisable(false);
                    setComplexOperationBinarizationComponentVisible(false);
                }
                else if (tempRadioButton.equals(closureRadioButton)){
                    operation = Morphology.CLOSURE;
                    if(!complexOperationAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,2);
                    setDownComplexOperationComponentVisible(false);
                    clearComplexOperationComponent();
                    setUpComplexOperationComponentDisable(false);
                    setComplexOperationBinarizationComponentVisible(false);
                }
                else if (tempRadioButton.equals(whiteTopHatRadioButton)){
                    operation = Morphology.WHITETOPHAT;
                    if(!complexOperationAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,2);
                    setDownComplexOperationComponentVisible(false);
                    clearComplexOperationComponent();
                    setUpComplexOperationComponentDisable(false);
                    setComplexOperationBinarizationComponentVisible(true);
                }
                else if (tempRadioButton.equals(blackTopHatRadioButton)){
                    operation = Morphology.BLACKTOPHAT;
                    if(!complexOperationAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,2);
                    setDownComplexOperationComponentVisible(false);
                    clearComplexOperationComponent();
                    setUpComplexOperationComponentDisable(false);
                    setComplexOperationBinarizationComponentVisible(true);
                }
                else if (tempRadioButton.equals(skeletisationRadioButton)){
                    operation = Morphology.SKELETISATION;
                    if(!skeletisationAnchorPane.isVisible())
                        interchangeChangeAnchorPanel(true,3);
                }
            }
        });
    }

    @FXML
    void binarizationValidateButtonOnAction(ActionEvent event) {
        if(binarizationCheckValuesEntered()) {
            int a = Integer.parseInt(binarizationThreshold1.getText());
            int b = Integer.parseInt(binarizationThreshold2.getText());
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setResizable(false);
            setComplexOperationBinarizationComponentVisible(false);
            stage.setTitle("Mathematical Morphology: "+operation+" result");
            stage.setScene(new Scene(new LoaderProvider().mathematicalMorphologyResultBinarizeLoader(imageToTransform,operation,a,b)));
            stage.show();
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
                ToolsProvider.displayErrorDialog(msg, stackPane);
                checker = false;
            }
            else if (a>b){
                ToolsProvider.displayErrorDialog(msg,stackPane);
                checker = false;
            }else if(a>255 || b>255){
                ToolsProvider.displayErrorDialog(msg,stackPane);
                checker = false;
            }
        }catch (NumberFormatException e){
            ToolsProvider.displayErrorDialog(msg,stackPane);
            checker = false;
        }
        return checker;
    }

    /**Complex Operation method*/

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
    void complexOperationValidateButtonOnAction(ActionEvent event) {
        if(checkMaskValue()){
            int a = Integer.parseInt(complexOperationNC.getText()), c = Integer.parseInt(complexOperationOrder.getText());
            int b = Integer.parseInt(complexOperationNL.getText()), k =0;
            int[][] maskTable = new int[b][a];
            for (int i = 0; i <b ; i++) {
                for (int j = 0; j <a ; j++) {
                    TextField t = (TextField) complexOperationGridPane.getChildren().get(k);
                    maskTable[i][j] = Integer.parseInt(t.getText());
                    k++;
                }
            }
            Mask mask = new Mask(a,b,a/2,b/2,maskTable,c);
            mask.setBorderFeeling(complexOperationBorderFilling.getSelectionModel().getSelectedItem());
            mask.setMorphologicalOperation(operation);
            if(!complexOperationThreshold1.getText().equals("") && !complexOperationThreshold2.getText().equals("")){
                int threshold1 = Integer.parseInt(complexOperationThreshold1.getText()), threshold2 = Integer.parseInt(complexOperationThreshold2.getText());
                mask.setThreshold1(threshold1);
                mask.setThreshold2(threshold2);
                mask.setThreshHoldDefine(true);
            }
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setResizable(false); stage.setTitle("Mathematical Morphology: "+operation+ " result");
            stage.setScene(new Scene(new LoaderProvider().mathematicalMorphologyResultComplexOperation(imageToTransform,mask)));
            stage.show();
        }
    }

    @FXML
    void complexOperationNewMaskButtonOnAction(ActionEvent event) {
        clearComplexOperationComponent();
        setDownComplexOperationComponentVisible(false);
        setUpComplexOperationComponentDisable(false);
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

    private void clearComplexOperationComponent(){
        complexOperationNC.clear();
        complexOperationNL.clear();
        complexOperationOrder.clear();
        complexOperationGridPane.getChildren().clear();
        complexOperationThreshold1.clear();
        complexOperationThreshold2.clear();
        complexOperationBorderFilling.getSelectionModel().clearSelection();
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
                ToolsProvider.displayErrorDialog(msg,stackPane);
                b = false;
            }
            else if(operation.equals(Morphology.BLACKTOPHAT)||operation.equals(Morphology.WHITETOPHAT)){
                int e = Integer.parseInt(complexOperationThreshold1.getText());
                int f = Integer.parseInt(complexOperationThreshold2.getText());
                if(e< 0 || f < 0){
                    ToolsProvider.displayErrorDialog(msg,stackPane);
                    b = false;
                }
            }
        }catch (NumberFormatException e){
            ToolsProvider.displayErrorDialog(msg,stackPane);
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
                    ToolsProvider.displayErrorDialog(msg,stackPane);
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


    public Image getImageToTransform() {
        return imageToTransform;
    }

    public void setImageToTransform(Image imageToTransform) {
        this.imageToTransform = imageToTransform;
    }

    private void setToggleGroup(){
        erosionRadioButton.setToggleGroup(toggleGroup);
        closureRadioButton.setToggleGroup(toggleGroup);
        binarizationRadioButton.setToggleGroup(toggleGroup);
        dilationRadioButton.setToggleGroup(toggleGroup);
        blackTopHatRadioButton.setToggleGroup(toggleGroup);
        whiteTopHatRadioButton.setToggleGroup(toggleGroup);
        openingRadioButton.setToggleGroup(toggleGroup);
        skeletisationRadioButton.setToggleGroup(toggleGroup);
    }

    private void setColor(){
        erosionRadioButton.setSelectedColor(Color.web("#C8AD7F"));
        closureRadioButton.setSelectedColor(Color.web("#C8AD7F"));
        binarizationRadioButton.setSelectedColor(Color.web("#C8AD7F"));
        dilationRadioButton.setSelectedColor(Color.web("#C8AD7F"));
        blackTopHatRadioButton.setSelectedColor(Color.web("#C8AD7F"));
        whiteTopHatRadioButton.setSelectedColor(Color.web("#C8AD7F"));
        openingRadioButton.setSelectedColor(Color.web("#C8AD7F"));
        skeletisationRadioButton.setSelectedColor(Color.web("#C8AD7F"));
    }

    //if binarizeAnchorpane visible change with complexOperationAnchorpane
    private void interchangeChangeAnchorPanel(boolean b, int page){
        if(page == 1) {
            binarizeAnchorPane.setVisible(b);
            complexOperationAnchorPane.setVisible(!b);
            skeletisationAnchorPane.setVisible(!b);
        }else if (page == 2){
            binarizeAnchorPane.setVisible(!b);
            complexOperationAnchorPane.setVisible(b);
            skeletisationAnchorPane.setVisible(!b);
        }else if (page == 3){
            binarizeAnchorPane.setVisible(!b);
            complexOperationAnchorPane.setVisible(!b);
            skeletisationAnchorPane.setVisible(b);
        }
    }

    private void setComplexOperationBinarizationComponentVisible(boolean b){
        complexOperationThreshold1.setVisible(b);
        complexOperationThreshold2.setVisible(b);
        complexOperationThresholdLabel.setVisible(b);
    }

    /**Skeletisation method*/
    @FXML
    void skeletisationValidateButtonOnAction(ActionEvent event) {
        if(skeletisationBorderFilling.getSelectionModel().getSelectedItem()==null)
            ToolsProvider.displayErrorDialog("Select a border filling method",stackPane);
        else{
            Mask mask = new Mask();
            mask.setBorderFeeling(skeletisationBorderFilling.getSelectionModel().getSelectedItem());
            mask.setCenterX(3/2); mask.setCenterY(3/2); mask.setNLmask(3); mask.setNCmask(3);
            mask.setMorphologicalOperation(operation);
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setResizable(false); stage.setTitle("Mathematical Morphology: "+operation+ " result");
            stage.setScene(new Scene(new LoaderProvider().mathematicalMorphologyResultComplexOperation(imageToTransform,mask)));
            stage.show();
        }
    }

}
