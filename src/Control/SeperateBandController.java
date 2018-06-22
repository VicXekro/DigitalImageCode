package Control;

import Model.Band;
import Model.Image;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 3/23/2017.
 */
public class SeperateBandController implements Initializable {

    private Image originalImage;
    private HomePageController homePageController;
    @FXML
    private TableColumn<Band, Object> bandColumn;
    @FXML
    private TableView<Band> bandTable;
    @FXML
    private TableColumn<Band, Integer> orderColumn;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton deleteBandButton;
    @FXML
    private JFXButton restoreImageBandButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteBandButton.setVisible(false);
        restoreImageBandButton.setVisible(false);
        bandColumn.setCellValueFactory(new PropertyValueFactory<Band, Object>("bandLine"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<Band, Integer>("bandOrder"));
        LinkedList<Band> store = fillTable();
        bandTable.getItems().setAll(store);
    }

    @FXML
    void deleteBandOnAction(ActionEvent event) {
        if(bandTable.getSelectionModel().getSelectedItem()==null)
            ToolsProvider.displayErrorDialog("Please select a band before continuing",stackPane);
        else if(bandTable.getItems().size()==1)
            ToolsProvider.displayErrorDialog("Image contains only one band, it cannot be deleted.",stackPane);
        else{
            ObservableList<Band> bandSelected, bandAll;
            bandAll = bandTable.getItems();
            bandSelected = bandTable.getSelectionModel().getSelectedItems();
            bandSelected.forEach(bandAll::remove);
            deleteBand(bandTable.getSelectionModel().getSelectedIndex());
        }
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public HomePageController getHomePageController() {
        return homePageController;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    private LinkedList<Band> fillTable(){
        LinkedList<Band> store = originalImage.getBandLinkedList();
        int [][] tempMagneticBand = new int[originalImage.getNbrOfBands()][(originalImage.getSizeOfBand()/originalImage.getNbrOfBands())];
        int b=0, k =0, a = 0;
        while (a<(originalImage.getSizeOfBand()/originalImage.getNbrOfBands())) {
            for (int j = 0; j < originalImage.getNbrOfBands(); j++) {
                for (int i = a; i < a + (originalImage.getSizeOfBand()/(originalImage.getNbrOfBands()*originalImage.getNL())); i++) {
                    if (k != originalImage.getSizeOfBand()) {
                        tempMagneticBand[j][i] = originalImage.getMagneticBand()[k];
                        k = k + 1;
                    }
                }
            }
            a += originalImage.getSizeOfBand()/(originalImage.getNbrOfBands()*originalImage.getNL()); /*b+=1;*/
        }
        for(int i=0; i<originalImage.getNbrOfBands();i++){
            store.get(i).setMagneticBand(tempMagneticBand[i]);
            store.get(i).getScrollPaneBandLine();
            store.get(i).transformBandToTable();
        }
        return store;
    }

    private void deleteBand(int selectedIndex){
        LinkedList<int[][]> bandTable = new LinkedList<>();
        originalImage.setNbrOfBands(originalImage.getNbrOfBands()-1);
        originalImage.getBandLinkedList().remove(selectedIndex);
        System.out.println(originalImage.getBandLinkedList().size());
        for (int i = 0; i < originalImage.getBandLinkedList().size(); i++) {
            bandTable.add(originalImage.getBandLinkedList().get(i).getBandTable());
        }
        originalImage = ToolsProvider.reconstituteImage(bandTable,originalImage);
        homePageController.fillControlInLeftPanel();
        homePageController.createMagneticBand();
    }
}
