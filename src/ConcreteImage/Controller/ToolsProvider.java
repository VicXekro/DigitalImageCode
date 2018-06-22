package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import ConcreteImage.View.ConcreteImageLoaderProvider;
import Model.LinearMask;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 * Created by Xekro on 6/3/2017.
 */
public class ToolsProvider {

    public static Parent concretImageParent;
    public static Scene concretImageScene;

    //method to convert an image into grayscale (black and white)
    public static Image convertToGrayScale(Image image){
        for (int i = 0; i <image.getHeight() ; i++) {
            for (int j = 0; j <image.getWidth() ; j++) {
                Color c = new Color(image.getBufferedImage().getRGB(j,i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() *0.114);
                Color newColor = new Color(red+green+blue, red+green+blue,red+green+blue);
                image.getBufferedImage().setRGB(j,i,newColor.getRGB());
            }
        }
        return image;
    }

    //method to transform an image into its negative
    public static Image transformToNegative(Image image){
        for (int i = 0; i <image.getHeight() ; i++) {
            for (int j = 0; j <image.getWidth() ; j++) {
                Color c = new Color(image.getBufferedImage().getRGB(j,i));
                int red = 255 - c.getRed();
                int green = 255 - c.getGreen();
                int blue = 255 - c.getBlue();
                Color newColor = new Color(red, green,blue);
                image.getBufferedImage().setRGB(j,i,newColor.getRGB());
            }
        }
        return image;
    }

    //method to make sure that the image is always centered in the frame
    public static void centerImage(ImageView imageView, Image image, HomePageController homePageController){
        double boundaryWidth = 980, boundaryHeigth = 592;
        if (image.getWidth()>980) {
            imageView.setFitWidth(boundaryWidth);
            double new_height = (boundaryWidth*image.getHeight())/image.getWidth();
            imageView.setFitHeight(new_height);
        }
        if (image.getHeight()>boundaryHeigth) {
            imageView.setFitHeight(boundaryHeigth);
            double new_width = (boundaryHeigth*image.getWidth())/image.getHeight();
            imageView.setFitWidth(new_width);
        }
        homePageController.getProgressHbox().setVisible(false);
    }

    //method to extract the Zone of Interest of an image
    public static void extractZOI(int startX,int startY, int endX, int endY){

    }

    //method create a copy of the image with zero border filling
    //Cx, Cy center coordinate of the mask
    public static void zeroFillingMethod(LinkedList<int[][]> bandTablesBordered, Image image, int Cx, int Cy ){
        for (int i = 0; i < bandTablesBordered.size() ; i++) {
            for (int j = 0; j <bandTablesBordered.get(0).length ; j++) {
                for (int k = 0; k <bandTablesBordered.get(0)[0].length ; k++) {
                    if(j<Cy||j>(Cy+image.getHeight()-1))
                        bandTablesBordered.get(i)[j][k] = 0;
                    else if(k<Cx||k>(Cx+image.getWidth()-1))
                        bandTablesBordered.get(i)[j][k] = 0;
                }
            }
        }
    }

    //method create a copy of the image with symmetric border filling
    //Cx, Cy center coordinate of the mask
    public static void symmetricFillingMethod(LinkedList<int[][]> bandTablesBordered, Image image, int Cx, int Cy ){
        for (int i = 0; i < bandTablesBordered.size(); i++) {
            //part to symmetrically fill the lines
            for (int j = Cy-1; j >= 0 ; j--) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    bandTablesBordered.get(i)[j][k+Cx] = bandTablesBordered.get(i)[j+2][k+Cx];
                }
            }

            for (int j = Cy+image.getHeight(); j <= bandTablesBordered.get(0).length-1 ; j++) {
                for (int k = 0; k < image.getWidth() ; k++) {
                    bandTablesBordered.get(i)[j][k+Cx] = bandTablesBordered.get(i)[j-2][k+Cy];
                }
            }

            //part to symmetrically fill the columns
            for (int j = 0; j < bandTablesBordered.get(0).length ; j++) {
                for (int k = Cx-1; k >= 0 ; k--) {
                    bandTablesBordered.get(i)[j][k] = bandTablesBordered.get(i)[j][k+2];
                }
            }

            for (int j = 0; j < bandTablesBordered.get(0).length ; j++) {
                for (int k = Cx+image.getWidth(); k <= bandTablesBordered.get(0)[0].length-1 ; k++) {
                    bandTablesBordered.get(i)[j][k] = bandTablesBordered.get(i)[j][k-2];
                }
            }
        }
    }

    //method create a copy of the image with circular border filling
    //Cx, Cy center coordinate of the mask
    public static void circularFillingMethod(LinkedList<int[][]> bandTablesBordered, Image image, int Cx, int Cy ){
        for (int i = 0; i < bandTablesBordered.size() ; i++) {
            //part to circularly fill the lines
            for (int j = Cy-1; j >=0 ; j--) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    bandTablesBordered.get(i)[j][k+Cx] = bandTablesBordered.get(i)[j+image.getHeight()][k+Cx];
                }
            }

            for (int j = Cy+ image.getHeight(); j <= bandTablesBordered.get(0).length-1 ; j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    bandTablesBordered.get(i)[j][k+Cx] = bandTablesBordered.get(i)[j-image.getHeight()][k+Cx];
                }
            }

            //part to circularly fill the columns
            for (int j = 0; j < bandTablesBordered.get(0).length ; j++) {
                for (int k = Cx-1; k >=0 ; k--) {
                    bandTablesBordered.get(i)[j][k] = bandTablesBordered.get(i)[j][k+image.getWidth()];
                }
            }

            for (int j = 0; j < bandTablesBordered.get(0).length; j++) {
                for (int k = Cx+image.getWidth(); k <= bandTablesBordered.get(0)[0].length-1 ; k++) {
                    bandTablesBordered.get(i)[j][k] = bandTablesBordered.get(i)[j][k-image.getWidth()];
                }
            }
        }
    }

    //method to create the bordered image according to a linear mask
    public static LinkedList<int[][]>  createBorderOnImage (int maskNC, int maskNL, int Cx, int Cy, Image image) throws OutOfMemoryError{
        LinkedList<int[][]> bandTables = new LinkedList<>();
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            int[][] temp = new int[image.getHeight() + Cy + (maskNL - Cy - 1)][image.getWidth() + Cx + (maskNC - Cx - 1)];
            bandTables.add(temp);
        }

            for (int j = 0; j <image.getHeight(); j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    Color c = new Color(image.getBufferedImage().getRGB(k,j));
                    bandTables.get(0)[j+Cy][k+Cx] = c.getRed();
                    bandTables.get(1)[j+Cy][k+Cx] = c.getGreen();
                    bandTables.get(2)[j+Cy][k+Cx] = c.getBlue();
                }
            }

        return bandTables;
    }

    //method to convolut a pixel.
    public static int convolutionPixel(int x0, int y0, LinearMask linearMask, int[][] borderBandTable){
        int c = 0; //Convoluted pixel
        int a = ((linearMask.getNCmask()%2 == 0)?(x0+linearMask.getCenterX()-1):(x0+linearMask.getCenterX())),
                b = ((linearMask.getNLmask()%2 == 0)?(y0+linearMask.getCenterY()-1):(y0+linearMask.getCenterY()));
        for (int i = (y0-linearMask.getCenterY()); i <= b ; i++) {
            for (int j = (x0-linearMask.getCenterX()); j <= a ; j++) {
                c +=  borderBandTable[i][j] * linearMask.getMask()[(i-y0+linearMask.getCenterY())][(j-x0+linearMask.getCenterX())];
            }
        }
        return c;
    }

    public static Image linearConvolution(Image image, LinearMask linearMask)throws OutOfMemoryError{
        LinkedList<int [][]> bandTablesBordered = createBorderOnImage(linearMask.getNCmask(),linearMask.getNLmask(),linearMask.getCenterX(),
                linearMask.getCenterY(), image);
        LinkedList<int [][]> IMA = createReceivingBandTables(image);
        LinkedList<int [][]> IMG = new LinkedList<>();

        determineBorderFillingMethod(bandTablesBordered,linearMask.getBorderFeeling(),image,linearMask.getCenterX(),linearMask.getCenterY());

        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            for (int j = 0; j <image.getHeight() ; j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    IMA.get(i)[j][k] = convolutionPixel((k+linearMask.getCenterX()),(j+linearMask.getCenterY()),
                            linearMask,bandTablesBordered.get(i));
                }
            }
        }
        IMG = transformImagetoLinkedList(image);
        //IMG.add(image.getBandLinkedList().get(i).getBandTable());
        int[] maxValueIMA = findMaxGrayScale(IMA,image), oldMaxValueIMG = findMaxGrayScale(IMG,image);

        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            for (int j = 0; j <image.getHeight() ; j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    IMG.get(i)[j][k] = (oldMaxValueIMG[i]*IMA.get(i)[j][k])/maxValueIMA[i];
                }
            }
        }

        //to be recheck
        modifyColorFromArray(IMG,image);
        return image;
    }

    //method to create the bandTables receiving image data before its reconstitution
    public static LinkedList<int[][]> createReceivingBandTables(Image image) throws OutOfMemoryError {
        LinkedList<int[][]> IMA = new LinkedList<>();
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            try {
                int[][] temp = new int[image.getHeight()][image.getWidth()];
                IMA.add(temp);
            }catch (OutOfMemoryError e){
                throw e;
            }
        }
        return IMA;
    }

    //method to create the bandTables receiving image data before its reconstitution
    public static LinkedList<float[][]> createReceivingBandTablesFloat(Image image) throws OutOfMemoryError {
        LinkedList<float[][]> IMA = new LinkedList<>();
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            try {
                float[][] temp = new float[image.getHeight()][image.getWidth()];
                IMA.add(temp);
            }catch (OutOfMemoryError e){
                throw e;
            }
        }
        return IMA;
    }

    //method to find the greater greyscale in a band
    public static int [] findMaxGrayScale(LinkedList<int[][]>bandTables, Image img){
        int [] maxValues = new int[bandTables.size()];
        for (int i = 0; i <bandTables.size() ; i++) {
            maxValues[i] = bandTables.get(i)[0][0];
            for (int j = 0; j <img.getHeight() ; j++) {
                for (int k = 0; k <img.getWidth() ; k++) {
                    if(bandTables.get(i)[j][k]>maxValues[i])
                        maxValues[i] = bandTables.get(i)[j][k];
                }
            }
        }
        return  maxValues;
    }

    //method to find the greater greyscale in a band
    public static float [] findMaxGrayScaleFloat(LinkedList<float[][]>bandTables, Image img){
        float [] maxValues = new float[bandTables.size()];
        for (int i = 0; i <bandTables.size() ; i++) {
            maxValues[i] = bandTables.get(i)[0][0];
            for (int j = 0; j <img.getHeight() ; j++) {
                for (int k = 0; k <img.getWidth() ; k++) {
                    if(bandTables.get(i)[j][k]>maxValues[i])
                        maxValues[i] = bandTables.get(i)[j][k];
                }
            }
        }
        return  maxValues;
    }

    //method to determine the borderFilling method to use
    public static void determineBorderFillingMethod(LinkedList<int[][]> bandTablesBordered, String borderFilling, Image image, int Cx, int Cy){
        if (borderFilling.equals(LinearMask.CIRCULAR))
            circularFillingMethod(bandTablesBordered,image,Cx,Cy);
        else if(borderFilling.equals(LinearMask.SYMMETRIC))
            symmetricFillingMethod(bandTablesBordered,image,Cx,Cy);
        else if(borderFilling.equals(LinearMask.ZERO))
            zeroFillingMethod(bandTablesBordered,image,Cx,Cy);
    }

    //method to reassign color of the BufferImage from an array
    public static void modifyColorFromArray( LinkedList<int [][]> IMG, Image image){
        for (int i = 0; i <image.getHeight() ; i++) {
            for (int j = 0; j <image.getWidth() ; j++) {
                int red = IMG.get(0)[i][j], green = IMG.get(1)[i][j], blue = IMG.get(2)[i][j];
                red = (red<0)?(256+red):red;
                green = (green<0)?(256+green):green;
                blue = (blue<0)?(256+blue):blue;
                try{
                    Color color = new Color(red,green,blue);
                    image.getBufferedImage().setRGB(j,i,color.getRGB());
                }catch (Exception e){
                    System.out.println("Red: "+red+" Green: "+green+" Blue: "+blue);
                }
            }
        }
    }

    //method to extractImageWindows
    public static int[][] extractImageWindow(int x0, int y0, int maskNC,int maskNL,int Cx, int Cy, int[][] borderBandTable){
        int [][] windowedImage = new int[maskNL][maskNC];
        int a = ((maskNC%2 == 0)?(x0+Cx-1):(x0+Cx)),
                b = ((maskNL%2 == 0)?(y0+Cy-1):(y0+Cy));
        for (int i = (y0-Cy); i <= b ; i++) {
            for (int j = (x0-Cx); j <= a ; j++) {
                windowedImage[(i-y0+Cy)][(j-x0+Cx)] =  borderBandTable[i][j];
            }
        }
        return windowedImage;
    }

    //method to create the copy of a buffered Image
    public static Image createCopyOfOriginalImage(Image image){
        BufferedImage originalBuffer = image.getBufferedImage();
        ColorModel cm = originalBuffer.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = originalBuffer.copyData(originalBuffer.getRaster().createCompatibleWritableRaster());
        BufferedImage copy = new BufferedImage(cm,raster,isAlphaPremultiplied,null);
        Image copyImage = new Image(copy);
        image.setPath(image.getPath());
        return copyImage;
    }

    //method to reload image in stackPane
    public static void reloadImage(Image image, StackPane stackPane, HomePageController homePageController){
        javafx.scene.image.Image convertImage = SwingFXUtils.toFXImage(image.getBufferedImage(),null);
        ImageView imageView = new ImageView(convertImage);
        ToolsProvider.centerImage(imageView,image, homePageController);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(imageView);
    }

    public static void displayAlertMemory(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Oh oh Image too big, cannot continue the operation");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().setAll(new javafx.scene.image.Image(fis));
        stage.showAndWait();
    }

    public static LinkedList<int[][]> transformImagetoLinkedList(Image image){
        LinkedList<int[][]> IMG = new LinkedList<>();
        int [][] tempRed = new int[image.getHeight()][image.getWidth()];
        int [][] tempGreen = new int[image.getHeight()][image.getWidth()];
        int [][] tempBlue = new int[image.getHeight()][image.getWidth()];
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                Color color = new Color(image.getBufferedImage().getRGB(k,j));
                tempRed[j][k] = color.getRed();
                tempGreen[j][k] = color.getGreen();
                tempBlue[j][k] = color.getBlue();
            }
        }
        IMG.add(tempRed); IMG.add(tempGreen); IMG.add(tempBlue);
        return IMG;
    }

    public static boolean isImageLoaded(Image image){
        boolean b = true;
        if(image == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please Open an image first!");
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/Resources/Icon/MainIcon.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().setAll(new javafx.scene.image.Image(fis));
            stage.showAndWait();
            b = false;
        }
        return b;
    }

    public static void displayAboutDialog(StackPane stackPane){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setBody(new LoaderProvider().aboutLoader());
        content.setStyle("-fx-background-color: black");
        JFXDialog dialog = new JFXDialog(stackPane,content, JFXDialog.DialogTransition.CENTER);
        dialog.show();
    }
}
