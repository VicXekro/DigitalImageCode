package ConcreteImage.View;

import ConcreteImage.Controller.*;
import ConcreteImage.Model.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Xekro on 6/2/2017.
 */
public class ConcreteImageLoaderProvider {

    public Parent homePageProvider(Stage stage){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePageView.fxml"));
        HomePageController controller = new HomePageController();
        controller.setOwnerStage(stage);
        loader.setController(controller);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public Parent linearFilterLoader (Image image, StackPane stackPane, HomePageController homePageController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LinearFilterView.fxml"));
        LinearFilterController controller = new LinearFilterController();
        controller.setDisplayerStackPane(stackPane);
        controller.setImageToFilter(image);
        controller.setHomePageController(homePageController);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent adaptiveFilterLoader(Image image, StackPane stackPane, HomePageController homePageController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdaptiveFilterView.fxml"));
        AdaptiveFilterController controller = new AdaptiveFilterController();
        controller.setImageToFilter(image);
        controller.setParentStackPane(stackPane);
        controller.setHomePageController(homePageController);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent binarizationLoader(Image image, StackPane stackPane, HomePageController homePageController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BinarizationView.fxml"));
        BinarizationController controller = new BinarizationController();
        controller.setImageToBinarize(image);
        controller.setParentStackPane(stackPane);
        controller.setHomePageController(homePageController);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent complexMorphologycalOperationLoader(Image image, StackPane stackPane, String operation, HomePageController homePageController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ComplexMorphologycalOperationView.fxml"));
        ComplexMorphologycalOperationController controller = new ComplexMorphologycalOperationController();
        controller.setImageToTransform(image);
        controller.setParenStackPane(stackPane);
        controller.setOperation(operation);
        controller.setHomePageController(homePageController);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent firstOrderTextureAnalysisLoader(Image image, StackPane stackPane, HomePageController homePageController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstOrderTextureAnalysisView.fxml"));
        FirstOrderTextureController controller = new FirstOrderTextureController();
        controller.setImage(image);
        controller.setStackPaneDisplayer(stackPane);
        controller.setHomePageController(homePageController);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent secondOrderTextureAnalysisLoader(Image image, StackPane stackPane, HomePageController homePageController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondOrderTextureAnalysisView.fxml"));
        SecondOrderTextureController controller = new SecondOrderTextureController();
        controller.setImageToTexture(image);
        controller.setStackPaneDisplayer(stackPane);
        controller.setHomePageController(homePageController);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }

    public Parent infoAboutImageLoader(Image image){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InformationImageView.fxml"));
        InfoAboutImageController controller = new InfoAboutImageController();
        controller.setImage(image);
        loader.setController(controller);
        Parent parent = null;
        try{
            parent = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return parent;
    }
}
