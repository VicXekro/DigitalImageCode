package ConcreteImage.Model;

import Control.ToolsProvider;
import Model.ImageWindow;
import Model.Texture;

import java.util.LinkedList;

/**
 * Created by Xekro on 6/15/2017.
 */
public class SecondOrderTextureAnalysisMethods {

    public static Image sEnergy(Image image, ImageWindow imageWindow, String direction, int interPixelDistance)throws OutOfMemoryError{
        LinkedList<int[][]> ENR_Image = ConcreteImage.Controller.ToolsProvider.createReceivingBandTables(image);
        LinkedList<float[][]> intermediate = ConcreteImage.Controller.ToolsProvider.createReceivingBandTablesFloat(image);
        LinkedList<int[][]> bandTablesbordered = ConcreteImage.Controller.ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ConcreteImage.Controller.ToolsProvider.determineBorderFillingMethod(bandTablesbordered,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                intermediate.get(0)[j][k] = sEnergyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(0),direction,interPixelDistance);
                intermediate.get(1)[j][k] = sEnergyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(1),direction,interPixelDistance);
                intermediate.get(2)[j][k] = sEnergyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(2),direction,interPixelDistance);
            }
        }
        float[] newMaxValue = ConcreteImage.Controller.ToolsProvider.findMaxGrayScaleFloat(intermediate,image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                ENR_Image.get(0)[j][k] = (int)((255*intermediate.get(0)[j][k])/newMaxValue[0]);
                ENR_Image.get(1)[j][k] = (int)((255*intermediate.get(1)[j][k])/newMaxValue[1]);
                ENR_Image.get(2)[j][k] = (int)((255*intermediate.get(2)[j][k])/newMaxValue[2]);
            }
        }
        ConcreteImage.Controller.ToolsProvider.modifyColorFromArray(ENR_Image,image);
        return image;
    }

    public static Image sContrast(Image image, ImageWindow imageWindow, String direction, int interPixelDistance)throws OutOfMemoryError{
        LinkedList<int[][]> bandTablesbordered = ConcreteImage.Controller.ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ConcreteImage.Controller.ToolsProvider.determineBorderFillingMethod(bandTablesbordered,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> contrastedImage =  ConcreteImage.Controller.ToolsProvider.createReceivingBandTables(image);
        LinkedList<float[][]> intermediate =  ConcreteImage.Controller.ToolsProvider.createReceivingBandTablesFloat(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                intermediate.get(0)[j][k] = sContrastOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(0),direction,interPixelDistance);
                intermediate.get(1)[j][k] = sContrastOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(1),direction,interPixelDistance);
                intermediate.get(2)[j][k] = sContrastOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(2),direction,interPixelDistance);
            }
        }
        float[] newMaxValue = ConcreteImage.Controller.ToolsProvider.findMaxGrayScaleFloat(intermediate,image);
        int[] oldMaxValue = ConcreteImage.Controller.ToolsProvider.findMaxGrayScale(ConcreteImage.Controller.ToolsProvider.transformImagetoLinkedList(image),image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                contrastedImage.get(0)[j][k] = (int)((oldMaxValue[0]*intermediate.get(0)[j][k])/newMaxValue[0]);
                contrastedImage.get(1)[j][k] = (int)((oldMaxValue[1]*intermediate.get(1)[j][k])/newMaxValue[1]);
                contrastedImage.get(2)[j][k] = (int)((oldMaxValue[2]*intermediate.get(2)[j][k])/newMaxValue[2]);
            }
        }
        ConcreteImage.Controller.ToolsProvider.modifyColorFromArray(contrastedImage,image);
        return image;
    }

    public static Image sDissiminarity(Image image, ImageWindow imageWindow, String direction, int interPixelDistance)throws OutOfMemoryError{
        LinkedList<int[][]> bandTablesbordered = ConcreteImage.Controller.ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ConcreteImage.Controller.ToolsProvider.determineBorderFillingMethod(bandTablesbordered,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> dissimiImage =  ConcreteImage.Controller.ToolsProvider.createReceivingBandTables(image);
        LinkedList<float[][]> intermediate =  ConcreteImage.Controller.ToolsProvider.createReceivingBandTablesFloat(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                intermediate.get(0)[j][k] = sDissiminarityOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(0),direction,interPixelDistance);
                intermediate.get(1)[j][k] = sDissiminarityOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(1),direction,interPixelDistance);
                intermediate.get(2)[j][k] = sDissiminarityOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                        imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(2),direction,interPixelDistance);
            }
        }
        float[] newMaxValue = ConcreteImage.Controller.ToolsProvider.findMaxGrayScaleFloat(intermediate,image);
        int[] oldMaxValue = ConcreteImage.Controller.ToolsProvider.findMaxGrayScale(ConcreteImage.Controller.ToolsProvider.transformImagetoLinkedList(image),image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                dissimiImage.get(0)[j][k] = (int)((oldMaxValue[0]*intermediate.get(0)[j][k])/newMaxValue[0]);
                dissimiImage.get(1)[j][k] = (int)((oldMaxValue[1]*intermediate.get(1)[j][k])/newMaxValue[1]);
                dissimiImage.get(2)[j][k] = (int)((oldMaxValue[2]*intermediate.get(2)[j][k])/newMaxValue[2]);
            }
        }
        ConcreteImage.Controller.ToolsProvider.modifyColorFromArray(dissimiImage,image);
        return image;
    }

    /**Process on A pixel*/
    public static float sContrastOnAPixel(int x0, int y0, int NCmask, int NLmask, int Cx, int Cy, int[][]bandTable,
                                        String direction, int interPixelDistance){
        float contrastedPixel = 0;
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0, y0,NCmask, NLmask, Cx, Cy,bandTable);
        int maxGrayScale = FirstOrderTextureAnalysisMethods.maxGreyScaleImageWindow(windowedImage);
        double[][] coocurrenceMatrix = determineDirectionToUseCoocurence(direction,interPixelDistance,windowedImage,maxGrayScale);
        for (int i = 0; i <=maxGrayScale ; i++)
            for (int j = 0; j <=maxGrayScale; j++)
                contrastedPixel += Math.pow((i-j),2)*coocurrenceMatrix[i][j];
        return contrastedPixel;
    }

    public static float sDissiminarityOnAPixel(int x0, int y0, int NCmask, int NLmask, int Cx, int Cy, int[][]bandTable,
                                        String direction, int interPixelDistance){
        float dissiminarityPixel = 0;
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0, y0,NCmask, NLmask, Cx, Cy,bandTable);
        int maxGrayScale = FirstOrderTextureAnalysisMethods.maxGreyScaleImageWindow(windowedImage);
        double[][] coocurrenceMatrix = determineDirectionToUseCoocurence(direction,interPixelDistance,windowedImage,maxGrayScale);
        for (int i = 0; i <=maxGrayScale ; i++)
            for (int j = 0; j <=maxGrayScale; j++)
                dissiminarityPixel += Math.abs(i-j)*coocurrenceMatrix[i][j];
        return dissiminarityPixel;
    }

    public static float sEnergyOnAPixel(int x0, int y0, int NCmask, int NLmask, int Cx, int Cy, int[][]bandTable,
                                        String direction, int interPixelDistance){
        float energiedPixel = 0;
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0, y0,NCmask, NLmask, Cx, Cy,bandTable);
        int maxGrayScale = FirstOrderTextureAnalysisMethods.maxGreyScaleImageWindow(windowedImage);
        double[][] coocurrenceMatrix = determineDirectionToUseCoocurence(direction,interPixelDistance,windowedImage,maxGrayScale);
        for (int i = 0; i <=maxGrayScale ; i++)
            for (int j = 0; j <=maxGrayScale; j++)
                energiedPixel += coocurrenceMatrix[i][j]*coocurrenceMatrix[i][j];
        return energiedPixel;
    }

    //method to determine which co_occurrence matrix method to apply
    public static double[][] determineDirectionToUseCoocurence(String direction, int interPixelDistance, int[][]imageWindow, int maxGrayScale){
        double[][] coocurrenceMatrix = null;
        if (direction.equals(Texture.DIRECTION_0))
            coocurrenceMatrix = co_occurrenceMatrix_0_Direction(imageWindow,maxGrayScale,interPixelDistance);
        else if (direction.equals(Texture.DIRECTION_45))
            coocurrenceMatrix = co_occurrenceMatrix_45_Direction(imageWindow,maxGrayScale,interPixelDistance);
        else if (direction.equals(Texture.DIRECTION_90))
            coocurrenceMatrix = co_occurrenceMatrix_90_Direction(imageWindow,maxGrayScale,interPixelDistance);
        else if (direction.equals(Texture.DIRECTION_135))
            coocurrenceMatrix = co_occurrenceMatrix_135_Direction(imageWindow,maxGrayScale,interPixelDistance);
        return coocurrenceMatrix;
    }

    /**Coocurrence Matrix Algorithm*/
    //method to obtain the coocurence matrix at 0
    public static double [][] co_occurrenceMatrix_0_Direction(int[][] imageWindow, int maxGrayScale,int interPixelDistance){
        double [][] coocurrenceMatrix = new double[maxGrayScale+1][maxGrayScale+1];
        for (int i = 0; i <=imageWindow.length-1 ; i++) {
            for (int j = 0; j <=imageWindow[0].length-1-interPixelDistance ; j++) {
                int a = imageWindow[i][j], b = imageWindow[i][j+interPixelDistance];
                coocurrenceMatrix[a][b]++;
                //coocurrenceMatrix[b][a]++;
            }
        }
        for (int i = 0; i <=maxGrayScale ; i++) {
            for (int j = 0; j <=maxGrayScale ; j++) {
                coocurrenceMatrix[i][j] /=  (imageWindow.length*(imageWindow[0].length - interPixelDistance));
            }
        }
        return coocurrenceMatrix;
    }

    //method to obtain the coocurrence matrix at 45
    public static double [][] co_occurrenceMatrix_45_Direction(int[][] imageWindow, int maxGrayScale,int interPixelDistance){
        double [][] coocurrenceMatrix = new double[maxGrayScale+1][maxGrayScale+1];
        for (int i = 0; i <=imageWindow.length-1-interPixelDistance ; i++) {
            for (int j = 0; j <=imageWindow[0].length-1-interPixelDistance ; j++) {
                int a = imageWindow[i][j], b = imageWindow[i+interPixelDistance][j+interPixelDistance];
                coocurrenceMatrix[a][b]++;
                //coocurrenceMatrix[b][a]++;
            }
        }
        for (int i = 0; i <=maxGrayScale ; i++) {
            for (int j = 0; j <=maxGrayScale ; j++) {
                coocurrenceMatrix[i][j] /=  ((imageWindow.length - interPixelDistance)*(imageWindow[0].length - interPixelDistance));
            }
        }
        return coocurrenceMatrix;
    }

    //method to obtain the coocurrence matrix at 90
    public static double[][] co_occurrenceMatrix_90_Direction(int[][] imageWindow, int maxGrayScale,int interPixelDistance){
        double [][] coocurrenceMatrix = new double[maxGrayScale+1][maxGrayScale+1];
        for (int i = interPixelDistance; i <=imageWindow.length-1 ; i++) {
            for (int j = 0; j <=imageWindow[0].length-1 ; j++) {
                int a = imageWindow[i][j], b = imageWindow[i-interPixelDistance][j];
                coocurrenceMatrix[a][b]++;
                //coocurrenceMatrix[b][a]++;
            }
        }
        for (int i = 0; i <=maxGrayScale ; i++) {
            for (int j = 0; j <=maxGrayScale ; j++) {
                coocurrenceMatrix[i][j] /=  ((imageWindow.length - interPixelDistance)*imageWindow[0].length);
            }
        }
        return coocurrenceMatrix;
    }

    //method to obtain the coocurrence matrix at 135
    public static double[][] co_occurrenceMatrix_135_Direction(int[][] imageWindow, int maxGrayScale,int interPixelDistance){
        double [][] coocurrenceMatrix = new double[maxGrayScale+1][maxGrayScale+1];
        for (int i = interPixelDistance; i <=imageWindow.length-1 ; i++) {
            for (int j = interPixelDistance; j <=imageWindow[0].length-1 ; j++) {
                int a = imageWindow[i][j], b = imageWindow[i-interPixelDistance][j-interPixelDistance];
                coocurrenceMatrix[a][b]++;
                //coocurrenceMatrix[b][a]++;
            }
        }
        for (int i = 0; i <=maxGrayScale ; i++) {
            for (int j = 0; j <=maxGrayScale ; j++) {
                coocurrenceMatrix[i][j] /=  ((imageWindow.length - interPixelDistance)*(imageWindow[0].length-interPixelDistance));
            }
        }
        return coocurrenceMatrix;
    }
}
