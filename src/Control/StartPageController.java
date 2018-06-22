package Control;

import Model.Band;
import Model.Image;
import ConcreteImage.View.ConcreteImageLoaderProvider;
import View.LoaderProvider;
import com.jfoenix.controls.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 3/19/2017.
 */
public class StartPageController implements Initializable{

    private boolean isNC = true, isNL=true, isBand=true;

    @FXML
    private Label NLError;
    @FXML
    private JFXTextField NC;
    @FXML
    private JFXTextField nbrOfBand;
    @FXML
    private JFXComboBox<String> creationComboBox;
    @FXML
    private JFXTextField NL;
    @FXML
    private JFXComboBox<String> formatComboBox;
    @FXML
    private Label NCError;
    @FXML
    private Label nbrOfBandError;
    @FXML
    private JFXButton generateButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatComboBox.getItems().setAll(initFormat());
        creationComboBox.getItems().setAll(initCreationType());
        progressIndicator.setVisible(false);
    }


    @FXML
    void NCkeyReleased(KeyEvent event) {
        String s = NC.getText();
        try{
            int a = Integer.parseInt(s);
            isNC = true;
            NCError.setText("");
            NC.setFocusColor(Color.WHITE);
            NC.setUnFocusColor(Color.WHITE);
            if(isNC && isNL && isBand)
                generateButton.setDisable(false);
        }
        catch (NumberFormatException e){
            NCError.setText("Only whole numbers accepted");
            isNC = false;
            generateButton.setDisable(true);
            NC.setFocusColor(Color.web("#ff0000"));
            NC.setUnFocusColor(Color.web("#ff0000"));
        }
    }

    @FXML
    void NLkeyReleased(KeyEvent event) {
        String s = NL.getText();
        try{
            int a = Integer.parseInt(s);
            isNL = true;
            NLError.setText("");
            NL.setFocusColor(Color.WHITE);
            NL.setUnFocusColor(Color.WHITE);
            if(isNC && isNL && isBand)
                generateButton.setDisable(false);
        }
        catch (NumberFormatException e){
            NLError.setText("Only whole numbers accepted");
            isNL = false;
            generateButton.setDisable(true);
            NL.setFocusColor(Color.web("#ff0000"));
            NL.setUnFocusColor(Color.web("#ff0000"));
        }
    }

    @FXML
    void nbrBandskeyReleased(KeyEvent event) {
        String s = nbrOfBand.getText();
        try{
            int a = Integer.parseInt(s);
            isBand = true;
            nbrOfBandError.setText("");
            nbrOfBand.setFocusColor(Color.WHITE);
            nbrOfBand.setUnFocusColor(Color.WHITE);
            if(isNC && isNL && isBand)
                generateButton.setDisable(false);
        }
        catch (NumberFormatException e){
            nbrOfBandError.setText("Only whole numbers accepted");
            isBand = false;
            generateButton.setDisable(true);
            nbrOfBand.setFocusColor(Color.web("#ff0000"));
            nbrOfBand.setUnFocusColor(Color.web("#ff0000"));
        }
    }

    @FXML
    void generateOnAction(ActionEvent event) {
        StartPageController pageController = this;
        if(!(isEntriesAreValid()))
            ToolsProvider.displayErrorDialog("All entries should be filled"+"\n >Only whole positive numbers accepted", stackPane);
        else if(Integer.parseInt(nbrOfBand.getText())>3){
            ToolsProvider.displayErrorDialog("Images of more than 3 bands are not yet supported", stackPane);
        }
        else {
            Image img = new Image(Integer.parseInt(NC.getText()), Integer.parseInt(NL.getText()), Integer.parseInt(nbrOfBand.getText()),
                    formatComboBox.getSelectionModel().getSelectedItem(), creationComboBox.getSelectionModel().getSelectedItem());
            for (int i = 0; i < img.getNbrOfBands(); i++) {
                Band band = new Band(img.getNC(), img.getNL(), img.getFormat());
                band.setBandOrder(i);
                img.getBandLinkedList().add(band);
            }
            if (img.getCreationMode().equals(Image.CREATION_MODE_MANUAL) || img.getCreationMode().equals(Image.CREATION_MODE_SEMI_AUTO)) {
                progressIndicator.setVisible(true);
                JFXDialogLayout content = new JFXDialogLayout();
                Task<Parent> loadManualBandEntry = new Task<Parent>() {
                    @Override
                    protected Parent call() throws Exception {
                        Parent parent = new LoaderProvider().manualBandEntryLoader(img, pageController);
                        content.setBody(parent);
                        return null;
                    }
                };
                loadManualBandEntry.setOnSucceeded(e -> {
                    Text text = new Text("Fill your magnetic band");
                    text.setStyle("-fx-font-family: Roboto");
                    content.setHeading(text);
                    JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
                    dialog.show();
                    progressIndicator.setVisible(false);
                });
                progressIndicator.progressProperty().bind(loadManualBandEntry.workDoneProperty());
                new Thread(loadManualBandEntry).start();
                //content.setBody(new LoaderProvider().manualBandEntryLoader(img,this));
            } else {
                progressIndicator.setVisible(true);
                Task<Parent> loadParentForAutoMode = new Task<Parent>() {
                    @Override
                    protected Parent call() throws Exception {
                        int[] temp = new int[img.getSizeOfBand()];
                        for (int i = 0; i < img.getSizeOfBand(); i++) {
                            int a = (int) (Math.random() * 256);
                            a = ((a<0)?(int) (a*-1):a);
                            temp[i] = a;
                        }
                        img.setMagneticBand(temp);
                        Parent parent = new LoaderProvider().HomePageLoader(img, pageController);
                        return parent;
                    }
                };
                progressIndicator.progressProperty().bind(loadParentForAutoMode.workDoneProperty());
                loadParentForAutoMode.setOnSucceeded(e -> {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(loadParentForAutoMode.getValue()));
                });
                Thread thread = new Thread(loadParentForAutoMode);
                thread.start();
            }
        }
    }

    @FXML
    void workOnConcreteImageOnAction(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        if(ConcreteImage.Controller.ToolsProvider.concretImageParent == null)
            ConcreteImage.Controller.ToolsProvider.concretImageParent = new ConcreteImageLoaderProvider().homePageProvider(stage);
        if(ConcreteImage.Controller.ToolsProvider.concretImageScene == null)
            ConcreteImage.Controller.ToolsProvider.concretImageScene = new Scene(ConcreteImage.Controller.ToolsProvider.concretImageParent);
        stage.setScene(ConcreteImage.Controller.ToolsProvider.concretImageScene);
        stage.show();
    }

    public LinkedList<String> initFormat(){
        LinkedList<String> store = new LinkedList<>();
        store.add("8-bits");
        store.add("16-bits in Intensities");
        store.add("16-bits in Amplitude");
        return store;
    }

    public LinkedList<String>initCreationType(){
        LinkedList<String> store = new LinkedList<>();
        store.add("Automatically");
        store.add("Semi-automatically");
        store.add("Manually");
        return store;
    }

    public boolean isEntriesAreValid(){
        if (NC.getText().equals("")||NL.getText().equals("")||nbrOfBand.getText().equals("")||formatComboBox.getSelectionModel().getSelectedItem()==null||
                creationComboBox.getSelectionModel().getSelectedItem()==null)
            return false;
        else if(Integer.parseInt(NC.getText())<=0||Integer.parseInt(NL.getText())<=0||Integer.parseInt(nbrOfBand.getText())<=0){
            return false;
        }
        else
            return true;
    }

}
