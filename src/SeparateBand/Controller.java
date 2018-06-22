package SeparateBand;


import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 3/3/2017.
 */
public class Controller implements Initializable {

    private boolean isSizeOfBand = true, isNC = true, isNL = true, isBands = true;
    private int numSizeOfBand, numNC, numNL, numBands;

    private SImage sImage;

    public void setsImage(SImage sImage) {
        this.sImage = sImage;
    }

    @FXML
    private ComboBox<String> creationType;
    @FXML
    private TextField sizeOfBand;
    @FXML
    private Label NLErro;
    @FXML
    private Button generateButton;
    @FXML
    private TextField NC;
    @FXML
    private ComboBox<String> format;
    @FXML
    private Label sizeOfBandError;
    @FXML
    private Label bandError;
    @FXML
    private TextField bands;
    @FXML
    private TextField NL;
    @FXML
    private Label NCError;
    @FXML
    private ScrollPane result;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        format.getItems().setAll(initFormat());
        creationType.getItems().setAll(initCreationType());
//        NL.setText(Integer.toString(sImage.getNL()));
    }

    @FXML
    void siezeOfBandsKeyReleased(KeyEvent event) {
        String s = sizeOfBand.getText();
        try{
            numSizeOfBand = Integer.parseInt(s);
            isSizeOfBand = true;
            sizeOfBandError.setText("");
            if(isSizeOfBand && isNC && isNL && isBands)
                generateButton.setDisable(false);
        }
        catch (NumberFormatException e){
            sizeOfBandError.setText("Only whole numbers accepted");
            isSizeOfBand = false;
            generateButton.setDisable(true);
        }
    }

    @FXML
    void NCKeyReleased(KeyEvent event) {
        String s = NC.getText();
        try{
            numNC = Integer.parseInt(s);
            isNC = true;
            NCError.setText("");
            if(isSizeOfBand && isNC && isNL && isBands)
                generateButton.setDisable(false);
        }
        catch (NumberFormatException e){
            NCError.setText("Only whole numbers accepted");
            isNC = false;
            generateButton.setDisable(true);
        }
    }

    @FXML
    void NLKeyReleased(KeyEvent event) {
        String s = NL.getText();
        try{
            numNL = Integer.parseInt(s);
            isNL = true;
            NLErro.setText("");
            if(isSizeOfBand && isNC && isNL && isBands)
                generateButton.setDisable(false);
        }
        catch (NumberFormatException e){
            NLErro.setText("Only whole numbers accepted");
            isNL = false;
            generateButton.setDisable(true);
        }
    }

    @FXML
    void bandsKeyReleased(KeyEvent event) {
        String s = bands.getText();
        try{
            numBands = Integer.parseInt(s);
            isBands = true;
            bandError.setText("");
            if(isSizeOfBand && isNC && isNL && isBands)
                generateButton.setDisable(false);
        }
        catch (NumberFormatException e){
            bandError.setText("Only whole numbers accepted");
            isBands = false;
            generateButton.setDisable(true);
        }
    }

    public LinkedList<String>initFormat(){
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

    @FXML
    void generateButtonOnAction(ActionEvent event) throws IOException {
        if(isValueConsistant() == true)
            showDialog();
        else{
            separateBands(createAutoMagneticBand(Byte.parseByte(sizeOfBand.getText())));
        }
    }

    public LinkedList<Byte> fillMagneticBandAuto(byte lengthMagneticBand){
        LinkedList<Byte> store = new LinkedList<>();
        for(byte i=0; i<lengthMagneticBand; i++){
            store.add((byte)(Math.random()*256));
        }
        return store;
    }

    public boolean isValueConsistant(){
        SImage img = new SImage(Byte.parseByte(sizeOfBand.getText()),Integer.parseInt(NC.getText()),
                                Integer.parseInt(NL.getText()),format.getSelectionModel().getSelectedItem(),
                                Integer.parseInt(bands.getText()),creationType.getSelectionModel().getSelectedItem());
        if(img.getFormat().equals("8-bits")){
            int i = img.getNC()*img.getNL();
            int j = (int)(img.getSizeOfBans()/img.getNumberOfBans());
            if(i!=j)
                return true;
        }
        return false;
    }

    public void showDialog(){
        JFXDialogLayout content = new JFXDialogLayout();
        Text text = new Text("Error");
        text.setStyle("-fx-text-fill: red");
        content.setHeading(text);// set the heading
        content.setBody(new Text("There is an inconstancy between the NC*NL and the number of pixel computed."));
        JFXDialog dialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
        dialog.show();
    }

    public LinkedList<Long> createAutoMagneticBand(byte lengthMagneticBand){
        HBox hBox = new HBox();
        LinkedList<Long> store = new LinkedList<>();
        Random random = new Random();
        for(byte i=0; i<lengthMagneticBand; i++){
            long j = (long)(random.nextInt(255)+0);
            hBox.getChildren().add(new Label(Long.toString(j)+" | "));
            store.add(j);
        }
        scrollPane.setContent(hBox);
        return store;
    }

   /* public void separateBands(LinkedList<Long> magneticBands){
        VBox vBoxParent = new VBox();
        int i = 0;
        for(int j=0; j<Integer.parseInt(bands.getText());j++){
            HBox box = new HBox();
            box.getChildren().add(new Label("Band "+ j));
            while(i<magneticBands.size()){
                for(int k =0 ; k<Integer.parseInt(NC.getText());k++){
                    box.getChildren().add(new Label(Long.toString(magneticBands.get(k+i))));
                }
                i = i+Integer.parseInt(NC.getText())+1;
            }

            vBoxParent.getChildren().add(box);
        }
        result.setContent(vBoxParent);
    }*/

    public void separateBands(LinkedList<Long> magneticBands){
        VBox vBoxParent = new VBox();
        LinkedList<HBox> list = new LinkedList<>();
        for(int i = 0; i<Integer.parseInt(bands.getText());i++){
            list.add(new HBox(new Label("Band "+i+" :")));
        }
        int b=0, k =0, a = 0;
        while (a<Integer.parseInt(bands.getText())*Integer.parseInt(NC.getText())){
            for(int j=0; j<Integer.parseInt(bands.getText()); j++){
                for(int i = a; i<a+Integer.parseInt(NC.getText());i++){
                    if(k!=magneticBands.size()){
                    list.get(j).getChildren().add(new Label(Long.toString(magneticBands.get(k)) + " | "));
                    k = k+1;}
                }
            }
            a += Integer.parseInt(NC.getText()); /*b+=1;*/
        }
        for(int m = 0; m<list.size();m++){
            vBoxParent.getChildren().add(list.get(m));
        }
        result.setContent(vBoxParent);
    }
}
