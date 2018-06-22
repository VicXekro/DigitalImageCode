package View;

import Control.*;
import Main.Preload;
import Model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Xekro on 3/20/2017.
 */
public class LoaderProvider {

    public Parent HomePageLoader(Image image, StartPageController startPageController){
        FXMLLoader loader = new FXMLLoader( getClass().getResource("HomePageView.fxml"));
        HomePageController homePageController = new HomePageController();
        homePageController.setImage(image);
        homePageController.setStartPageController(startPageController);
        loader.setController(homePageController);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  parent;

    }

    public Parent startPageLoader () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartPageView.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent menuLoader(HomePageController controller){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
        MenuController menuController = new MenuController();
        menuController.setHomePageController(controller);
        loader.setController(menuController);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public Parent sperateBandLoader(Image image, HomePageController controller){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SeperateBandView.fxml"));
        SeperateBandController seperateBandController = new SeperateBandController();
        seperateBandController.setOriginalImage(image);
        seperateBandController.setHomePageController(controller);
        loader.setController(seperateBandController);
        Parent parent = null;
        try {
            parent = loader.load();
            ProgressBar bar = new ProgressBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public Parent displayImageLoader(Image image, HomePageController controller){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayImageView.fxml"));
        DisplayImageController displayImageController = new DisplayImageController();
        displayImageController.setImageToDisplay(image);
        displayImageController.setHomePageController(controller);
        loader.setController(displayImageController);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public Parent manualBandEntryLoader(Image image, StartPageController startPageController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manualBandEntryView.fxml"));
        ManualBandEntryController controller = new ManualBandEntryController();
        controller.setDataImage(image);
        controller.setStartPageController(startPageController);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public Parent displayImageAnimationLoader(Image image, String displayMethod, boolean isZOISelected){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayImageAnimationView.fxml"));
        DisplayImageAnimationController controller = new DisplayImageAnimationController();
        controller.setImageToAnimate(image);
        controller.setDisplayMethod(displayMethod);
        controller.setZOISelected(isZOISelected);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent display8BitVersionOf16BitLoader(Image image){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Display8bitVersionOf16Bit.fxml"));
        Display8BitVersionOf16BitController controller = new Display8BitVersionOf16BitController();
        controller.setImage8bitVersion(image);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent imageLinearFilteringResultLoader(Image originalImage, LinearMask mask){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FilteredImageResultView.fxml"));
        FilteringImageResultController controller = new FilteringImageResultController();
        controller.setLinearMask(mask);
        controller.setOriginal8bitImage(originalImage);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public FXMLLoader preloaderLoader(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PreloaderView.fxml"));
        return loader;
    }

    public Parent aboutLoader(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutView.fxml"));
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent filteringImageLoader(Image image){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FilteringImageView.fxml"));
        FilteringImageController controller = new FilteringImageController();
        controller.setImageToFilter(image);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent imageAdaptiveFilteringResultLoader(Image originalImage, ImageWindow imageWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FilteredImageResultView.fxml"));
        FilteringImageResultController controller = new FilteringImageResultController();
        controller.setOriginal8bitImage(originalImage);
        controller.setImageWindow(imageWindow);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent mathematicalMorphologyLoader(Image image){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MathematicalMorphologyView.fxml"));
        MathematicalMorphologyController controller = new MathematicalMorphologyController();
        controller.setImageToTransform(image);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return  parent;
    }

    public Parent mathematicalMorphologyResultBinarizeLoader(Image image, String operation,int thres1, int thres2){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MathematicalMorphologyResultView.fxml"));
        MathematicalMorphologyResultController controller = new MathematicalMorphologyResultController();
        controller.setOriginalImage(image);
        controller.setOperation(operation);
        controller.setThreshold(thres1,thres2);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent mathematicalMorphologyResultComplexOperation(Image image, Mask mask){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MathematicalMorphologyResultView.fxml"));
        MathematicalMorphologyResultController controller = new MathematicalMorphologyResultController();
        controller.setOperation(mask.getMorphologicalOperation());
        controller.setOriginalImage(image);
        controller.setMask(mask);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent textureAnalysisLoader(Image image){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TextureAnalysisView.fxml"));
        TextureAnalysisController controller = new TextureAnalysisController();
        controller.setImageToTexture(image);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent textureAnalysisResultFirstOrderLoader(Image image, ImageWindow imageWindow, String firstOrderParameter){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TextureAnalysisResultView.fxml"));
        TextureAnalysisResultController controller = new TextureAnalysisResultController();
        controller.setOriginal8bitImage(image);
        controller.setImageWindow(imageWindow);
        controller.setFirstOrderParameter(firstOrderParameter);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent textureAnalysisResultSecondOrderLoader(Image image, ImageWindow imageWindow, String secondOrderParameter, String direction,
                                                         int InterPixelDistance){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TextureAnalysisResultView.fxml"));
        TextureAnalysisResultController controller = new TextureAnalysisResultController();
        controller.setOriginal8bitImage(image);
        controller.setImageWindow(imageWindow);
        controller.setSecondOrderParameter(secondOrderParameter);
        controller.setSecondOrderDirection(direction);
        controller.setSecondOrderPixelInterDistance(InterPixelDistance);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent customDisplayPreloader(Image image){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomDisplayView.fxml"));
        CustomDisplayController controller = new CustomDisplayController();
        controller.setImage(image);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent customDisplayResultLoader(Image image, LinkedList<Threshold> thresholds){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomDisplayResultView.fxml"));
        CustomDisplayResultController controller = new CustomDisplayResultController();
        controller.setImage(image);
        controller.setThresholds(thresholds);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }
}
