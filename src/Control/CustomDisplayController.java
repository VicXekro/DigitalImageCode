package Control;

import Model.Image;
import Model.Threshold;
import View.LoaderProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/24/2017.
 */
public class CustomDisplayController implements Initializable{

    private Image image;
    private AnchorPane[][] overViewCustomImage;
    @FXML
    private VBox vBoxValues;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTreshHold();
    }

    @FXML
    void validateButtonOnAction(ActionEvent event) {
        LinkedList<Threshold> thresholds = new LinkedList<>();
        for (int i = 0; i <vBoxValues.getChildren().size() ; i++) {
            HBox hBox = (HBox)vBoxValues.getChildren().get(i);
            TextField t1 = (TextField) hBox.getChildren().get(1);
            TextField t2 = (TextField) hBox.getChildren().get(3);
            ColorPicker colorPicker = (ColorPicker)hBox.getChildren().get(4);
            thresholds.add(new Threshold(colorPicker.getValue(),Integer.parseInt(t1.getText()),Integer.parseInt(t2.getText())));
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(new LoaderProvider().customDisplayResultLoader(image,thresholds)));
        stage.setTitle("Custom Display Result");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void newThresholdOnAction(ActionEvent event) {
        createTreshHold();
    }

    public void createTreshHold(){
        Label label1 = new Label("From:");
        Label label2 = new Label("To:");
        TextField textField1 = new TextField();
        textField1.setPrefWidth(40);
        TextField textField2 = new TextField();
        textField2.setPrefWidth(40);
        ColorPicker colorPicker = new ColorPicker();
        HBox hbox = new HBox(label1,textField1,label2,textField2,colorPicker);
        hbox.setSpacing(3);
        vBoxValues.getChildren().add(hbox);
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
