package Model;

import Control.ToolsProvider;

import java.util.LinkedList;

/**
 * Created by Xekro on 5/27/2017.
 */
public class Texture {

    /**First Order Constants*/
    public static final String FIRST_ORDER_AVERAGE = "Average or Mean";
    public static final String FIRST_ORDER_ENTROPY = "Entropy";
    public static final String FIRST_ORDER_STANDARD_DEVIATION = "Standard deviation";
    public static final String FIRST_ORDER_SKEWNESS = "Skewness";
    public static final String FIRST_ORDER_KURTOSIS = "Kurtosis";

    /**Second Order Constants*/
    public static final String SECOND_ORDER_ENERGY = "Energy";
    public static final String SECOND_ORDER_ENTROPY = "Entropy";
    public static final String SECOND_ORDER_CONTRAST = "Contrast or Inertia";
    public static final String SECOND_ORDER_CORRELATION = "Correlation";
    public static final String SECOND_ORDER_CLUSTER_PROMINENCE = "Cluster Prominence";
    public static final String SECOND_ORDER_DISSIMINARITY = "Dissimilarity";

    /**Direction*/
    public static final String DIRECTION_0 = "0 degree";
    public static final String DIRECTION_45 = "45 degree";
    public static final String DIRECTION_90 = "90 degree";
    public static final String DIRECTION_135 = "135 degree";
    public static LinkedList<String> directionList(){
        LinkedList<String> direction = new LinkedList<>();
        direction.add(DIRECTION_0);
        direction.add(DIRECTION_45);
        direction.add(DIRECTION_90);
        direction.add(DIRECTION_135);
        return direction;
    }

    /**GrayScale Range Constants*/
    public static final String GRAYSCALE_SHORT_RUN_LOW = "Short Run Low Emphasis";
    public static final String GRAYSCALE_SHORT_RUN_HIGH = "Short Run High Emphasis";
    public static final String GRAYSCALE_LONG_RUN_LOW = "Long Run Low Emphasis";
    public static final String GRAYSCALE_LONG_RUN_HIGH = "Long Run High Emphasis";
    public static final String GRAYSCALE_DISTRIBUTION_GRAYSCALE = "Distribution of greyscale";
    public static final String GRAYSCALE_DISTRIBUTION_LENGTH_RANGE = "Distribution of lengths ranges";
    public static final String GRAYSCALE_PERCENTAGE = "Greyscales percentage";
    public static final String GRAYSCALE_LOW_GRAYSCALE = "Low greyscale range";
    public static final String GRAYSCALE_HIGH_GRAYSCALE = "High greyscale range";

    /**First Order Method*/
    //method to Calculate the average/Mean of the whole image
    public static Image averageCalculationProcess(Image image, ImageWindow imageWindow){
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = (int)averageOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                            bandsBorderedTable.get(i));
                }
            }
        }


        Image averageImageParameter = ToolsProvider.reconstituteImage(IMA,image);
        return averageImageParameter;
    }

    //method to Calculate the entropy of the whole image
    public static Image entropyCalculationWholeProcess(Image image, ImageWindow imageWindow){
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = (int)entropyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                            bandsBorderedTable.get(i));
                }
            }
        }
        Image entropyImage = ToolsProvider.reconstituteImage(IMA, image);
        return entropyImage;
    }

    //method to Calculate the standard deviation of the whole image
    public static Image standardCalculationWholeProcess(Image image, ImageWindow imageWindow){
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = (int)standardDeviationOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                            bandsBorderedTable.get(i));
                }
            }
        }
        Image standardDeviationImage = ToolsProvider.reconstituteImage(IMA, image);
        return standardDeviationImage;
    }

    //method to Calculate the skewness of the whole image
    public static Image skewnessCalculationWholeProcess(Image image, ImageWindow imageWindow){
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = (int)skewnessOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                            bandsBorderedTable.get(i));
                }
            }
        }
        Image skewnessImage = ToolsProvider.reconstituteImage(IMA,image);
        return skewnessImage;
    }

    //method to Calculate the kurtosis of the whole image
    public static Image kurtosisCalculationWholeProcess(Image image, ImageWindow imageWindow){
        LinkedList<int[][]> bandsBorderedTable = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandsBorderedTable,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = (int)kurtosisOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow,
                            bandsBorderedTable.get(i));
                }
            }
        }
        Image kurtosisImage = ToolsProvider.reconstituteImage(IMA,image);
        return kurtosisImage;
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

    //method to calculate the skewness for one pixel
    public static double skewnessOnAPixel(int x0, int y0, ImageWindow imageWindow, int[][]bandsBorderedTable){
        int [][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),bandsBorderedTable);
        int maxGreyScale = maxGreyScaleImageWindow(windowedImage);
        double [] probability = firstOrderProbability(maxGreyScale,windowedImage);
        double average = averageOnAPixel(x0,y0,imageWindow,bandsBorderedTable);
        double standardDeviation = standardDeviationOnAPixel(x0, y0,imageWindow,bandsBorderedTable);
        System.out.println("average :"+average+" standard Deviation: "+standardDeviation);
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

    /**Second order parameter*/

    public static Image sContrast(Image image, ImageWindow imageWindow, String direction, int interPixelDistance){
        LinkedList<int[][]> bandTablesbordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandTablesbordered,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> contrastedImage = ToolsProvider.createReceivingBandTables(image);
        LinkedList<float[][]> intermediate = ToolsProvider.createReceivingBandTablesFloat(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    intermediate.get(i)[j][k] = sContrastOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                            imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(i),direction,interPixelDistance);
                }
            }
        }
        ToolsProvider.printTextureBeforeNormailze(intermediate,image);
        float[] newMaxValue = ToolsProvider.findMaxGrayScaleFloat(intermediate,image);
        int[] oldMaxValue = ToolsProvider.findMaxGrayScale(ToolsProvider.transformImageInLinkedListTable(image),image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    contrastedImage.get(i)[j][k] = (int)((oldMaxValue[i]*intermediate.get(i)[j][k])/newMaxValue[i]);
                }
            }
        }
        Image imageContrasted = ToolsProvider.reconstituteImage(contrastedImage,image);
        return imageContrasted;
    }

    public static Image sEnergy(Image image,ImageWindow imageWindow, String direction, int interPixelDistance){
        LinkedList<int[][]> ENR_Image = ToolsProvider.createReceivingBandTables(image);
        LinkedList<float[][]> intermediate = ToolsProvider.createReceivingBandTablesFloat(image);
        LinkedList<int[][]> bandTablesbordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandTablesbordered,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    intermediate.get(i)[j][k] = sEnergyOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                            imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(i),direction,interPixelDistance);
                }
            }
        }
        ToolsProvider.printTextureBeforeNormailze(intermediate,image);
        float[] newMaxValue = ToolsProvider.findMaxGrayScaleFloat(intermediate,image);
        int[] oldMaxValue = ToolsProvider.findMaxGrayScale(ToolsProvider.transformImageInLinkedListTable(image),image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    ENR_Image.get(i)[j][k] = (int)((oldMaxValue[i]*intermediate.get(i)[j][k])/newMaxValue[i]);
                }
            }
        }
        Image energyImage = ToolsProvider.reconstituteImage(ENR_Image,image);
        return energyImage;
    }

    public static void sEntropy(){}

    public static void sCorrelation() {}

    public static Image sDissiminarity(Image image, ImageWindow imageWindow, String direction, int interPixelDistance){
        LinkedList<int[][]> bandTablesbordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(),image);
        ToolsProvider.determineBorderFillingMethod(bandTablesbordered,imageWindow.getBorderFeeling(),image,imageWindow.getCenterX(),imageWindow.getCenterY());
        LinkedList<int[][]> dissimiImage = ToolsProvider.createReceivingBandTables(image);
        LinkedList<float[][]> intermediate = ToolsProvider.createReceivingBandTablesFloat(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    intermediate.get(i)[j][k] = sDissiminarityOnAPixel(k+imageWindow.getCenterX(),j+imageWindow.getCenterY(),imageWindow.getNCmask(),
                            imageWindow.getNLmask(),imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesbordered.get(i),direction,interPixelDistance);
                }
            }
        }
        ToolsProvider.printTextureBeforeNormailze(intermediate,image);
        float[] newMaxValue = ToolsProvider.findMaxGrayScaleFloat(intermediate,image);
        int[] oldMaxValue = ToolsProvider.findMaxGrayScale(ToolsProvider.transformImageInLinkedListTable(image),image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    dissimiImage.get(i)[j][k] = (int)((oldMaxValue[i]*intermediate.get(i)[j][k])/newMaxValue[i]);
                }
            }
        }
        Image imageDissiminarity = ToolsProvider.reconstituteImage(dissimiImage,image);
        return imageDissiminarity;
    }

    public static void sClusterProminence(){}

    /**Process on a pixel*/
    public static float sContrastOnAPixel(int x0, int y0, int NCmask, int NLmask, int Cx, int Cy, int[][]bandTable,
                                         String direction, int interPixelDistance){
        float contrastedPixel = 0;
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0, y0,NCmask, NLmask, Cx, Cy,bandTable);
        int maxGrayScale = maxGreyScaleImageWindow(windowedImage);
        double[][] coocurrenceMatrix = determineDirectionToUseCoocurence(direction,interPixelDistance,windowedImage,maxGrayScale);
        for (int i = 0; i <=maxGrayScale ; i++)
            for (int j = 0; j <=maxGrayScale; j++)
                contrastedPixel += Math.pow((i-j),2)*coocurrenceMatrix[i][j];
        return contrastedPixel;
    }

    public static float sDissiminarityOnAPixel(int x0, int y0, int NCmask, int NLmask, int Cx, int Cy, int[][]bandTable,
                                        String direction, int interPixelDistance){
        float dissimiPixel = 0;
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0, y0,NCmask, NLmask, Cx, Cy,bandTable);
        int maxGrayScale = maxGreyScaleImageWindow(windowedImage);
        double[][] coocurrenceMatrix = determineDirectionToUseCoocurence(direction,interPixelDistance,windowedImage,maxGrayScale);
        for (int i = 0; i <=maxGrayScale ; i++)
            for (int j = 0; j <=maxGrayScale; j++)
                dissimiPixel += Math.abs(i-j)*coocurrenceMatrix[i][j];
        return dissimiPixel;
    }

    public static float sEnergyOnAPixel(int x0, int y0, int NCmask, int NLmask, int Cx, int Cy, int[][]bandTable,
                                String direction, int interPixelDistance){
        float energiedPixel = 0;
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0, y0,NCmask, NLmask, Cx, Cy,bandTable);
        int maxGrayScale = maxGreyScaleImageWindow(windowedImage);
        double[][] coocurrenceMatrix = determineDirectionToUseCoocurence(direction,interPixelDistance,windowedImage,maxGrayScale);
        for (int i = 0; i <=maxGrayScale ; i++)
            for (int j = 0; j <=maxGrayScale; j++)
                energiedPixel += coocurrenceMatrix[i][j]*coocurrenceMatrix[i][j];
        return energiedPixel;
    }

    //method to determine which co_occurrence matrix method to apply
    public static double[][] determineDirectionToUseCoocurence(String direction, int interPixelDistance, int[][]imageWindow, int maxGrayScale){
        double[][] coocurrenceMatrix = null;
        if (direction.equals(DIRECTION_0))
            coocurrenceMatrix = co_occurrenceMatrix_0_Direction(imageWindow,maxGrayScale,interPixelDistance);
        else if (direction.equals(DIRECTION_45))
            coocurrenceMatrix = co_occurrenceMatrix_45_Direction(imageWindow,maxGrayScale,interPixelDistance);
        else if (direction.equals(DIRECTION_90))
            coocurrenceMatrix = co_occurrenceMatrix_90_Direction(imageWindow,maxGrayScale,interPixelDistance);
        else if (direction.equals(DIRECTION_135))
            coocurrenceMatrix = co_occurrenceMatrix_135_Direction(imageWindow,maxGrayScale,interPixelDistance);
        return coocurrenceMatrix;
    }
}
