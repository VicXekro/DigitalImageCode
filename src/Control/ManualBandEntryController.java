package Control;

import Model.Image;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 3/27/2017.
 */
public class ManualBandEntryController implements Initializable{

    private Image dataImage; //image containing just the basic data.

    Image image = new Image(3,3,2,"8-bits",Image.CREATION_MODE_MANUAL);
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXButton fillButton;
    @FXML
    private Label errorLabel;
    private  StartPageController startPageController;
    private HBox hBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hBox = new HBox();
        fillButton.setVisible(false);
        if(dataImage.getCreationMode().equals(Image.CREATION_MODE_SEMI_AUTO))
            fillButton.setVisible(true);
        createMagneticBand();
    }

    @FXML
    void validateOnAction(ActionEvent event) {
        if(!checkDataValidity()){
            errorLabel.setText("One of your entry is either not a number or a negative number or greater than 255; please check again.");
        }
        else {
            errorLabel.setText("");
            int [] magneticBand = new int[dataImage.getSizeOfBand()];
            for(int i = 0; i<dataImage.getSizeOfBand(); i++){
                TextField t = (TextField)hBox.getChildren().get(i);
                magneticBand[i] = Integer.parseInt(t.getText());
            }
            dataImage.setMagneticBand(magneticBand);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(new LoaderProvider().HomePageLoader(dataImage,startPageController)));
            stage.show();
        }
    }

    @FXML
    void fillOnAction(ActionEvent event) {
        fillAutoRemainCase();
    }

    public Image getDataImage() {
        return dataImage;
    }

    public void setDataImage(Image dataImage) {
        this.dataImage = dataImage;
    }

    public void createMagneticBand(){
        for(int i=0; i<dataImage.getSizeOfBand(); i++){
            TextField textField = new TextField();
            textField.setPrefWidth(40);
            textField.setStyle("-fx-font-family: Roboto");
            hBox.getChildren().add(textField);
        }
        scrollPane.setContent(hBox);
    }

    //method to check that there is no negative value
    public boolean checkDataValidity(){
        for(Node n: hBox.getChildren()){
            TextField t = (TextField) n;
            if(t.getText().equals(""))
                t.setText("0");
            try {
                long i = Long.parseLong(t.getText());
                if (i < 0 || i>255)
                    return false;
            }catch(NumberFormatException nfe){
                return false;
            }
        }
        return true;
    }

    //method to fill automatically the remain case.
    public void fillAutoRemainCase(){
        for(Node n: hBox.getChildren()){
            TextField t = (TextField)n;
            if(t.getText().equals("")) {
                int a = (int) (Math.random() * 256);
                a = ((a<0)?(int)(a*-1):a);
                t.setText(Integer.toString(a));
            }
        }
    }

    public void setStartPageController(StartPageController startPageController) {
        this.startPageController = startPageController;
    }
}
