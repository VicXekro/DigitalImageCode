package ConcreteImage.Model;

import ConcreteImage.Controller.ToolsProvider;
import Model.ImageWindow;

import java.util.LinkedList;

/**
 * Created by Xekro on 6/8/2017.
 */
public class AdaptiveFilterMethods {

    public static final String LEE_FILTER = "Lee Filter";
    public static Image leeFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage) throws OutOfMemoryError{
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int j = 0; j <originalImage.getHeight() ; j++) {
            for (int k = 0; k <originalImage.getWidth() ; k++) {
                int[][] windowedImage0 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(0));
                int[][] windowedImage1 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(1));
                int[][] windowedImage2 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(2));
                double z0 = localAverage(imageWindow,windowedImage0);
                double z1 = localAverage(imageWindow,windowedImage1);
                double z2 = localAverage(imageWindow,windowedImage2);
                double localVarianceSqr0 = localVariance(imageWindow,z0,windowedImage0);
                double localVarianceSqr1 = localVariance(imageWindow,z1,windowedImage1);
                double localVarianceSqr2 = localVariance(imageWindow,z2,windowedImage2);
                double y0 = z0;
                double y1 = z1;
                double y2 = z2;
                double sYsqr0 = (localVarianceSqr0 - Math.pow(sU,2))/(Math.pow(sU,2)+1);
                double sYsqr1 = (localVarianceSqr1 - Math.pow(sU,2))/(Math.pow(sU,2)+1);
                double sYsqr2 = (localVarianceSqr2 - Math.pow(sU,2))/(Math.pow(sU,2)+1);
                sYsqr0 = ((sYsqr0<0)?0:sYsqr0);
                sYsqr1 = ((sYsqr1<0)?0:sYsqr1);
                sYsqr2 = ((sYsqr2<0)?0:sYsqr2);
                double b0 = sYsqr0/(Math.pow(z0,2)*Math.pow(sU,2)+sYsqr0);
                double b1 = sYsqr1/(Math.pow(z1,2)*Math.pow(sU,2)+sYsqr1);
                double b2 = sYsqr2/(Math.pow(z2,2)*Math.pow(sU,2)+sYsqr2);
                IMA.get(0)[j][k] = (int)(z0 + b0*(originalImage.getBandTable().get(0)[j][k]-z0));
                IMA.get(1)[j][k] = (int)(z1 + b1*(originalImage.getBandTable().get(1)[j][k]-z1));
                IMA.get(2)[j][k] = (int)(z2 + b2*(originalImage.getBandTable().get(2)[j][k]-z2));
            }
        }

        ToolsProvider.modifyColorFromArray(IMA,originalImage);
        return originalImage;
    }

    public static final String ALTERNATED_LEE_FILTER = "Alternated Lee Filter";
    public static Image alternatedLeeFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage) throws OutOfMemoryError{
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int j = 0; j < originalImage.getHeight(); j++) {
            for (int k = 0; k < originalImage.getWidth(); k++) {
                int[][] windowedImage0 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(0));
                int[][] windowedImage1 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(1));
                int[][] windowedImage2 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(2));
                double z0 = localAverage(imageWindow,windowedImage0);
                double z1 = localAverage(imageWindow,windowedImage1);
                double z2 = localAverage(imageWindow,windowedImage2);
                double localVarianceSqr0 = localVariance(imageWindow,z0,windowedImage0);
                double localVarianceSqr1 = localVariance(imageWindow,z1,windowedImage1);
                double localVarianceSqr2 = localVariance(imageWindow,z2,windowedImage2);
                double sY0 = ((z0!=0)?(Math.sqrt(localVarianceSqr0)/z0):1);
                double sY1 = ((z1!=0)?(Math.sqrt(localVarianceSqr1)/z1):1);
                double sY2 = ((z2!=0)?(Math.sqrt(localVarianceSqr2)/z2):1);
                if(sY0<sU)
                    IMA.get(0)[j][k] = (int)(z0);
                else{
                    double a = Math.pow(sY0,2)+Math.pow(sU,4);
                    double b = originalImage.getBandTable().get(0)[j][k]*
                            (Math.pow(sY0,2)-Math.pow(sU,2))+z0*(Math.pow(sU,2)+Math.pow(sU,4));
                    b = b/a;
                    IMA.get(0)[j][k] = (int)(b);
                }
                if(sY1<sU)
                    IMA.get(1)[j][k] = (int)(z1);
                else{
                    double a = Math.pow(sY1,2)+Math.pow(sU,4);
                    double b = originalImage.getBandTable().get(1)[j][k]*
                            (Math.pow(sY1,2)-Math.pow(sU,2))+z1*(Math.pow(sU,2)+Math.pow(sU,4));
                    b = b/a;
                    IMA.get(1)[j][k] = (int)(b);
                }
                if(sY2<sU)
                    IMA.get(2)[j][k] = (int)(z2);
                else{
                    double a = Math.pow(sY2,2)+Math.pow(sU,4);
                    double b = originalImage.getBandTable().get(2)[j][k]*
                            (Math.pow(sY2,2)-Math.pow(sU,2))+z2*(Math.pow(sU,2)+Math.pow(sU,4));
                    b = b/a;
                    IMA.get(2)[j][k] = (int)(b);
                }
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,originalImage);
        return originalImage;
    }

    public static final String MAP_FILTER = "Map Filter";
    public static Image mapFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage)throws OutOfMemoryError{
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int j = 0; j <originalImage.getHeight() ; j++) {
            for (int k = 0; k <originalImage.getWidth() ; k++) {
                int[][] windowedImage0 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(0));
                int[][] windowedImage1 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(1));
                int[][] windowedImage2 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(2));
                double z0 = localAverage(imageWindow,windowedImage0);
                double z1 = localAverage(imageWindow,windowedImage1);
                double z2 = localAverage(imageWindow,windowedImage2);
                double localVarianceSqr0 = localVariance(imageWindow,z0,windowedImage0);
                double localVarianceSqr1 = localVariance(imageWindow,z1,windowedImage1);
                double localVarianceSqr2 = localVariance(imageWindow,z2,windowedImage2);
                double sY0 = ((z0!=0)?(Math.sqrt(localVarianceSqr0)/z0):0);
                double sY1 = ((z1!=0)?(Math.sqrt(localVarianceSqr1)/z1):0);
                double sY2 = ((z2!=0)?(Math.sqrt(localVarianceSqr2)/z2):0);
                if(sY0<sU)
                    IMA.get(0)[j][k] = (int)(z0);
                else{
                    double a = Math.pow(sY0,2)*(1+Math.pow(sU,2));
                    double b = originalImage.getBandTable().get(0)[j][k]*(Math.pow(sY0,2)-Math.pow(sU,2))+
                            z0*Math.pow(sU,2)*(1+Math.pow(sY0,2));
                    IMA.get(0)[j][k] = (int)(b/a);
                }
                if(sY1<sU)
                    IMA.get(1)[j][k] = (int)(z1);
                else{
                    double a = Math.pow(sY1,2)*(1+Math.pow(sU,2));
                    double b = originalImage.getBandTable().get(1)[j][k]*(Math.pow(sY1,2)-Math.pow(sU,2))+
                            z1*Math.pow(sU,2)*(1+Math.pow(sY1,2));
                    IMA.get(1)[j][k] = (int)(b/a);
                }
                if(sY2<sU)
                    IMA.get(2)[j][k] = (int)(z2);
                else{
                    double a = Math.pow(sY2,2)*(1+Math.pow(sU,2));
                    double b = originalImage.getBandTable().get(2)[j][k]*(Math.pow(sY2,2)-Math.pow(sU,2))+
                            z2*Math.pow(sU,2)*(1+Math.pow(sY2,2));
                    IMA.get(2)[j][k] = (int)(b/a);
                }
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,originalImage);
        return originalImage;
    }

    public static final String GAMMA_FILTER = "Gamma Filter";
    public static Image gammaFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage)throws OutOfMemoryError{
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        for (int j = 0; j <originalImage.getHeight() ; j++) {
            for (int k = 0; k <originalImage.getWidth() ; k++) {
                int[][] windowedImage0 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(0));
                int[][] windowedImage1 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(1));
                int[][] windowedImage2 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(2));
                double z0 = localAverage(imageWindow,windowedImage0);
                double z1 = localAverage(imageWindow,windowedImage1);
                double z2 = localAverage(imageWindow,windowedImage2);
                double localVarianceSqr0 = localVariance(imageWindow,z0,windowedImage0);
                double localVarianceSqr1 = localVariance(imageWindow,z1,windowedImage1);
                double localVarianceSqr2 = localVariance(imageWindow,z2,windowedImage2);
                double sY0 = ((z0!=0)?(Math.sqrt(localVarianceSqr0)/z0):0);
                double sY1 = ((z1!=0)?(Math.sqrt(localVarianceSqr1)/z1):0);
                double sY2 = ((z2!=0)?(Math.sqrt(localVarianceSqr2)/z2):0);
                if((Math.pow(sY0,2)-Math.pow(sU,2))!=0){
                    double alpha = (1+Math.pow(sU,2))/(Math.pow(sY0,2)-Math.pow(sU,2));
                    double t1 = z0*(alpha-numberOfViews-1);
                    double t2 = Math.pow(t1,2)+(4*alpha*numberOfViews*originalImage.getBandTable().get(0)[j][k]*z0);
                    double t = t1+Math.sqrt(t2);
                    IMA.get(0)[j][k] = (int)(t/(2*alpha));
                }
                if((Math.pow(sY1,2)-Math.pow(sU,2))!=0){
                    double alpha = (1+Math.pow(sU,2))/(Math.pow(sY1,2)-Math.pow(sU,2));
                    double t1 = z1*(alpha-numberOfViews-1);
                    double t2 = Math.pow(t1,2)+(4*alpha*numberOfViews*originalImage.getBandTable().get(1)[j][k]*z1);
                    double t = t1+Math.sqrt(t2);
                    IMA.get(1)[j][k] = (int)(t/(2*alpha));
                }
                if((Math.pow(sY2,2)-Math.pow(sU,2))!=0){
                    double alpha = (1+Math.pow(sU,2))/(Math.pow(sY2,2)-Math.pow(sU,2));
                    double t1 = z2*(alpha-numberOfViews-1);
                    double t2 = Math.pow(t1,2)+(4*alpha*numberOfViews*originalImage.getBandTable().get(2)[j][k]*z2);
                    double t = t1+Math.sqrt(t2);
                    IMA.get(2)[j][k] = (int)(t/(2*alpha));
                }
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,originalImage);
        return originalImage;
    }

    public static final String FROST_FILTER = "Frost Filter";
    public static Image frostFilter(int numberOfViews, int imageFactor, ImageWindow imageWindow, Image originalImage)throws OutOfMemoryError{
        LinkedList<int [][]> bandTablesBordered = ToolsProvider.createBorderOnImage(imageWindow.getNCmask(),imageWindow.getNLmask(),imageWindow.getCenterX(),
                imageWindow.getCenterY(), originalImage);
        ToolsProvider.determineBorderFillingMethod(bandTablesBordered,imageWindow.getBorderFeeling(),originalImage,imageWindow.getCenterX(),
                imageWindow.getCenterY());
        LinkedList<int [][]> IMA = ToolsProvider.createReceivingBandTables(originalImage);
        LinkedList<double [][]> IMAf = createIntermediaryReceivingBandTables(originalImage);
        double sU = noiseStandardDeviationEstimation(numberOfViews,imageFactor);
        double coefK = 4/(imageWindow.getN()*Math.pow(sU,2));
        for (int j = 0; j <originalImage.getHeight() ; j++) {
            for (int k = 0; k <originalImage.getWidth() ; k++) {
                int[][] windowedImage0 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(0));
                int[][] windowedImage1 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(1));
                int[][] windowedImage2 = ToolsProvider.extractImageWindow(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow.getNCmask(),imageWindow.getNLmask(),
                        imageWindow.getCenterX(),imageWindow.getCenterY(),bandTablesBordered.get(2));
                double z0 = localAverage(imageWindow,windowedImage0);
                double z1 = localAverage(imageWindow,windowedImage1);
                double z2 = localAverage(imageWindow,windowedImage2);
                double localVarianceSqr0 = localVariance(imageWindow,z0,windowedImage0);
                double localVarianceSqr1 = localVariance(imageWindow,z1,windowedImage1);
                double localVarianceSqr2 = localVariance(imageWindow,z2,windowedImage2);
                double a0 = (coefK*localVarianceSqr0)/Math.pow(z0,2);
                double a1 = (coefK*localVarianceSqr1)/Math.pow(z1,2);
                double a2 = (coefK*localVarianceSqr2)/Math.pow(z2,2);
                IMAf.get(0)[j][k] = frostFilterSubMethod(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow,a0,windowedImage0);
                IMAf.get(1)[j][k] = frostFilterSubMethod(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow,a1,windowedImage1);
                IMAf.get(2)[j][k] = frostFilterSubMethod(k+imageWindow.getCenterX(), j+imageWindow.getCenterY(),imageWindow,a2,windowedImage2);
            }
        }

        double[] maxIMAf = findMaxValue(IMAf,originalImage);

        for (int j = 0; j <originalImage.getHeight() ; j++) {
            for (int k = 0; k <originalImage.getWidth() ; k++) {
                double a0 = (IMAf.get(0)[j][k]/maxIMAf[0]);
                double a1 = (IMAf.get(1)[j][k]/maxIMAf[1]);
                double a2 = (IMAf.get(2)[j][k]/maxIMAf[2]);
                IMA.get(0)[j][k] = (int)(255*(a0));
                IMA.get(1)[j][k] = (int)(255*(a1));
                IMA.get(2)[j][k] = (int)(255*(a2));
            }
        }

        ToolsProvider.modifyColorFromArray(IMA,originalImage);
        return originalImage;
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
            double a = Math.pow((Control.ToolsProvider.factorial(numberOfViews-1)/gammaEstimationFunction(numberOfViews)),2);
            return Math.sqrt(numberOfViews*a-1);
        }
        else return -1;
    }

    //method to calculate the local average
    public static double localAverage(ImageWindow imageWindow, int[][] windowedImage){
        double sum = 0;
        for (int i = 0; i < imageWindow.getNLmask(); i++) {
            for (int j = 0; j <imageWindow.getNCmask() ; j++) {
                sum+= windowedImage [i][j];
            }
        }
        return sum/Math.pow(imageWindow.getN(),2);
    }

    //method to calculate the local variance
    public static double localVariance(ImageWindow imageWindow, double localAverage, int[][] windowedImage){
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
        if(image.getHeight()==image.getWidth())
            return image.getHeight();
        else if (image.getWidth()>image.getHeight())
            return image.getHeight();
        else return image.getWidth();
    }

    public static Image determineAdaptiveFilterToUse(ImageWindow imageWindow, Image originalImage) throws OutOfMemoryError{
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

    public static double frostFilterSubMethod(int x0, int y0, ImageWindow imageWindow, double a, int[][] windowedImage){
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
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            double[][] temp = new double[image.getHeight()][image.getWidth()];
            IMA.add(temp);
        }
        return IMA;
    }

    //method to find the greater value in a band
    public static double [] findMaxValue(LinkedList<double[][]>bandTables, Image img){
        double [] maxValues = new double[bandTables.size()];
        maxValues[0] = bandTables.get(0)[0][0];
        maxValues[1] = bandTables.get(1)[0][0];
        maxValues[2] = bandTables.get(2)[0][0];
        for (int j = 0; j <img.getHeight() ; j++) {
            for (int k = 0; k <img.getWidth() ; k++) {
                if(bandTables.get(0)[j][k]>maxValues[0])
                    maxValues[0] = bandTables.get(0)[j][k];
                if(bandTables.get(1)[j][k]>maxValues[1])
                    maxValues[1] = bandTables.get(1)[j][k];
                if(bandTables.get(2)[j][k]>maxValues[2])
                    maxValues[2] = bandTables.get(2)[j][k];
            }
        }
        return  maxValues;
    }
}
