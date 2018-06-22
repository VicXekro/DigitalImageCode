package Control;

import View.LoaderProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 3/22/2017.
 */
public class MenuController implements Initializable {

    private HomePageController homePageController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public HomePageController getHomePageController() {
        return homePageController;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    @FXML
    void seperateButtonOnAction(ActionEvent event) {
        changeTitlePane("Identify Bands"," #3fa9f5");
        homePageController.getMiddleAnchorPane().setVisible(false);
        homePageController.getDrawer().close();
        homePageController.getDrawer().setVisible(false);
        homePageController.getTransition().setRate(homePageController.getTransition().getRate()*-1);
        homePageController.getTransition().play();
        homePageController.getCenterAnchorPane().getChildren().clear();
        homePageController.getCenterAnchorPane().getChildren().add(new LoaderProvider().sperateBandLoader(homePageController.getImage(),homePageController));
    }

    @FXML
    void displayImageOnAction(ActionEvent event) {
        homePageController.getCenterAnchorPane().getChildren().clear();
        changeTitlePane("Display Image", "#00d410");
        homePageController.getCenterAnchorPane().getChildren().add(new LoaderProvider().displayImageLoader(homePageController.getImage(),homePageController));
        partOfLoader();
    }

    @FXML
    void filterImageOnAction(ActionEvent event) {
        homePageController.getCenterAnchorPane().getChildren().clear();
        changeTitlePane("Filter Image","#FF931E");
        homePageController.getCenterAnchorPane().getChildren().add(new LoaderProvider().filteringImageLoader(homePageController.getImage()));
        partOfLoader();
    }

    @FXML
    void mathematicalMorphologyOnAction(ActionEvent event) {
        homePageController.getCenterAnchorPane().getChildren().clear();
        changeTitlePane("Mathematical Morphology", "#C8AD7F");
        homePageController.getCenterAnchorPane().getChildren().add(new LoaderProvider().mathematicalMorphologyLoader(homePageController.getImage()));
        partOfLoader();
    }

    @FXML
    void textureAnalysisOnAction(ActionEvent event) {
        homePageController.getCenterAnchorPane().getChildren().clear();
        changeTitlePane("Texture Analysis","#C14C4C");
        homePageController.getCenterAnchorPane().getChildren().add(new LoaderProvider().textureAnalysisLoader(homePageController.getImage()));
        partOfLoader();
    }

    private void changeTitlePane(String title, String color){
        homePageController.getLableTitle().setText(title);
        homePageController.getLableTitle().setStyle("-fx-text-fill: "+color);
    }

    private void partOfLoader(){
        homePageController.getMiddleAnchorPane().setVisible(false);
        homePageController.getDrawer().close();
        homePageController.getDrawer().setVisible(false);
        homePageController.getTransition().setRate(homePageController.getTransition().getRate()*-1);
        homePageController.getTransition().play();
    }

    @FXML
    void aboutButtonOnAction(ActionEvent event) {
        ToolsProvider.displayAboutDialog(homePageController.getStackPane());
    }
}
