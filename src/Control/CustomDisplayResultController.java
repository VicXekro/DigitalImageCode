package Control;

import Model.Image;
import Model.Threshold;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/24/2017.
 */
public class CustomDisplayResultController implements Initializable{

    private Image image;
    private LinkedList<Threshold> thresholds;
    private AnchorPane[][]anchorPanes;
    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        overView();
        for (int i = 0; i <image.getNL() ; i++) {
            for (int j = 0; j <image.getNC() ; j++) {
                gridPane.add(anchorPanes[i][j],j,i);
            }
        }
    }

    private void overView(){
        anchorPanes = new AnchorPane[image.getNL()][image.getNC()];
        int d = 0;
        for (int i = 0; i <image.getNL() ; i++) {
            for (int j = 0; j <image.getNC() ; j++) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefWidth(10); anchorPane.setPrefHeight(10);
                for (int k = 0; k <thresholds.size() ; k++) {
                    if (image.getMagneticBand()[d]> thresholds.get(k).getThreshold1()&&image.getMagneticBand()[d]< thresholds.get(k).getThreshold2())
                        anchorPane.setStyle("-fx-background-color: rgb("+(int)(thresholds.get(k).getColor().getRed()*255)+","
                                +(int)(thresholds.get(k).getColor().getGreen()*255)+"," +(int)(thresholds.get(k).getColor().getBlue()*255)+")");
                    /*else
                        anchorPane.setStyle("-fx-background-color: black");*/
                }
                anchorPanes[i][j] = anchorPane;
                d++;
            }
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setThresholds(LinkedList<Threshold> thresholds) {
        this.thresholds = thresholds;
    }
}
