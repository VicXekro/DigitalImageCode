package Model;

import Control.ToolsProvider;

import java.util.LinkedList;

/**
 * Created by Xekro on 5/3/2017.
 */
public class Filter {

    /** Robert Filter*/
    public static final String ROBERT = "Robert";
    private static int[][] maskRobertHorizontal = {{0,0,0},{0,1,0},{0,0,-1}}, maskRobert45Direction = {{-2,-1,0},{-1,0,1},{0,1,2}};
    private static int[][] maskRobertVertical = {{0,0,0},{0,0,1},{0,-1,0}}, maskRobert135Direction = {{0,1,2},{-1,0,1},{-2,-1,0}};
    public static final LinearMask ROBERT_HORIZONTAL = new LinearMask(3,3,3/2,3/2,maskRobertHorizontal,"Robert Horizontal");
    public static final LinearMask ROBERT_VERTICAL = new LinearMask(3,3,3/2,3/2,maskRobertVertical,"Robert Vertical");
    public static final LinearMask ROBERT_45 = new LinearMask(3,3,3/2,3/2,maskRobert45Direction,"Robert 45");
    public static final LinearMask ROBERT_135 = new LinearMask(3,3,3/2,3/2,maskRobert135Direction,"Robert 135");

    /**Sobel Filter*/
    public static final String SOBEL = "Sobel";
    private static int[][] maskSobelHorizontal = {{-1,0,1},{-2,0,2},{-1,0,1}}, maskSobel45Direction = {{0,0,0},{0,1,0},{0,0,-1}};
    private static int[][] maskSobelVertical = {{-1,-2,-1},{0,0,0},{1,2,1}}, maskSobel135Direction = {{0,0,0},{0,0,1},{0,-1,0}};
    public static final LinearMask SOBEL_HORIZONTAL = new LinearMask(3,3,3/2,3/2,maskSobelHorizontal,"Sobel Horizontal");
    public static final LinearMask SOBEL_VERTICAL = new LinearMask(3,3,3/2,3/2,maskSobelVertical,"Sobel Vertical");
    public static final LinearMask SOBEL_45 = new LinearMask(3,3,3/2,3/2,maskSobel45Direction,"Sobel 45");
    public static final LinearMask SOBEL_135 = new LinearMask(3,3,3/2,3/2,maskSobel135Direction,"Sobel 135");

    /**Prewitt Filter*/
    public static final String PREWITT = "Prewitt";
    private static int[][] maskPrewittHorizontal = {{-1,1,1},{-1,-2,1},{-1,1,1}}, maskPrewitt45Direction = {{-1,-1,1},{-1,-2,1},{1,1,1}};
    private static int[][] maskPrewittVertical = {{-1,-1,-1},{1,-2,1},{1,1,1}}, maskPrewitt135Direction = {{1,1,1},{-1,-2,1},{-1,-1,1}};
    public static final LinearMask PREWITT_HORIZONTAL = new LinearMask(3,3,3/2,3/2,maskPrewittHorizontal,"Prewitt Horizontal");
    public static final LinearMask PREWITT_VERTICAL = new LinearMask(3,3,3/2,3/2,maskPrewittVertical,"Prewitt Vertical");
    public static final LinearMask PREWITT_45 = new LinearMask(3,3,3/2,3/2,maskPrewitt45Direction,"Prewitt 45");
    public static final LinearMask PREWITT_135 = new LinearMask(3,3,3/2,3/2,maskPrewitt135Direction,"Prewitt 135");

    /**Kirsh Filter*/
    public static final String KIRSH = "Kirsh";
    private static int[][] maskKirshHorizontal = {{-3,-3,5},{-3,0,5},{-3,-3,5}}, maskKirsh45Direction = {{-3,-3,-3},{-3,1,5},{-3,5,5}};
    private static int[][] maskKirshVertical = {{-3,-3,-3},{-3,0,-3},{5,5,5}}, maskKirsh135Direction = {{-3,5,5},{-3,0,5},{-3,-3,-3}};
    public static final LinearMask KIRSH_HORIZONTAL = new LinearMask(3,3,3/2,3/2,maskKirshHorizontal,"Kirsh Horizontal");
    public static final LinearMask KIRSH_VERTICAL = new LinearMask(3,3,3/2,3/2,maskKirshVertical,"Kirsh Vertical");
    public static final LinearMask KIRSH_45 = new LinearMask(3,3,3/2,3/2,maskKirsh45Direction,"Kirsh 45");
    public static final LinearMask KIRSH_135 = new LinearMask(3,3,3/2,3/2,maskKirsh135Direction,"Kirsh 135");

    /**Average Mask*/
    public static final String AVERAGE = "Average";
    private static int[][] averageMask = {{1,1,1},{1,1,1},{1,1,1}};
    public static  final LinearMask AVERAGE_MASK = new LinearMask(3,3,3/2,3/2,averageMask,"Average Mask");

    public static final String LEE_FILTER = "Lee Filter";
    public static Image leeFilter(int numberOfViews,int imageFactor,ImageWindow imageWindow,Image originalImage){
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int i = 0; i <originalImage.getNbrOfBands() ; i++) {
            for (int j = 0; j <originalImage.getNL() ; j++) {
                for (int k = 0; k <originalImage.getNC() ; k++) {
                    double z = localAverage(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(), bandTablesBordered.get(i),imageWindow);
                    double localVarianceSqr = localVariance(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),bandTablesBordered.get(i),
                            imageWindow,z);
                    double y = z;
                    double sYsqr = (localVarianceSqr - Math.pow(sU,2))/(Math.pow(sU,2)+1);
                    sYsqr = ((sYsqr<0)?0:sYsqr);
                    double b = sYsqr/(Math.pow(z,2)*Math.pow(sU,2)+sYsqr);
                    IMA.get(i)[j][k] = (int)(z + b*(originalImage.getBandLinkedList().get(i).getBandTable()[j][k]-z));
                }
            }
        }

        Image filteredImage = ToolsProvider.reconstituteImage(IMA,originalImage);
        return filteredImage;
    }

    public static final String ALTERNATED_LEE_FILTER = "Alternated Lee Filter";
    public static Image alternatedLeeFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage){
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int i = 0; i < originalImage.getNbrOfBands(); i++) {
            for (int j = 0; j < originalImage.getNL(); j++) {
                for (int k = 0; k < originalImage.getNC(); k++) {
                    double z = localAverage(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(), bandTablesBordered.get(i),imageWindow);
                    double localVarianceSqr = localVariance(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),bandTablesBordered.get(i),
                            imageWindow,z);
                    double sY = ((z!=0)?(Math.sqrt(localVarianceSqr)/z):1);
                    if(sY<sU)
                        IMA.get(i)[j][k] = (int)(z);
                    else{
                        double a = Math.pow(sY,2)+Math.pow(sU,4);
                        double b = originalImage.getBandLinkedList().get(i).getBandTable()[j][k]*
                                (Math.pow(sY,2)-Math.pow(sU,2))+z*(Math.pow(sU,2)+Math.pow(sU,4));
                        b = b/a;
                        IMA.get(i)[j][k] = (int)(b);
                    }
                }
            }
        }
        Image filteredImage = ToolsProvider.reconstituteImage(IMA, originalImage);
        return filteredImage;
    }

    public static final String MAP_FILTER = "Map Filter";
    public static Image mapFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage){
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int i = 0; i <originalImage.getNbrOfBands() ; i++) {
            for (int j = 0; j <originalImage.getNL() ; j++) {
                for (int k = 0; k <originalImage.getNC() ; k++) {
                    double z = localAverage(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(), bandTablesBordered.get(i),imageWindow);
                    double localVarianceSqr = localVariance(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),bandTablesBordered.get(i),
                            imageWindow,z);
                    double sY = ((z!=0)?(Math.sqrt(localVarianceSqr)/z):0);
                    if(sY<sU)
                        IMA.get(i)[j][k] = (int)(z);
                    else{
                        double a = Math.pow(sY,2)*(1+Math.pow(sU,2));
                        double b = originalImage.getBandLinkedList().get(i).getBandTable()[j][k]*(Math.pow(sY,2)-Math.pow(sU,2))+
                                z*Math.pow(sU,2)*(1+Math.pow(sY,2));
                        IMA.get(i)[j][k] = (int)(b/a);
                    }
                }
            }
        }
        Image filteredImage = ToolsProvider.reconstituteImage(IMA,originalImage);
        return filteredImage;
    }

    public static final String GAMMA_FILTER = "Gamma Filter";
    public static Image gammaFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage){
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int i = 0; i <originalImage.getNbrOfBands() ; i++) {
            for (int j = 0; j <originalImage.getNL() ; j++) {
                for (int k = 0; k <originalImage.getNC() ; k++) {
                    double z = localAverage(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(), bandTablesBordered.get(i),imageWindow);
                    double localVarianceSqr = localVariance(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),bandTablesBordered.get(i),
                            imageWindow,z);
                    double sY = ((z!=0)?(Math.sqrt(localVarianceSqr)/z):0);
                    if((Math.pow(sY,2)-Math.pow(sU,2))!=0){
                        double alpha = (1+Math.pow(sU,2))/(Math.pow(sY,2)-Math.pow(sU,2));
                        double t1 = z*(alpha-numberOfViews-1);
                        double t2 = Math.pow(t1,2)+(4*alpha*numberOfViews*originalImage.getBandLinkedList().get(i).getBandTable()[j][k]*z);
                        double t = t1+Math.sqrt(t2);
                        IMA.get(i)[j][k] = (int)(t/(2*alpha));
                    }
                }
            }
        }
        Image filteredImage = ToolsProvider.reconstituteImage(IMA,originalImage);
        return filteredImage;
    }

    public static final String FROST_FILTER = "Frost Filter";
    public static Image frostFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage){
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        LinkedList<double [][]> IMAf = createIntermediaryReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        double coefK = 4/(imageWindow.getN()*Math.pow(sU,2));
        for (int i = 0; i <originalImage.getNbrOfBands() ; i++) {
            for (int j = 0; j <originalImage.getNL() ; j++) {
                for (int k = 0; k <originalImage.getNC() ; k++) {
                    double z = localAverage(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(), bandTablesBordered.get(i),imageWindow);
                    double localVarianceSqr = localVariance(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),bandTablesBordered.get(i),
                            imageWindow,z);
                    double a = (coefK*localVarianceSqr)/Math.pow(z,2);
                    IMAf.get(i)[j][k] = frostFilterSubMethod(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),bandTablesBordered.get(i),imageWindow,a);
                }
            }
        }
        ToolsProvider.printTextureBeforeNormailzeDouble(IMAf,originalImage);
        double[] maxIMAf = findMaxValue(IMAf,originalImage);

        for (int i = 0; i <originalImage.getNbrOfBands() ; i++) {
            for (int j = 0; j <originalImage.getNL() ; j++) {
                for (int k = 0; k <originalImage.getNC() ; k++) {
                    double a = (IMAf.get(i)[j][k]/maxIMAf[i]);
                    IMA.get(i)[j][k] = (int)(255*(a));
                }
            }
        }

        Image filteredImage = ToolsProvider.reconstituteImage(IMA,originalImage);
        return filteredImage;
    }

    //method to obtain the gamma estimation
    public static double gammaEstimationFunction(int numberOfViews){
        double a = 1.7724538509 , t = 1;
        for (int i = 1; i <= numberOfViews ; i++)
            t*=2*i-1;
        return (a*t)/Math.pow(2,numberOfViews);
    }

    //method to calculate the noise standard deviation
    public static double  noiseStandardDeviationEstimation(int numberOfViews, int imageFactor){
        if(imageFactor == 0)
            return 1/Math.sqrt(numberOfViews);
        else if(imageFactor == 1)
            return Math.sqrt(0.273/numberOfViews);
        else if(imageFactor == 2){
            double a = Math.pow((ToolsProvider.factorial(numberOfViews-1)/gammaEstimationFunction(numberOfViews)),2);
            return Math.sqrt(numberOfViews*a-1);
        }
        else return -1;
    }

    //method to calculate the local average
    public static double localAverage(int x0, int y0, int[][]borderedBandTable, ImageWindow imageWindow){
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),
                imageWindow.getCenterX(),imageWindow.getCenterY(),borderedBandTable);
        double sum = 0;
        for (int i = 0; i < imageWindow.getNLmask(); i++) {
            for (int j = 0; j <imageWindow.getNCmask() ; j++) {
                sum+= windowedImage [i][j];
            }
        }
        return sum/Math.pow(imageWindow.getN(),2);
    }

    //method to calculate the local variance
    public static double localVariance(int x0, int y0, int[][]borderedBandTable, ImageWindow imageWindow, double localAverage){
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),
                imageWindow.getCenterX(),imageWindow.getCenterY(),borderedBandTable);
        double sum = 0, result = 0,
                temp = 1/(Math.pow(imageWindow.getNCmask(),2)-1);
        for (int i = 0; i <imageWindow.getNLmask() ; i++) {
            for (int j = 0; j <imageWindow.getNCmask() ; j++) {
                sum+= Math.pow((windowedImage[i][j]-localAverage),2);
            }
        }
        result = temp * sum;
        return result;
    }

    //method to calculate the image window size
    public static int imageWindowSize(Image image){
        if(image.getNC()==image.getNL())
            return image.getNL();
        else if (image.getNC()>image.getNL())
            return image.getNL();
        else return image.getNC();
    }

    public static Image determineAdaptiveFilterToUse(ImageWindow imageWindow, Image originalImage){
        Image image = null;
        if(imageWindow.getAdaptiveFilterMethod().equals(LEE_FILTER))
            image = leeFilter(imageWindow.getNumberOfViews(),imageWindow.getImageFactor(),imageWindow,originalImage);
        else if (imageWindow.getAdaptiveFilterMethod().equals(ALTERNATED_LEE_FILTER))
            image = alternatedLeeFilter(imageWindow.getNumberOfViews(),imageWindow.getImageFactor(),imageWindow,originalImage);
        else if(imageWindow.getAdaptiveFilterMethod().equals(MAP_FILTER))
            image = mapFilter(imageWindow.getNumberOfViews(),imageWindow.getImageFactor(),imageWindow,originalImage);
        else if(imageWindow.getAdaptiveFilterMethod().equals(GAMMA_FILTER))
            image = gammaFilter(imageWindow.getNumberOfViews(),imageWindow.getImageFactor(),imageWindow,originalImage);
        else if(imageWindow.getAdaptiveFilterMethod().equals(FROST_FILTER))
            image = frostFilter(imageWindow.getNumberOfViews(),imageWindow.getImageFactor(),imageWindow,originalImage);
        return image;
    }

    public static double frostFilterSubMethod(int x0, int y0, int[][]borderedBandTable, ImageWindow imageWindow, double a){
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,imageWindow.getNCmask(),imageWindow.getNLmask(),
                imageWindow.getCenterX(),imageWindow.getCenterY(),borderedBandTable);
        double sum = 0;
        for (int i = 0; i <imageWindow.getNLmask() ; i++) {
            for (int j = 0; j <imageWindow.getNCmask() ; j++) {
                double X = -a*(Math.abs(i-(y0-imageWindow.getCenterY()))+Math.abs(j-(x0-imageWindow.getCenterX())));
                sum += windowedImage[i][j]*Math.exp(X);
            }
        }
        return sum;
    }

    //method to create the bandTables receiving image data before its reconstitution
    public static LinkedList<double[][]> createIntermediaryReceivingBandTables(Image image){
        LinkedList<double[][]> IMA = new LinkedList<>();
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            double[][] temp = new double[image.getNL()][image.getNC()];
            IMA.add(temp);
        }
        return IMA;
    }

    //method to find the greater value in a band
    public static double [] findMaxValue(LinkedList<double[][]>bandTables, Image img){
        double [] maxValues = new double[bandTables.size()];
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

}
