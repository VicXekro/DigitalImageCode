package ConcreteImage.SpeechRecognition;

import ConcreteImage.Controller.HomePageController;
import ConcreteImage.Controller.ToolsProvider;
import ConcreteImage.Model.Image;
import Model.Filter;
import Model.LinearMask;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Created by Xekro on 6/12/2017.
 */
public class SpeechRecognitionController {

    private final String accousticPath = "resource:/ConcreteImage/SpeechRecognition/en_us";
    private final String dictionaryPath = "resource:/ConcreteImage/SpeechRecognition/cmudict-en-us.dict";
    private final String grammarPath = "resource:/ConcreteImage/SpeechRecognition/";
    private boolean isExit;
    private HomePageController homePageController ;
    private ActionEvent event;
    private LiveSpeechRecognizer recognizer;

    public SpeechRecognitionController(HomePageController homePageController) {
        this.homePageController = homePageController;
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(accousticPath);
        configuration.setDictionaryPath(dictionaryPath);
        configuration.setGrammarPath(grammarPath);
        configuration.setUseGrammar(true);
        configuration.setGrammarName("ConcreteImage");
        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SpeechRecognitionController(){}

    public void microphone() {
            //recognizer.startRecognition(true);
        //startRecongnitionController();
            while (true) {
                System.out.println("Start");
                String utterance = recognizer.getResult().getHypothesis();
                System.out.println(utterance);
                if (utterance.contains("convert to greyscale") || utterance.contains("convert to black and white")) {
                    System.out.print("Hello");
                    homePageController.convertToGrayScaleOnAction(event);
                    recognizer.stopRecognition();
                    break;
                }
                else if(utterance.contains("convert to negative")){
                    homePageController.negativeMenuOnAction(event);
                    recognizer.stopRecognition();
                    break;
                }
                else if(utterance.contains("undo")){
                    homePageController.undoMenuOnAction(event);
                    recognizer.stopRecognition();
                    break;
                }
                else if(utterance.contains("tization")){
                    homePageController.skeletisationMenuOnAction(event);
                    recognizer.stopRecognition();
                    break;
                }
                else if(utterance.contains("close")){
                    homePageController.closeMenuOnAction(event);
                    recognizer.stopRecognition();
                    isExit = true;
                    break;
                }
                else if (utterance.startsWith("stop")) {
                    isExit = true;
                    recognizer.stopRecognition();
                    break;
                }
                else if(utterance.contains("filter")){
                    applyLinearFilter(utterance);
                    recognizer.stopRecognition();
                    break;
                }
                System.out.println(utterance);
            }
    }

    public void setEvent(ActionEvent event) {
        this.event = event;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public void startRecongnitionController(){
        recognizer.startRecognition(true);
    }

    private void applyLinearFilter(String utterance){
        System.out.println(utterance);
        homePageController.setUndoImage(ToolsProvider.createCopyOfOriginalImage(homePageController.getImage()));
        LinearMask linearMask = null;
        if(utterance.contains("average")) {
            linearMask = Filter.AVERAGE_MASK;
            linearMask.setBorderFeeling(homePageController.getBorderfillingMethod());
        }
        else if(utterance.contains("horizontal")){
            if(utterance.contains("sobel")) {
                linearMask = Filter.SOBEL_HORIZONTAL;
                linearMask.setBorderFeeling(homePageController.getBorderfillingMethod());
            }
            else if(utterance.contains("prewitt")){
                linearMask = Filter.PREWITT_HORIZONTAL;
                linearMask.setBorderFeeling(homePageController.getBorderfillingMethod());
            }
            else if(utterance.contains("robert")){
                linearMask = Filter.ROBERT_HORIZONTAL;
                linearMask.setBorderFeeling(homePageController.getBorderfillingMethod());
            }
        }
        ToolsProvider.reloadImage(ToolsProvider.linearConvolution(homePageController.getImage(), linearMask),
                homePageController.getStackPaneDisplayer(),homePageController);
    }

}
