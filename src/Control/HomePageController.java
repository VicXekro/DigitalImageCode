package Control;

import Model.Band;
import Model.Image;
import View.LoaderProvider;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 3/20/2017.
 */
public class HomePageController implements Initializable{

    private Image image;
    @FXML
    private JFXTextField nbrOfBandsTextField;
    @FXML
    private Label NLLableError;
    @FXML
    private Label NCLableError;
    @FXML
    private JFXTextField NCTextField;
    @FXML
    private Label nbrOfBandsLabelError;
    @FXML
    private ScrollPane magneticBandPane;
    @FXML
    private JFXTextField creationMode;
    @FXML
    private JFXTextField NLTextField;
    @FXML
    private JFXTextField format;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private AnchorPane centerAnchorPane;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private AnchorPane middleAnchorPane;
    @FXML
    private Label lableTitle;
    @FXML
    private StackPane stackPane;

    private StartPageController startPageController;

    private HamburgerBasicCloseTransition transition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lableTitle.setText("Identify Bands");
        lableTitle.setStyle("-fx-text-fill:  #3fa9f5");
        drawer.close();
        drawer.setVisible(false);
        drawer.setSidePane(new LoaderProvider().menuLoader(this));
        disableLeftControll(false);
        fillControlInLeftPanel();
        createMagneticBand();
        centerAnchorPane.getChildren().add(new LoaderProvider().sperateBandLoader(getImage(),this));

        /*Partition of the code handling the dropdown menu*/
        transition = new HamburgerBasicCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if(!(drawer.isVisible()))
                drawer.setVisible(true);
            else
                drawer.setVisible(false);
            if(drawer.isShown()) {
                drawer.close();
                middleAnchorPane.setVisible(false);
            }
            else {
                middleAnchorPane.setVisible(true);
                drawer.open();
            }
        });
    }

    @FXML
    void backOnAction(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new LoaderProvider().startPageLoader()));
        stage.show();
    }

    /**
     * Method to disable, Make non editable, Make non visible some controls
     * on the left panel of the screen
     * @param b, boolean
     */
    public void disableLeftControll(boolean b){
        NCTextField.setEditable(b);
        NLTextField.setEditable(b);
        nbrOfBandsTextField.setEditable(b);
        format.setEditable(b);
        creationMode.setEditable(b);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void fillControlInLeftPanel(){
        NCTextField.setText(Integer.toString(image.getNC()));
        NLTextField.setText(Integer.toString(image.getNL()));
        nbrOfBandsTextField.setText(Integer.toString(image.getNbrOfBands()));
        format.setText(image.getFormat());
        creationMode.setText(image.getCreationMode());
    }

    public void createMagneticBand(){
        HBox hBox = new HBox();
        for(int i=0; i<image.getSizeOfBand();i++){
            TextField textField = new TextField(Long.toString(image.getMagneticBand()[i]));
            textField.setEditable(false);
            textField.setStyle("-fx-font-family: Roboto");
            textField.setPrefWidth(40);
            hBox.getChildren().add(textField);
        }
        magneticBandPane.setContent(hBox);
    }

    public AnchorPane getCenterAnchorPane() {
        return centerAnchorPane;
    }

    public JFXDrawer getDrawer() {
        return drawer;
    }

    public AnchorPane getMiddleAnchorPane() {
        return middleAnchorPane;
    }

    public HamburgerBasicCloseTransition getTransition() {
        return transition;
    }

    public void setStartPageController(StartPageController startPageController) {
        this.startPageController = startPageController;
    }

    public Label getLableTitle() {
        return lableTitle;
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
