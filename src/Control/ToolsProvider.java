package Control;

import Model.Band;
import Model.Image;
import Model.LinearMask;
import Model.NegativeNumberException;
import View.LoaderProvider;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.util.LinkedList;

/**
 * Created by Xekro on 4/6/2017.
 */
    public class ToolsProvider {

    //method to convert Image
    public static Image convertImage(Image image){
        if(image.getFormat().equals(Image.FORMAT_16BITS_INTENSITIES))
            return ToolsProvider.convert16BitIntensitiesIn8bit(image);
        else if (image.getFormat().equals(Image.FORMAT_16BITS_AMPLITUDE))
            return ToolsProvider.convert16bitAmplitudeIn8bit(image);
        else
            return image;
    }

    //method to compute the factorial of a number
    public static int factorial(int number){
        if(number == 0 || number == 1)
            return 1;
        else
            return number * factorial(number-1);
    }

    //method to calculate the AVG for the conversion
    public static double[][] AVG (int[][] IMA, int NC, int NL){
        double[][] avg = new double[IMA.length][1], sum = new double[IMA.length][1];
        for (int i = 0; i <IMA.length ; i++) {
            for (int j = 0; j <IMA[0].length ; j++) {
                sum[i][0] += IMA[i][j];
            }
        }
        for (int i = 0; i <IMA.length ; i++) {
            avg[i][0] = sum[i][0]/(NC*NL);
        }
        return avg;
    }

    //method to calculate the AvgSq for the conversion
    public static double[][] AvgSq (int[][] IMA, int NC, int NL){
        double[][] avgsq = new double[IMA.length][1], sum = new double[IMA.length][1];
        for (int i = 0; i < IMA.length; i++) {
            for (int j = 0; j <IMA[0].length ; j++) {
                sum[i][0] += Math.pow(IMA[i][j],2);
            }
        }
        for (int i = 0; i <IMA.length ; i++) {
            avgsq[i][0] = sum[i][0]/(NC*NL);
        }
        return avgsq;
    }

    //method to calculate the SD for the conversion
    public static double[][] SD (double[][] Avg, double[][] Avgsq){
        double[][] sd = new double[Avg.length][1];
        for (int i = 0; i <Avg.length ; i++) {
            double sdSquare = Avgsq[i][0] - Math.pow(Avg[i][0],2);
            sd[i][0] = Math.sqrt(sdSquare);
        }
        return sd;
    }

    //method to convert a 16Bit intensities image into an 8-bits Image.
    public static Image convert16BitIntensitiesIn8bit(Image image){
        int[][] IMA = new int[image.getNbrOfBands()][image.getSizeOfBand()/2];
        Image IMA8bit = new Image(image.getNC(), image.getNL(), image.getNbrOfBands(), Image.FORMAT_8BITS, image.getCreationMode());
        for(int i = 0; i<image.getNbrOfBands();i++){
            for (int j = 0; j < IMA8bit.getSizeOfBand()/(IMA8bit.getNbrOfBands()); j++) {
                if((2*j+1)<image.getSizeOfBand()/image.getNbrOfBands()){
                    IMA[i][j] = (image.getBandLinkedList().get(i).getMagneticBand()[2*j]*256+image.getBandLinkedList().get(i).getMagneticBand()[2*j+1])/3;
                }
            }
        }
        double[][] avg = AVG(IMA,image.getNC(),image.getNL()), avgsq = AvgSq(IMA,image.getNC(),image.getNL()), sd = SD(avg,avgsq);
        littleConversion(IMA8bit,IMA,avg,sd,image);
        IMA8bit = getMagneticBandFromBandList(IMA8bit);
        return IMA8bit;
    }

    //method to convert a 16Bit amplitude image into an 8-bits image
    public static Image convert16bitAmplitudeIn8bit(Image image){
        Image IMA8bit = new Image(image.getNC(), image.getNL(), image.getNbrOfBands(), Image.FORMAT_8BITS, image.getCreationMode());
        int[][] IMA = new int[image.getNbrOfBands()][image.getSizeOfBand()/2];
        for(int i = 0; i<image.getNbrOfBands();i++){
            for (int j = 0; j < IMA8bit.getSizeOfBand()/(IMA8bit.getNbrOfBands()); j++) {
                if((2*j+1)<image.getSizeOfBand()/image.getNbrOfBands()){
                    IMA[i][j] = image.getBandLinkedList().get(i).getMagneticBand()[2*j]*256+image.getBandLinkedList().get(i).getMagneticBand()[2*j+1];
                }
            }
        }
        double[][] avg = AVG(IMA,image.getNC(),image.getNL()), avgsq = AvgSq(IMA,image.getNC(),image.getNL()), sd = SD(avg,avgsq);
        littleConversion(IMA8bit,IMA,avg,sd,image);
        IMA8bit = getMagneticBandFromBandList(IMA8bit);
        return IMA8bit;
    }

    //method to transform a 16bits pixel into an 8bits pixel and assign it to its bands
    public static void littleConversion(Image image, int[][]IMA, double[][]avg, double[][]sd, Image originalImage ){
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            Band band = new Band(image.getNC(),image.getNL(),image.getFormat());
            band.setBandOrder(originalImage.getBandLinkedList().get(i).getBandOrder());
            image.getBandLinkedList().add(band);
        }
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for(int j =0; j<image.getSizeOfBand()/image.getNbrOfBands();j++){
                image.getBandLinkedList().get(i).getMagneticBand()[j] = Math.min((int)((255*IMA[i][j])/(avg[i][0]+3*sd[i][0])),255);
            }
            image.getBandLinkedList().get(i).getScrollPaneBandLine();
            image.getBandLinkedList().get(i).transformBandToTable();
        }
    }

    /**
     * Method to create the image overview on the Display Image Page
     * @param img
     * @param arrayOfColor
     * @return
     */
    public static AnchorPane[][] ImageOverview(Image img, AnchorPane[][] arrayOfColor){
        int red = 0, green = 0, blue = 0;
        LinkedList<Band> imageBand = img.getBandLinkedList();
        arrayOfColor = new AnchorPane[img.getNL()][img.getNC()];
        if(img.getNbrOfBands()<=3) {
            for (int i = 0; i < img.getNL(); i++) {
                for (int j =0; j<img.getNC();j++){
                    AnchorPane anchorPane = new AnchorPane();
                    anchorPane.setPrefHeight(10); anchorPane.setPrefWidth(10);
                    if (imageBand.size()==1){
                        red = (int)(imageBand.get(0).getBandTable()[i][j]);
                        green = (int)(imageBand.get(0).getBandTable()[i][j]);
                        blue = (int)(imageBand.get(0).getBandTable()[i][j]);
                        anchorPane.setStyle("-fx-background-color: rgb("+red+","+green+"," +blue+")");
                    }else if (imageBand.size()==2){
                        red = ((imageBand.get(0).getBandOrder()== 0)? (int)(imageBand.get(0).getBandTable()[i][j]): red);
                        green = ((imageBand.get(0).getBandOrder()== 1)? (int)(imageBand.get(0).getBandTable()[i][j]): green);
                        blue = ((imageBand.get(0).getBandOrder()== 2)? (int)(imageBand.get(0).getBandTable()[i][j]): blue);
                        red = ((imageBand.get(1).getBandOrder()== 0)? (int)(imageBand.get(1).getBandTable()[i][j]): red);
                        green = ((imageBand.get(1).getBandOrder()== 1)? (int)(imageBand.get(1).getBandTable()[i][j]): green);
                        blue = ((imageBand.get(1).getBandOrder()== 2)? (int)(imageBand.get(1).getBandTable()[i][j]): blue);
                        anchorPane.setStyle("-fx-background-color: rgb("+red+","+green+"," +blue+")");
                    }else {
                        anchorPane.setStyle("-fx-background-color: rgb("+imageBand.get(0).getBandTable()[i][j]+","
                                                                        +imageBand.get(1).getBandTable()[i][j]+","
                                                                        +imageBand.get(2).getBandTable()[i][j]+")");
                    }
                    arrayOfColor[i][j] = anchorPane;
                    red = 0; green = 0; blue = 0;
                }
            }
        }
        return arrayOfColor;
    }

    //check if a number is negatif, throws exception
    public static void checkIfNumberNegative(int number) throws NegativeNumberException{
        if(number<0) throw new NegativeNumberException("negative number found");
    }

    //method to display dialogbox for errors
    public static void displayErrorDialog(String errorMessage, StackPane stackPane){
        JFXDialogLayout content = new JFXDialogLayout();
        Label text = new Label("Error");
        text.setStyle("-fx-text-fill: red; -fx-font-family: Roboto;");
        JFXButton button = new JFXButton("OK");
        button.setStyle("-fx-font-family: Roboto");
        content.setActions(button);
        content.setHeading(text);
        content.setBody(new Text(errorMessage));
        JFXDialog dialog = new JFXDialog(stackPane,content, JFXDialog.DialogTransition.CENTER);
        button.setOnAction(e->{dialog.close();});
        dialog.show();
    }

    //method to extract the ZOI from an Image
    public static Image getTheZOI(Image image, int startX, int startY, int endX, int endY){
        Image zoi = new Image(endX-startX+1,endY-startY+1,image.getNbrOfBands(),image.getFormat(),image.getCreationMode());
        int[][]temp = new int[image.getBandLinkedList().size()][(endY-startY+1)*(endX-startX+1)];
        LinkedList<Band> zoiBand = new LinkedList<>();
        int a, j = 0;
        for(int i=0; i<image.getBandLinkedList().size();i++){
            for(int k = startY; k<=endY; k++){
                a = image.getNC()*k+startX;
                for (int l = 0; l <=(endX-startX) ; l++) {
                    temp[i][j]= image.getBandLinkedList().get(i).getMagneticBand()[a+l];
                    j++;
                }
            }
            j = 0;
        }
        for(int i=0; i<image.getNbrOfBands(); i++){
            Band band = new Band(endX-startX+1, endY-startY+1, image.getFormat());
            band.setMagneticBand(temp[i]);
            band.getScrollPaneBandLine();
            band.transformBandToTable();
            zoiBand.add(band);
        }
        zoi.setBandLinkedList(zoiBand);
        zoi = getMagneticBandFromBandList(zoi);
        return zoi;
    }

    //method to get the magnetic band of an image from its different bands
    public static Image getMagneticBandFromBandList(Image image){
        int[]magneticBand = new int[image.getSizeOfBand()];
        int k = 0, a =0, b=0;
        while(a<(image.getSizeOfBand() / (image.getNbrOfBands() * image.getNC())) && b<image.getBandLinkedList().get(0).getSizeOfBand()) {
            for (int i = 0; i < image.getBandLinkedList().size(); i++) {
                for (int j = b; j <  b + (image.getSizeOfBand()/(image.getNbrOfBands()*image.getNL())); j++) {
                    magneticBand[k] = image.getBandLinkedList().get(i).getMagneticBand()[j];
                    k++;
                }
            }
            a++;b+=(image.getSizeOfBand()/(image.getNbrOfBands()*image.getNL()));
        }
        image.setMagneticBand(magneticBand);
        return image;
    }

    //method create a copy of the image with zero border filling
    //Cx, Cy center coordinate of the mask
    public static void zeroFillingMethod(LinkedList<int[][]> bandTablesBordered, Image image, int Cx, int Cy ){
        for (int i = 0; i < bandTablesBordered.size() ; i++) {
            for (int j = 0; j <bandTablesBordered.get(0).length ; j++) {
                for (int k = 0; k <bandTablesBordered.get(0)[0].length ; k++) {
                    if(j<Cy||j>(Cy+image.getNL()-1))
                        bandTablesBordered.get(i)[j][k] = 0;
                    else if(k<Cx||k>(Cx+image.getNC()-1))
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
                for (int k = 0; k <image.getNC() ; k++) {
                    bandTablesBordered.get(i)[j][k+Cx] = bandTablesBordered.get(i)[j+2][k+Cx];
                }
            }

            for (int j = Cy+image.getNL(); j <= bandTablesBordered.get(0).length-1 ; j++) {
                for (int k = 0; k < image.getNC() ; k++) {
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
                for (int k = Cx+image.getNC(); k <= bandTablesBordered.get(0)[0].length-1 ; k++) {
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
                for (int k = 0; k <image.getNC() ; k++) {
                    bandTablesBordered.get(i)[j][k+Cx] = bandTablesBordered.get(i)[j+image.getNL()][k+Cx];
                }
            }

            for (int j = Cy+ image.getNL(); j <= bandTablesBordered.get(0).length-1 ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    bandTablesBordered.get(i)[j][k+Cx] = bandTablesBordered.get(i)[j-image.getNL()][k+Cx];
                }
            }

            //part to circularly fill the columns
            for (int j = 0; j < bandTablesBordered.get(0).length ; j++) {
                for (int k = Cx-1; k >=0 ; k--) {
                    bandTablesBordered.get(i)[j][k] = bandTablesBordered.get(i)[j][k+image.getNC()];
                }
            }

            for (int j = 0; j < bandTablesBordered.get(0).length; j++) {
                for (int k = Cx+image.getNC(); k <= bandTablesBordered.get(0)[0].length-1 ; k++) {
                    bandTablesBordered.get(i)[j][k] = bandTablesBordered.get(i)[j][k-image.getNC()];
                }
            }
        }
    }

    //method to create the bordered image according to a linear mask
    public static LinkedList<int[][]>  createBorderOnImage(int maskNC, int maskNL, int Cx, int Cy, Image image){
        LinkedList<int[][]> bandTables = new LinkedList<>();
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            int [][] temp = new int[image.getNL()+Cy+(maskNL-Cy-1)][image.getNC()+Cx+(maskNC-Cx-1)];
            bandTables.add(temp);
        }

        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL(); j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    bandTables.get(i)[j+Cy][k+Cx] = image.getBandLinkedList().get(i).getBandTable()[j][k];
                }
            }
        }

        return bandTables;
    }

    //method to create the bandTables receiving image data before its reconstitution
    public static LinkedList<int[][]> createReceivingBandTables(Image image){
        LinkedList<int[][]> IMA = new LinkedList<>();
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            int[][] temp = new int[image.getNL()][image.getNC()];
            IMA.add(temp);
        }
        return IMA;
    }

    //method to create the bandTables receiving image data before its reconstitution
    public static LinkedList<float[][]> createReceivingBandTablesFloat(Image image){
        LinkedList<float[][]> IMA = new LinkedList<>();
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            float[][] temp = new float[image.getNL()][image.getNC()];
            IMA.add(temp);
        }
        return IMA;
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

    public static Image linearConvolution(Image image, LinearMask linearMask){
        LinkedList<int [][]> bandTablesBordered = createBorderOnImage(linearMask.getNCmask(),linearMask.getNLmask(),linearMask.getCenterX(),
                linearMask.getCenterY(), image);
        LinkedList<int [][]> IMA = createReceivingBandTables(image);
        LinkedList<int [][]> IMG = new LinkedList<>();

      determineBorderFillingMethod(bandTablesBordered,linearMask.getBorderFeeling(),image,linearMask.getCenterX(),linearMask.getCenterY());

        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = convolutionPixel((k+linearMask.getCenterX()),(j+linearMask.getCenterY()),
                            linearMask,bandTablesBordered.get(i));
                }
            }
        }
        IMG = transformImageInLinkedListTable(image);
        int[] maxValueIMA = findMaxGrayScale(IMA,image), oldMaxValueIMG = findMaxGrayScale(IMG,image);

        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMG.get(i)[j][k] = (oldMaxValueIMG[i]*IMA.get(i)[j][k])/maxValueIMA[i];
                }
            }
        }

        Image convolutedImage = reconstituteImage(IMG, image);

        return convolutedImage;
    }

    //method to find the greater greyscale in a band
    public static int [] findMaxGrayScale(LinkedList<int[][]>bandTables, Image img){
        int [] maxValues = new int[bandTables.size()];
        for (int i = 0; i <bandTables.size() ; i++) {
            maxValues[i] = bandTables.get(i)[0][0];
            for (int j = 0; j <img.getNL() ; j++) {
                for (int k = 0; k <img.getNC() ; k++) {
                    if(bandTables.get(i)[j][k]>maxValues[i])
                        maxValues[i] = bandTables.get(i)[j][k];
                }
            }
        }
        return  maxValues;
    }

    public static float [] findMaxGrayScaleFloat(LinkedList<float[][]>bandTables, Image img){
        float [] maxValues = new float[bandTables.size()];
        for (int i = 0; i <bandTables.size() ; i++) {
            maxValues[i] = bandTables.get(i)[0][0];
            for (int j = 0; j <img.getNL() ; j++) {
                for (int k = 0; k <img.getNC() ; k++) {
                    if(bandTables.get(i)[j][k]>maxValues[i])
                        maxValues[i] = bandTables.get(i)[j][k];
                }
            }
        }
        return  maxValues;
    }

    public static Image reconstituteImage(LinkedList<int[][]> bandTables, Image image){
        Image reconstitutedImage = new Image(image.getNC(),image.getNL(),image.getNbrOfBands(),image.getFormat(),image.getCreationMode());
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            Band band = new Band(image.getNC(),image.getNL(),image.getFormat());
            band.setBandOrder(image.getBandLinkedList().get(i).getBandOrder());
            band.setBandTable(bandTables.get(i));
            band.transformTableToBand();
            band.getScrollPaneBandLine();
            reconstitutedImage.getBandLinkedList().add(band);
        }

        reconstitutedImage = getMagneticBandFromBandList(reconstitutedImage);
        return reconstitutedImage;
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

    //method to determine the borderFilling method to use
    public static void determineBorderFillingMethod(LinkedList<int[][]> bandTablesBordered, String borderFilling, Image image, int Cx, int Cy){
        if (borderFilling.equals(LinearMask.CIRCULAR_METHOD))
            circularFillingMethod(bandTablesBordered,image,Cx,Cy);
        else if(borderFilling.equals(LinearMask.SYMMETRIC_METHOD))
            symmetricFillingMethod(bandTablesBordered,image,Cx,Cy);
        else if(borderFilling.equals(LinearMask.ZERO_METHOD))
            zeroFillingMethod(bandTablesBordered,image,Cx,Cy);
    }

    //method to fill the different combobox for filling method
    public static LinkedList<String> initializeBorderFilllingCombo(){
        LinkedList<String> store = new LinkedList<>();
        store.add(LinearMask.ZERO_METHOD);
        store.add(LinearMask.SYMMETRIC_METHOD);
        store.add(LinearMask.CIRCULAR_METHOD);
        return store;
    }

    public static LinkedList<int[][]> transformImageInLinkedListTable(Image image){
        LinkedList<int[][]>IMG = new LinkedList<>();
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            int [][] temp = new int[image.getNL()][image.getNC()];
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    temp[j][k] = image.getBandLinkedList().get(i).getBandTable()[j][k];
                }
            }
            IMG.add(temp);
        }
        return IMG;
    }

    public static void printTextureBeforeNormailze(LinkedList<float[][]> intermediare, Image image){
        String s = "";
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    s += intermediare.get(i)[j][k]+ "|||";
                }
                s+= "\n";
            }
            s += "\n\n";
        }
        JOptionPane.showMessageDialog(null,s);
    }

    public static void printTextureBeforeNormailzeDouble(LinkedList<double[][]> intermediare, Image image){
        String s = "";
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    s += intermediare.get(i)[j][k]+ "|||";
                }
                s+= "\n";
            }
            s += "\n\n";
        }
        JOptionPane.showMessageDialog(null,s);
    }

    public static void displayAboutDialog(StackPane stackPane){
        JFXDialogLayout content = new JFXDialogLayout();
        JFXButton button = new JFXButton("OK");
        button.setStyle("-fx-background-color: white; -fx-text-fill: black");
        button.setStyle("-fx-font-family: Roboto");
        content.setActions(button);
        content.setBody(new LoaderProvider().aboutLoader());
        content.setStyle("-fx-background-color: black");
        JFXDialog dialog = new JFXDialog(stackPane,content, JFXDialog.DialogTransition.CENTER);
        button.setOnAction(e->{dialog.close();});
        dialog.show();
    }
}
