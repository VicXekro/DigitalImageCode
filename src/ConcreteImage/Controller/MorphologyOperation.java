package ConcreteImage.Controller;

import ConcreteImage.Model.Image;
import Model.Mask;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Xekro on 6/4/2017.
 */
public class MorphologyOperation {

    //method to binarize an iamge using threshold
    public static Image binarization(Image image, int threshold1, int threshold2){
        for (int i = 0; i <image.getHeight() ; i++) {
            for (int j = 0; j <image.getWidth() ; j++) {
                Color c = new Color(image.getBufferedImage().getRGB(j,i));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                red = ((red>=threshold1 && red <= threshold2)?255:0);
                green = ((green>=threshold1 && green <= threshold2)?255:0);
                blue = ((blue>=threshold1 && blue <= threshold2)?255:0);
                Color newColor = new Color(red, green,blue);
                image.getBufferedImage().setRGB(j,i,newColor.getRGB());
            }
        }
        return image;
    }

    //method to perform the erosion of an image
    public static Image imageErosion(Mask mask, Image image)throws OutOfMemoryError{
        LinkedList<int[][]> ImaB1 = new LinkedList<>(), ImaB2 = new LinkedList<>();
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        fillIntermediaryBinaryImage(ImaB1,ImaB2,image,mask);
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            for (int j = 0; j <image.getHeight() ; j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    LinkedList<Integer> enabledCells = outPutEnableCells(k+mask.getCenterX(),j+mask.getCenterY() ,mask,ImaB1.get(i));
                    boolean b = true; int l =0;
                    while (b && l<enabledCells.size()){
                        if (enabledCells.get(l)==0)
                            b = false;
                        l++;
                    }
                    if (b) ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] = ImaB1.get(i)[j+mask.getCenterY()][k+mask.getCenterX()];
                    else ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                }
            }
        }
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    if (ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] == 1)
                        IMA.get(i)[j][k] = 255;
                    else IMA.get(i)[j][k] = 0;
                }
            }
        }

        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to perform the dilation of an image
    public static Image imageDilation(Mask mask, Image image)throws OutOfMemoryError{
        LinkedList<int[][]> ImaB1 = new LinkedList<>(), ImaB2 = new LinkedList<>();
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        fillIntermediaryBinaryImage(ImaB1,ImaB2,image,mask);
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            for (int j = 0; j <image.getHeight() ; j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    LinkedList<Integer> enabledCells = outPutEnableCells(k+mask.getCenterX(),j+mask.getCenterY() ,mask,ImaB1.get(i));
                    boolean b = false; int l =0;
                    while (!b && l<enabledCells.size()){
                        if (enabledCells.get(l)==1)
                            b = true;
                        l++;
                    }
                    if (b) ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] = 1;
                    else ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] = ImaB1.get(i)[j+mask.getCenterY()][k+mask.getCenterX()];
                }
            }
        }
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                for (int k = 0; k <image.getWidth() ; k++) {
                    if (ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] == 1)
                        IMA.get(i)[j][k] = 255;
                    else IMA.get(i)[j][k] = 0;
                }
            }
        }

        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to perform the opening of an image
    public static Image imageOpening(Mask mask, Image image)throws OutOfMemoryError{
        Image openedImage = image;
        for (int i = 0; i < mask.getOrder() ; i++)
            openedImage = imageErosion(mask,openedImage);
        for (int i = 0; i < mask.getOrder(); i++)
            openedImage = imageDilation(mask,openedImage);
        return openedImage;
    }

    //method to perform the closure of an image
    public static Image imageClosure(Mask mask, Image image)throws OutOfMemoryError{
        Image closedImage = image;
        for (int i = 0; i < mask.getOrder(); i++)
            closedImage = imageDilation(mask,image);
        for (int i = 0; i < mask.getOrder(); i++)
            closedImage = imageErosion(mask,image);
        return closedImage;
    }

    //method to perform the white top hat of an image
    public static Image imageWhiteTopHat(Mask mask, Image image)throws OutOfMemoryError{
        LinkedList<int[][]> binarized = new LinkedList<>();
        if(!mask.isThreshHoldDefine())
            binarized = binarizeImage(image);
        else
            binarized = binarizationProcess(mask.getThreshold1(),mask.getThreshold2(),image);
        Image closuredImage = imageClosure(mask,image);
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                IMA.get(0)[j][k] = binarized.get(0)[j][k]-closuredImage.getBandTable().get(0)[j][k] ;
                IMA.get(1)[j][k] = binarized.get(1)[j][k]-closuredImage.getBandTable().get(1)[j][k] ;
                IMA.get(2)[j][k] = binarized.get(2)[j][k]-closuredImage.getBandTable().get(2)[j][k] ;
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to perform the black top hat of an image
    public static Image imageBlackTopHat(Mask mask, Image image)throws OutOfMemoryError{
        Image openedImage = imageOpening(mask,image);
        LinkedList<int[][]> binarized = new LinkedList<>();
        if(!mask.isThreshHoldDefine())
            binarized = binarizeImage(image);
        else
            binarized = binarizationProcess(mask.getThreshold1(),mask.getThreshold2(),image);
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                IMA.get(0)[j][k] = binarized.get(0)[j][k]-openedImage.getBandTable().get(0)[j][k] ;
                IMA.get(1)[j][k] = binarized.get(1)[j][k]-openedImage.getBandTable().get(1)[j][k] ;
                IMA.get(2)[j][k] = binarized.get(2)[j][k]-openedImage.getBandTable().get(2)[j][k] ;
            }
        }
        ToolsProvider.modifyColorFromArray(IMA,image);
        return image;
    }

    //method to perform the skeletisation of an image
    public static javafx.scene.image.Image imageSkeletisation(Mask mask, Image image)throws OutOfMemoryError{
        int[] iter = new int[image.getNumberOfBands()];
        boolean[] stable = new boolean[image.getNumberOfBands()];
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            iter[i] = 1;
            stable[i] = false;
        }
        LinkedList<int[][]> ImaB1 = new LinkedList<>(), ImaB2 = new LinkedList<>();
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        fillIntermediaryBinaryImage(ImaB1,ImaB2,image,mask);
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            while (!stable[i]){
                stable[i] = true;
                iter[i]++;

                //Convolution with the East Mask
                for (int j = 0; j <image.getHeight() ; j++) {
                    for (int k = 0; k <image.getWidth() ; k++) {
                        if(ImaB1.get(i)[j][k] == 1 || ImaB1.get(i)[j+1][k] == 1 || ImaB1.get(i)[j+2][k] == 1 || !(binaryToBoolean(ImaB1.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB1.get(i)[j][k+2]) &&  binaryToBoolean(ImaB1.get(i)[j+1][k+2]) &&  binaryToBoolean(ImaB1.get(i)[j+2][k+2])))
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = ImaB1.get(i)[j+(3/2)][k+(3/2)];
                        else{
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i]= false;
                        }
                    }
                }

                //Convolution with the South-East Mask
                for (int j = 0; j <image.getHeight() ; j++) {
                    for (int k = 0; k <image.getWidth() ; k++) {
                        if(ImaB2.get(i)[j+1][k] == 1 || ImaB2.get(i)[j][k] == 1 || ImaB2.get(i)[j][k+1] == 1 || !(binaryToBoolean(ImaB2.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB2.get(i)[j+1][k+2]) && binaryToBoolean(ImaB2.get(i)[j+2][k+2]) && binaryToBoolean(ImaB2.get(i)[j+2][k+1])) )
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = ImaB2.get(i)[j+(3/2)][k+(3/2)];
                        else{
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i]= false;
                        }
                    }
                }

                //Convolution with the South Mask
                for (int j = 0; j <image.getHeight() ; j++) {
                    for (int k = 0; k <image.getWidth() ; k++) {
                        if(ImaB1.get(i)[j][k] == 1 || ImaB1.get(i)[j][k+1] == 1 || ImaB1.get(i)[j][k+2] == 1 || !(binaryToBoolean(ImaB1.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB1.get(i)[j+2][k]) && binaryToBoolean(ImaB1.get(i)[j+2][k+1]) && binaryToBoolean(ImaB1.get(i)[j+2][k+2])) )
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = ImaB1.get(i)[j+(3/2)][k+(3/2)];
                        else {
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i] = false;
                        }
                    }
                }

                //Convolution with the South-West Mask
                for (int j = 0; j <image.getHeight() ; j++) {
                    for (int k = 0; k <image.getWidth() ; k++) {
                        if(ImaB2.get(i)[j+1][k+2] == 1 || ImaB2.get(i)[j][k+2] == 1 || ImaB2.get(i)[j][k+1] == 1 || !(binaryToBoolean(ImaB2.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB2.get(i)[j+1][k]) && binaryToBoolean(ImaB2.get(i)[j+2][k]) && binaryToBoolean(ImaB2.get(i)[j+2][k+1])))
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = ImaB2.get(i)[j+(3/2)][k+(3/2)];
                        else{
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i] = false;
                        }
                    }
                }

                //Convolution with West Mask
                for (int j = 0; j <image.getHeight() ; j++) {
                    for (int k = 0; k <image.getWidth() ; k++) {
                        if(ImaB1.get(i)[j][k+2] == 1 || ImaB1.get(i)[j+1][k+2] == 1 || ImaB1.get(i)[j+2][k+2] == 1 || !(binaryToBoolean(ImaB1.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB1.get(i)[j][k]) && binaryToBoolean(ImaB1.get(i)[j+1][k]) && binaryToBoolean(ImaB1.get(i)[j+2][k])))
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = ImaB1.get(i)[j+(3/2)][k+(3/2)];
                        else{
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i] = false;
                        }
                    }
                }

                //Convolution with North-West Mask
                for (int j = 0; j <image.getHeight() ; j++) {
                    for (int k = 0; k <image.getWidth() ; k++) {
                        if(ImaB2.get(i)[j+1][k+2] == 1 || ImaB2.get(i)[j+2][k+2] == 1 || ImaB2.get(i)[j+2][k+1] == 1 || !(binaryToBoolean(ImaB2.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB2.get(i)[j+1][k]) && binaryToBoolean(ImaB2.get(i)[j][k]) && binaryToBoolean(ImaB2.get(i)[j][k+1])))
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = ImaB2.get(i)[j+(3/2)][k+(3/2)];
                        else{
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i] = false;
                        }
                    }
                }

                //Convolution with North Mask
                for (int j = 0; j <image.getHeight() ; j++) {
                    for (int k = 0; k <image.getWidth() ; k++) {
                        if(ImaB1.get(i)[j+2][k] == 1 || ImaB1.get(i)[j+2][k+1] == 1 || ImaB1.get(i)[j+2][k+2] == 1 || !(binaryToBoolean(ImaB1.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB1.get(i)[j][k]) && binaryToBoolean(ImaB1.get(i)[j][k+1]) && binaryToBoolean(ImaB1.get(i)[j][k+2])))
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = ImaB1.get(i)[j+(3/2)][k+(3/2)];
                        else{
                            ImaB2.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i] = false;
                        }
                    }
                }

                //Convolution with North-East Mask
                for (int j = 0; j < image.getHeight(); j++) {
                    for (int k = 0; k < image.getWidth(); k++) {
                        if(ImaB2.get(i)[j+1][k] == 1 || ImaB2.get(i)[j+2][k] == 1 || ImaB2.get(i)[j+2][k+1] == 1 || !(binaryToBoolean(ImaB2.get(i)[j+(3/2)][k+(3/2)])
                                && binaryToBoolean(ImaB2.get(i)[j][k+1]) && binaryToBoolean(ImaB2.get(i)[j][k+2]) && binaryToBoolean(ImaB2.get(i)[j+1][k+2])))
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = ImaB2.get(i)[j+(3/2)][k+(3/2)];
                        else{
                            ImaB1.get(i)[j+(3/2)][k+(3/2)] = 0;
                            stable[i] = false;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < image.getNumberOfBands(); i++) {
            for (int j = 0; j <image.getHeight() ; j++) {
                for (int k = 0; k < image.getWidth(); k++) {
                    if(ImaB1.get(i)[j+(3/2)][k+(3/2)] == 1)
                        IMA.get(i)[j][k] = 255;
                    else
                        IMA.get(i)[j][k] = 0;
                }
            }
        }

        ToolsProvider.modifyColorFromArray(IMA,image);
        javafx.scene.image.Image skeletiseImage = SwingFXUtils.toFXImage(image.getBufferedImage(),null);
        return skeletiseImage;
    }

    public static void fillIntermediaryBinaryImage(LinkedList<int[][]>ImaB1, LinkedList<int[][]> ImaB2, Image image, Mask mask){
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            int [][] temp = new int[image.getHeight()+mask.getCenterY()+(mask.getNLmask()-mask.getCenterY()-1)][image.getWidth()+mask.getCenterX()+(mask.getNCmask()-mask.getCenterX()-1)];
            int [][] temp2 = new int[image.getHeight()+mask.getCenterY()+(mask.getNLmask()-mask.getCenterY()-1)][image.getWidth()+mask.getCenterX()+(mask.getNCmask()-mask.getCenterX()-1)];
            ImaB1.add(temp);
            ImaB2.add(temp2);
        }

        for (int j = 0; j <image.getHeight(); j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                Color c = new Color(image.getBufferedImage().getRGB(k,j));
                int red = c.getRed(), green = c.getGreen(), blue = c.getBlue();
                ImaB2.get(0)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                ImaB2.get(1)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                ImaB2.get(2)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                if(red>0)
                    ImaB1.get(0)[j+mask.getCenterY()][k+mask.getCenterX()] = 1;
                else
                    ImaB1.get(0)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                if(green>0)
                    ImaB1.get(1)[j+mask.getCenterY()][k+mask.getCenterX()] = 1;
                else
                    ImaB1.get(1)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                if(blue>0)
                    ImaB1.get(2)[j+mask.getCenterY()][k+mask.getCenterX()] = 1;
                else
                    ImaB1.get(2)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
            }
        }

        ToolsProvider.determineBorderFillingMethod(ImaB1,mask.getBorderFeeling(),image,mask.getCenterX(),mask.getCenterY());
        ToolsProvider.determineBorderFillingMethod(ImaB2,mask.getBorderFeeling(),image,mask.getCenterX(),mask.getCenterY());
    }

    public static LinkedList<Integer> outPutEnableCells(int x0, int y0, Mask mask, int[][] bandTableBordered){
        LinkedList<Integer> enableCellsValue = new LinkedList<>();
        int[][] windowedImage = ToolsProvider.extractImageWindow(x0,y0,mask.getNCmask(),mask.getNLmask(),mask.getCenterX(),mask.getCenterY(),bandTableBordered);
        for (int i = 0; i <mask.getNLmask() ; i++) {
            for (int j = 0; j <mask.getNCmask() ; j++) {
                if(mask.getMask()[i][j] == 1)
                    enableCellsValue.add(windowedImage[i][j]);
            }
        }
        return enableCellsValue;
    }

    public static boolean binaryToBoolean(int a){
        if(a==1)return true;
        else return false;
    }

    public static LinkedList<int[][]> binarizeImage(Image image){
        LinkedList<int[][]> binarised = new LinkedList<>();
        for (int i = 0; i <image.getNumberOfBands() ; i++) {
            int[][] temp = new int[image.getHeight()][image.getWidth()];
            binarised.add(temp);
        }
        for (int j = 0; j <image.getHeight() ; j++) {
            for (int k = 0; k <image.getWidth() ; k++) {
                Color color = new Color(image.getBufferedImage().getRGB(k,j));
                int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
                red = ((red>0)?255:0);
                green = ((green>0)?255:0);
                blue = ((blue>0)?255:0);
                binarised.get(0)[j][k] = red;
                binarised.get(1)[j][k] = green;
                binarised.get(2)[j][k] = blue;
            }
        }
        return binarised;
    }

    //method to binarize an image in function of a thresh hold
    public static LinkedList<int[][]> binarizationProcess(int threshold1, int threshold2, Image image) {
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int j = 0; j < image.getHeight(); j++) {
            for (int k = 0; k < image.getWidth(); k++) {
                Color color = new Color(image.getBufferedImage().getRGB(k,j));
                int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
                red = ((red>=threshold1 && red<=threshold2)?255:0);
                green = ((green>=threshold1 && green<=threshold2)?255:0);
                blue = ((blue>=threshold1 && blue<=threshold2)?255:0);
                IMA.get(0)[j][k] = red;
                IMA.get(1)[j][k] = green;
                IMA.get(2)[j][k] = blue;
            }
        }
        return IMA;
    }

}
