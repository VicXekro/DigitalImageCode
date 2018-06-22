package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import Control.*;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/16/2017.
 */
public class InfoAboutImageController implements Initializable {

    private Image image;

    @FXML
    private JFXTextField nbrOfBandTextfield;
    @FXML
    private JFXTextField NCTextfield;
    @FXML
    private JFXTextField NLTextfield;
    @FXML
    private JFXTextField pixelPerBand;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NCTextfield.setText(Integer.toString(image.getWidth()));
        NLTextfield.setText(Integer.toString(image.getHeight()));
        nbrOfBandTextfield.setText(Integer.toString(image.getNumberOfBands()));
        pixelPerBand.setText(Integer.toString(image.getHeight()*image.getHeight()));
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
