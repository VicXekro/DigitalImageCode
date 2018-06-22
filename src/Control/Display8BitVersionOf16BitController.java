package Control;

import Model.Band;
import Model.Image;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 4/15/2017.
 */
public class Display8BitVersionOf16BitController implements Initializable{

    private Image image8bitVersion;

    @FXML
    private ScrollPane magneticBand;
    @FXML
    private TableColumn<Band, ScrollPane> bandColumn;
    @FXML
    private TableView<Band> bandTable;
    @FXML
    private TableColumn<Band, Integer> orderColumn;
    @FXML
    private JFXTextField NCTextfield;
    @FXML
    private JFXTextField sizeOfBandTextfield;
    @FXML
    private JFXTextField NLTextfield;
    @FXML
    private JFXTextField formatTextfield;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bandColumn.setCellValueFactory(new PropertyValueFactory<Band, ScrollPane>("bandLine"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<Band, Integer>("bandOrder"));
        fillTheMagneticBand();
        bandTable.getItems().setAll(image8bitVersion.getBandLinkedList());
        setAllTextfield();
    }

    public Image getImage8bitVersion() {
        return image8bitVersion;
    }

    public void setImage8bitVersion(Image image8bitVersion) {
        this.image8bitVersion = image8bitVersion;
    }

    public void fillTheMagneticBand(){
        HBox hBox = new HBox();
        for(int i=0; i<image8bitVersion.getSizeOfBand();i++){
            TextField textField = new TextField(Long.toString(image8bitVersion.getMagneticBand()[i]));
            textField.setEditable(false);
            textField.setStyle("-fx-font-family: Roboto");
            textField.setPrefWidth(40);
            hBox.getChildren().add(textField);
        }
        magneticBand.setContent(hBox);
    }

    public void setAllTextfield(){
        NCTextfield.setText(Integer.toString(image8bitVersion.getNC()));
        NLTextfield.setText(Integer.toString(image8bitVersion.getNL()));
        formatTextfield.setText(image8bitVersion.getFormat());
        sizeOfBandTextfield.setText(Integer.toString(image8bitVersion.getSizeOfBand()));
    }
}
