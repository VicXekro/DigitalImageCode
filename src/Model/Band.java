package Model;

import Model.Image;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by Xekro on 3/23/2017.
 */
public class Band extends Image {

    private SimpleObjectProperty<ScrollPane> bandLine;
    private SimpleIntegerProperty bandOrder;
    private int [][] bandTable;

    public Band(){
        bandOrder = new SimpleIntegerProperty();
        bandLine = new SimpleObjectProperty<>(null);
        super.setNbrOfBands(1);
    }

    public Band (int NC, int NL, String format){
        super.setNC(NC);
        super.setNL(NL);
        bandOrder = new SimpleIntegerProperty();
        bandLine = new SimpleObjectProperty<>(new ScrollPane());
        super.setNbrOfBands(1);
        super.setFormat(format);
        super.setSizeOfBand(NC, NL);
        initiaLizetMagneticBand(new int[getSizeOfBand()]);
        bandTable = new int[NL][NC];
    }

    public void getScrollPaneBandLine(){
        HBox hBox = new HBox();
        for(int i = 0; i<getMagneticBand().length;i++){
            TextField textField = new TextField(Long.toString(getMagneticBand()[i]));
            textField.setStyle("-fx-font-family: Roboto");
            textField.setPrefWidth(40);
            textField.setEditable(false);
            hBox.getChildren().add(textField);
        }
        bandLine.get().setContent(hBox);
    }

    public int getBandOrder() {
        return bandOrder.get();
    }

    public SimpleIntegerProperty bandOrderProperty() {
        return bandOrder;
    }

    public void setBandOrder(int bandOrder) {
        this.bandOrder.set(bandOrder);
    }

    public ScrollPane getBandLine() {
        return bandLine.get();
    }

    public SimpleObjectProperty<ScrollPane> bandLineProperty() {
        return bandLine;
    }

    public void setBandLine(ScrollPane bandLine) {
        this.bandLine.set(bandLine);
    }

    public void transformBandToTable(){
        int a =0;
        for(int i = 0; i< getNL(); i++){
            for(int j = 0; j< getNC(); j++){
                bandTable[i][j] = getMagneticBand()[a];
                a++;
            }
        }
    }

    public int[][] getBandTable() {
        return bandTable;
    }

    public void setBandTable(int[][] bandTable) {
        this.bandTable = bandTable;
    }
    
    public void transformTableToBand(){
        int [] temp = new int[getSizeOfBand()];
        int k = 0;
        for (int i = 0; i <bandTable.length ; i++) {
            for (int j = 0; j <bandTable[0].length ; j++) {
                temp[k] = bandTable[i][j];
                k++;
            }
        }
        setMagneticBand(temp);
    }
}
