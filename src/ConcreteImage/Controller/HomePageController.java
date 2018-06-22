package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import ConcreteImage.SpeechRecognition.SpeechRecognitionController;
import ConcreteImage.View.ConcreteImageLoaderProvider;
import Model.LinearMask;
import Model.Mask;
import Model.Morphology;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Xekro on 6/2/2017.
 */
public class HomePageController implements Initializable {
    private Stage ownerStage;
    private File file;
    private ConcreteImage.Model.Image image;
    private Image undoImage;
    private FileChooser fileChooser = new FileChooser();
    private SimpleStringProperty borderfillingMethod;
    private LinkedList<Stage> stages = new LinkedList<>();
    SpeechRecognitionController speechController = new SpeechRecognitionController(this);
    private JFXSnackbar snackbar;

    @FXML
    private Label borderFillingLabelNotification;

    @FXML
    private StackPane stackPaneDisplayer;
    @FXML
    private JFXButton speechButton;
    @FXML
    private Label progressHbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressHbox.setVisible(false);
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("JPG","*.jpg"));
        borderfillingMethod = new SimpleStringProperty(LinearMask.SYMMETRIC);
        //snackbar = new JFXSnackbar(pane);
    }

    @FXML
    void aboutMenuOnAction(ActionEvent event) {
        ToolsProvider.displayAboutDialog(stackPaneDisplayer);
    }

    @FXML
    public void undoMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            if (undoImage != null) {
                image = ToolsProvider.createCopyOfOriginalImage(undoImage);
                ToolsProvider.reloadImage(image, stackPaneDisplayer,this);
                undoImage = null;
            }
        }
    }

    @FXML
    void backButtonOnAction(ActionEvent event) {
        for (Stage s: stages) {
            s.close();
        }
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new LoaderProvider().startPageLoader()));
        stage.show();
    }

    @FXML //method handling the speechRecognition
    void speechButtonOnAction(ActionEvent event) throws FileNotFoundException {
        if(ToolsProvider.isImageLoaded(image)) {
            FileInputStream fis = new FileInputStream("src/Resources/Icon/ic_mic_black_24dp_red.png");
            speechButton.setGraphic(new ImageView(new javafx.scene.image.Image(fis)));
            speechController.setEvent(event);
            speechController.setExit(false);
            startSpeechRecognizer();
        }
    }

    private void startSpeechRecognizer(){
       Task<Void> task = new Task<Void>() {
           @Override
           protected Void call() throws Exception {
               Platform.runLater(new Runnable() {
                   @Override
                   public void run() {
                       speechController.startRecongnitionController();
                       speechController.microphone();
                   }
               });
               return null;
           }
       };
       Thread thread = new Thread(task);
       thread.start();
       task.setOnSucceeded(event -> {
           if (!speechController.isExit())
               startSpeechRecognizer();
           else {
               FileInputStream fis = null;
               try {
                   fis = new FileInputStream("src/Resources/Icon/ic_mic_black_24dp.png");
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
               speechButton.setGraphic(new ImageView(new javafx.scene.image.Image(fis)));
           }
       });
    }

    @FXML//method implemented when the user click on the menu open
    void openOnAction(ActionEvent event) throws FileNotFoundException {
        try {
            if (image == null) {
                fileChooser.setTitle("Choose an Image");
                file = fileChooser.showOpenDialog(ownerStage);
                image = new Image(file);
                ToolsProvider.reloadImage(image, stackPaneDisplayer, this);
            } else {
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save your work before continuing?", yes, no);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == yes) {
                    saveOnAction(event);
                    fileChooser.setTitle("Choose an Image");
                    file = fileChooser.showOpenDialog(ownerStage);
                    image = new Image(file);
                    ToolsProvider.reloadImage(image, stackPaneDisplayer, this);
                } else {
                    fileChooser.setTitle("Choose an Image");
                    file = fileChooser.showOpenDialog(ownerStage);
                    image = new Image(file);
                    ToolsProvider.reloadImage(image, stackPaneDisplayer, this);
                }
            }
        }catch (IllegalArgumentException e){
            image = null;
            Alert alert = new Alert(Alert.AlertType.ERROR, "Image non conform; Please reload a new one");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK)
                openOnAction(event);
        }
    }

    @FXML
    void saveOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            fileChooser.setTitle("Save your work");
            file = fileChooser.showSaveDialog(ownerStage);
            try {
                ImageIO.write(image.getBufferedImage(), "jpg", file);
                //snackbar.show("Saved as "+file.getName(),5000);
            } catch (IOException e) {
                e.printStackTrace();
            }catch (IllegalArgumentException e){}
        }
    }

    @FXML
    void zeroMethoMenuItemOnAction(ActionEvent event) {
        borderfillingMethod.setValue(LinearMask.ZERO);
        borderFillingLabelNotification.setText(borderfillingMethod.getValue());
    }

    @FXML
    void symmetricMethoMenuItemOnAction(ActionEvent event) {
        borderfillingMethod.setValue(LinearMask.SYMMETRIC);
        borderFillingLabelNotification.setText(borderfillingMethod.getValue());
    }

    @FXML
    void circularMethoMenuItemOnAction(ActionEvent event) {
        borderfillingMethod.setValue(LinearMask.CIRCULAR);
        borderFillingLabelNotification.setText(borderfillingMethod.getValue());
    }

    @FXML
    void infoAboutImage(ActionEvent event) {
      if(ToolsProvider.isImageLoaded(image)){
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().infoAboutImageLoader(image)));
            stage.setTitle("Image info");
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    /**Added by me method*/
    @FXML
    public void convertToGrayScaleOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Image image = ToolsProvider.convertToGrayScale(this.image);
            ToolsProvider.reloadImage(image, stackPaneDisplayer,this);
        }
    }

    @FXML
    public void negativeMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Image image = ToolsProvider.transformToNegative(this.image);
            ToolsProvider.reloadImage(image, stackPaneDisplayer,this);
        }
    }

    /**Morphologycal Method*/
    @FXML
    void binarizationMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().binarizationLoader(image, stackPaneDisplayer, this)));
            stage.setTitle("Binarization");
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void erosionMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().complexMorphologycalOperationLoader(image, stackPaneDisplayer, Morphology.EROSION, this)));
            stage.setTitle(Morphology.EROSION);
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void dilationMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().complexMorphologycalOperationLoader(image, stackPaneDisplayer, Morphology.DILATION, this)));
            stage.setTitle(Morphology.DILATION);
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void openingMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().complexMorphologycalOperationLoader(image, stackPaneDisplayer, Morphology.OPENING, this)));
            stage.setTitle(Morphology.OPENING);
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void closureMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().complexMorphologycalOperationLoader(image, stackPaneDisplayer, Morphology.CLOSURE, this)));
            stage.setTitle(Morphology.CLOSURE);
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void whiteTopHatMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().complexMorphologycalOperationLoader(image, stackPaneDisplayer, Morphology.WHITETOPHAT, this)));
            stage.setTitle(Morphology.WHITETOPHAT);
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void blackTopHatMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().complexMorphologycalOperationLoader(image, stackPaneDisplayer, Morphology.BLACKTOPHAT, this)));
            stage.setTitle(Morphology.BLACKTOPHAT);
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    public void skeletisationMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Mask mask = new Mask();
            mask.setBorderFeeling(borderfillingMethod.getValue());
            mask.setCenterX(3 / 2);
            mask.setCenterY(3 / 2);
            mask.setNLmask(3);
            mask.setNCmask(3);
            javafx.scene.image.Image skeletisedImage = MorphologyOperation.imageSkeletisation(mask, image);
            ImageView imageView = new ImageView(skeletisedImage);
            ToolsProvider.centerImage(imageView, image,this);
            stackPaneDisplayer.getChildren().clear();
            stackPaneDisplayer.getChildren().add(imageView);
        }
    }

    /**LinearFilter Method*/
    @FXML
    void adaptativeMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().adaptiveFilterLoader(image, stackPaneDisplayer, this)));
            stage.setTitle("Adaptive Filter");
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void linearFilterMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().linearFilterLoader(image, stackPaneDisplayer, this)));
            stage.setTitle("Linear Filter");
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    /**Texture analysis*/
    @FXML
    void firstOrderMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().firstOrderTextureAnalysisLoader(image, stackPaneDisplayer, this)));
            stage.setTitle("First Order Texture Analysis");
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    void secondOrderMenuOnAction(ActionEvent event) {
        if(ToolsProvider.isImageLoaded(image)) {
            undoImage = ToolsProvider.createCopyOfOriginalImage(image);
            Stage stage = new Stage();
            stage.setScene(new Scene(new ConcreteImageLoaderProvider().secondOrderTextureAnalysisLoader(image, stackPaneDisplayer, this)));
            stage.setTitle("Second Order Texture Analysis");
            stage.setResizable(false);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stage.getIcons().add(new javafx.scene.image.Image(fis));
            stages.add(stage);
            stage.show();
        }
    }

    @FXML
    public void closeMenuOnAction(ActionEvent event) {
        ownerStage.close();
    }


    public Stage getOwnerStage() {
        return ownerStage;
    }

    public void setOwnerStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    public Image getImage() {
        return image;
    }

    public String getBorderfillingMethod() {
        return borderfillingMethod.get();
    }

    public StackPane getStackPaneDisplayer() {
        return stackPaneDisplayer;
    }

    public void setUndoImage(Image undoImage) {
        this.undoImage = undoImage;
    }

    public Label getProgressHbox() {
        return progressHbox;
    }

}
