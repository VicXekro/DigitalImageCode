package ConcreteImage.Model;

import ConcreteImage.Controller.ToolsProvider;
import Model.ImageWindow;

import java.util.LinkedList;

/**
 * Created by Xekro on 6/14/2017.
 */
public class FirstOrderTextureAnalysisMethods {

    /**First Order Method*/
    //method to Calculate the average/Mean of the whole image
    public static Image averageCalculationProcess(Image image, ImageWindow imageWindow)throws OutOfMemoryError{
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                IMA.get(0)[j][k] = (int)averageOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(0));
                IMA.get(1)[j][k] = (int)averageOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(1));
                IMA.get(2)[j][k] = (int)averageOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(2));
            }
        }

        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to Calculate the standard deviation of the whole image
    public static Image standardCalculationWholeProcess(Image image, ImageWindow imageWindow)throws OutOfMemoryError{
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                IMA.get(0)[j][k] = (int)standardDeviationOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(0));
                IMA.get(1)[j][k] = (int)standardDeviationOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(1));
                IMA.get(2)[j][k] = (int)standardDeviationOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(2));
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to Calculate the entropy of the whole image
    public static Image entropyCalculationWholeProcess(Image image, ImageWindow imageWindow)throws OutOfMemoryError{
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                IMA.get(0)[j][k] = (int)entropyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(0));
                IMA.get(1)[j][k] = (int)entropyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(1));
                IMA.get(2)[j][k] = (int)entropyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(2));
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to Calculate the skewness of the whole image
    public static Image skewnessCalculationWholeProcess(Image image, ImageWindow imageWindow)throws OutOfMemoryError{
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                IMA.get(0)[j][k] = (int)skewnessOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(0));
                IMA.get(1)[j][k] = (int)skewnessOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(1));
                IMA.get(2)[j][k] = (int)skewnessOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(2));
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to Calculate the kurtosis of the whole image
    public static Image kurtosisCalculationWholeProcess(Image image, ImageWindow imageWindow)throws OutOfMemoryError{
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                IMA.get(0)[j][k] = (int)kurtosisOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(0));
                IMA.get(1)[j][k] = (int)kurtosisOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(1));
                IMA.get(2)[j][k] = (int)kurtosisOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                        bandsBorderedTable.get(2));
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    /**Part of the first order parameter*/
    //method to calculate the Average For One Pixel
    public static double averageOnAPixel(int x0, int y0, ImageWindow imageWindow, int[][]bandsBorderedTable){
        int [][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),bandsBorderedTable);
        int maxGreyScale = maxGreyScaleImageWindow(windowedImage);
        double [] probability = firstOrderProbability(maxGreyScale,windowedImage);
        double average = 0;
        for (int i = 0; i < probability.length; i++) {
            average += i*probability[i];
        }

        return average;
    }

    //method to calculate the standard deviation for one pixel
    public static double standardDeviationOnAPixel(int x0, int y0, ImageWindow imageWindow, int[][]bandsBorderedTable){
        int [][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),bandsBorderedTable);
        double[] probability = firstOrderProbability(maxGreyScaleImageWindow(windowedImage),windowedImage);
        double standardDeviation = 0;
        double average = averageOnAPixel(x0,y0,imageWindow,bandsBorderedTable);
        for (int i = 0; i <probability.length ; i++) {
            standardDeviation += Math.pow((i-average),2)*probability[i];
        }
        standardDeviation = Math.sqrt(standardDeviation);
        return standardDeviation;
    }

    //method to calculate the Entropy For One Pixel
    public static double entropyOnAPixel(int x0, int y0, ImageWindow imageWindow, int[][]bandsBorderedTable){
        int [][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),bandsBorderedTable);
        int maxGreyScale = maxGreyScaleImageWindow(windowedImage);
        double [] probability = firstOrderProbability(maxGreyScale,windowedImage);
        double entropy = 0;
        for (int i = 0; i <probability.length ; i++) {
            if(probability[i]!=0)
                entropy+= probability[i]*(Math.log(probability[i])/Math.log(2));
        }
        entropy *= -1;
        return entropy;
    }

    //method to calculate the skewness for one pixel
    public static double skewnessOnAPixel(int x0, int y0, ImageWindow imageWindow, int[][]bandsBorderedTable){
        int [][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),bandsBorderedTable);
        int maxGreyScale = maxGreyScaleImageWindow(windowedImage);
        double [] probability = firstOrderProbability(maxGreyScale,windowedImage);
        double average = averageOnAPixel(x0,y0,imageWindow,bandsBorderedTable);
        double standardDeviation = standardDeviationOnAPixel(x0, y0,imageWindow,bandsBorderedTable);
        //System.out.println("average :"+average+" standard Deviation: "+standardDeviation);
        double skewness = 0;
        for (int i = 0; i <probability.length ; i++) {
            skewness+= Math.pow((i-average),3)*probability[i];
        }
        skewness = skewness/Math.pow(standardDeviation,3);
        return skewness;
    }

    //method to calculate the kurtosis for one pixel
    public static double kurtosisOnAPixel(int x0, int y0, ImageWindow imageWindow, int[][]bandsBorderedTable){
        int [][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),bandsBorderedTable);
        int maxGreyScale = maxGreyScaleImageWindow(windowedImage);
        double [] probability = firstOrderProbability(maxGreyScale,windowedImage);
        double average = averageOnAPixel(x0,y0,imageWindow,bandsBorderedTable);
        double standardDeviation = standardDeviationOnAPixel(x0, y0,imageWindow,bandsBorderedTable);
        double skewness = 0;
        for (int i = 0; i <probability.length ; i++) {
            skewness+= Math.pow((i-average),4)*probability[i];
        }
        skewness = skewness/Math.pow(standardDeviation,4);
        return skewness;
    }

    //method to determine the max greyscale in a image window
    public static int maxGreyScaleImageWindow(int[][] imageWindow){
        int maxGreyscale = imageWindow[0][0];
        for (int i = 0; i < imageWindow.length; i++) {
            for (int j = 0; j <imageWindow[0].length ; j++) {
                if(maxGreyscale<imageWindow[i][j])
                    maxGreyscale = imageWindow[i][j];
            }
        }
        return maxGreyscale;
    }

    //method to calculate the occurrence of a greyscale in an image window in the case of first order texture analysis
    public static double [] firstOrderProbability(int maxGreyScale,int[][] imageWindow){
        double [] probability = new double[maxGreyScale+1];
        for (int i = 0; i <probability.length ; i++) {
            for (int j = 0; j <imageWindow.length ; j++) {
                for (int k = 0; k <imageWindow[0].length ; k++) {
                    if(i == imageWindow[j][k])
                        probability[i]++;
                }
            }
        }
        for (int i = 0; i <probability.length ; i++) {
            probability[i] /= (imageWindow.length * imageWindow[0].length);
        }
        return probability;
    }


}
