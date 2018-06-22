package Model;

import Control.ToolsProvider;

import java.util.LinkedList;

/**
 * Created by Xekro on 5/14/2017.
 */
public class Morphology {

    public static final String BINARIZATION = "Binarization";
    public static final String EROSION = "Erosion";
    public static final String DILATION = "Dilation";
    public static final String OPENING = "Opening";
    public static final String CLOSURE = "Closure";
    public static final String WHITETOPHAT = "White Top Hat";
    public static final String BLACKTOPHAT = "Black Top Hat";
    public static final String SKELETISATION = "Skeletisation";

    private int[][] BNorth = {{1,1,1},{0,1,0},{0,0,0}}, BNorth_East = {{0,1,1},{0,1,1},{0,0,0}}, BEast= {{0,0,1},{0,1,1},{0,0,1}};
    private int[][] BSouth_East = {{0,0,0},{0,1,1},{0,1,1}}, BSouth = {{0,0,0},{0,1,0},{1,1,1}}, BSouth_West = {{0,0,0},{1,1,0},{1,1,0}};
    private int[][] BWest = {{1,0,0},{1,1,0},{1,0,0}}, BNorth_West = {{1,1,0},{1,1,0},{0,0,0}};

    //method to binarize an image
    public static Image binarization(int threshold1, int threshold2, Image image){
        LinkedList<int[][]> IMA = binarizationProcess(threshold1,threshold2,image);
        Image binarizedImage = ToolsProvider.reconstituteImage(IMA,image);
        return binarizedImage;
    }

    //method to perform the erosion of an image
    public static Image imageErosion(Mask mask,Image image){
        LinkedList<int[][]> ImaB1 = new LinkedList<>(), ImaB2 = new LinkedList<>();
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        fillIntermediaryBinaryImage(ImaB1,ImaB2,image,mask);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
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
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j < image.getNL(); j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    if (ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] == 1)
                        IMA.get(i)[j][k] = 255;
                    else IMA.get(i)[j][k] = 0;
                }
            }
        }

        Image erodedImage = ToolsProvider.reconstituteImage(IMA,image);
        return erodedImage;
    }

    //method to perform the dilation of an image
    public static Image imageDilation(Mask mask, Image image){
        LinkedList<int[][]> ImaB1 = new LinkedList<>(), ImaB2 = new LinkedList<>();
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        fillIntermediaryBinaryImage(ImaB1,ImaB2,image,mask);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
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
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j < image.getNL(); j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    if (ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] == 1)
                        IMA.get(i)[j][k] = 255;
                    else IMA.get(i)[j][k] = 0;
                }
            }
        }

        Image dilatedImage = ToolsProvider.reconstituteImage(IMA,image);
        return dilatedImage;
    }

    //method to perform the opening of an image
    public static Image imageOpening(Mask mask, Image image){
        Image openedImage = image;
        for (int i = 0; i < mask.getOrder() ; i++)
            openedImage = imageErosion(mask,openedImage);
        for (int i = 0; i < mask.getOrder(); i++)
            openedImage = imageDilation(mask,openedImage);
        return openedImage;
    }

    //method to perform the closure of an image
    public static Image imageClosure(Mask mask, Image image){
        Image closedImage = image;
        for (int i = 0; i < mask.getOrder(); i++)
            closedImage = imageDilation(mask,image);
        for (int i = 0; i < mask.getOrder(); i++)
            closedImage = imageErosion(mask,image);
        return closedImage;
    }

    //method to perform the white top hat of an image
    public static Image imageWhiteTopHat(Mask mask, Image image){
        LinkedList<int[][]> binarized = new LinkedList<>();
        if(!mask.isThreshHoldDefine())
            binarized = binarizeImage(image);
        else
            binarized = binarizationProcess(mask.getThreshold1(),mask.getThreshold2(),image);
        Image closuredImage = imageClosure(mask,image);
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = binarized.get(i)[j][k]-closuredImage.getBandLinkedList().get(i).getBandTable()[j][k] ;
                }
            }
        }

        Image whiteTopedImage = ToolsProvider.reconstituteImage(IMA,image);
        return whiteTopedImage;
    }

    //method to perform the black top hat of an image
    public static Image imageBlackTopHat(Mask mask, Image image){
        Image openedImage = imageOpening(mask,image);
        LinkedList<int[][]> binarized = new LinkedList<>();
        if(!mask.isThreshHoldDefine())
            binarized = binarizeImage(image);
        else
            binarized = binarizationProcess(mask.getThreshold1(),mask.getThreshold2(),image);
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    IMA.get(i)[j][k] = binarized.get(i)[j][k]-openedImage.getBandLinkedList().get(i).getBandTable()[j][k] ;
                }
            }
        }
        Image blackTopedImage = ToolsProvider.reconstituteImage(IMA,image);
        return blackTopedImage;
    }

    //method to perform the skeletisation of an image
    public static Image imageSkeletisation(Mask mask, Image image){
        int[] iter = new int[image.getNbrOfBands()];
        boolean[] stable = new boolean[image.getNbrOfBands()];
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            iter[i] = 1;
            stable[i] = false;
        }
        LinkedList<int[][]> ImaB1 = new LinkedList<>(), ImaB2 = new LinkedList<>();
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        fillIntermediaryBinaryImage(ImaB1,ImaB2,image,mask);
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            while (!stable[i]){
                stable[i] = true;
                iter[i]++;

                //Convolution with the East Mask
                for (int j = 0; j <image.getNL() ; j++) {
                    for (int k = 0; k <image.getNC() ; k++) {
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
                for (int j = 0; j <image.getNL() ; j++) {
                    for (int k = 0; k <image.getNC() ; k++) {
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
                for (int j = 0; j <image.getNL() ; j++) {
                    for (int k = 0; k <image.getNC() ; k++) {
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
                for (int j = 0; j <image.getNL() ; j++) {
                    for (int k = 0; k <image.getNC() ; k++) {
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
                for (int j = 0; j <image.getNL() ; j++) {
                    for (int k = 0; k <image.getNC() ; k++) {
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
                for (int j = 0; j <image.getNL() ; j++) {
                    for (int k = 0; k <image.getNC() ; k++) {
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
                for (int j = 0; j <image.getNL() ; j++) {
                    for (int k = 0; k <image.getNC() ; k++) {
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
                for (int j = 0; j < image.getNL(); j++) {
                    for (int k = 0; k < image.getNC(); k++) {
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

        for (int i = 0; i < image.getNbrOfBands(); i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k < image.getNC(); k++) {
                    if(ImaB1.get(i)[j+(3/2)][k+(3/2)] == 1)
                        IMA.get(i)[j][k] = 255;
                    else
                        IMA.get(i)[j][k] = 0;
                }
            }
        }

        Image skeletonImage = ToolsProvider.reconstituteImage(IMA, image);
        return skeletonImage;
    }

    public static void fillIntermediaryBinaryImage(LinkedList<int[][]>ImaB1, LinkedList<int[][]> ImaB2, Image image, Mask mask){
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            int [][] temp = new int[image.getNL()+mask.getCenterY()+(mask.getNLmask()-mask.getCenterY()-1)][image.getNC()+mask.getCenterX()+(mask.getNCmask()-mask.getCenterX()-1)];
            int [][] temp2 = new int[image.getNL()+mask.getCenterY()+(mask.getNLmask()-mask.getCenterY()-1)][image.getNC()+mask.getCenterX()+(mask.getNCmask()-mask.getCenterX()-1)];
            ImaB1.add(temp);
            ImaB2.add(temp2);
        }

        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL(); j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    ImaB2.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                    if(image.getBandLinkedList().get(i).getBandTable()[j][k]>0)
                        ImaB1.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] = 1;
                    else
                        ImaB1.get(i)[j+mask.getCenterY()][k+mask.getCenterX()] = 0;
                }
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

    public static LinkedList<int[][]> binarizeImage(Image image){
        LinkedList<int[][]> binarised = new LinkedList<>();
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            int[][] temp = new int[image.getNL()][image.getNC()];
            binarised.add(temp);
        }
        for (int i = 0; i <image.getNbrOfBands() ; i++) {
            for (int j = 0; j <image.getNL() ; j++) {
                for (int k = 0; k <image.getNC() ; k++) {
                    if(image.getBandLinkedList().get(i).getBandTable()[j][k]>0)
                        binarised.get(i)[j][k] = 255;
                    else
                        binarised.get(i)[j][k] = 0;
                }
            }
        }
        return binarised;
    }

    //method to binarize an image in function of a thresh hold
    public static LinkedList<int[][]> binarizationProcess(int threshold1, int threshold2, Image image) {
        LinkedList<int[][]> IMA = ToolsProvider.createReceivingBandTables(image);
        for (int i = 0; i < image.getNbrOfBands(); i++) {
            for (int j = 0; j < image.getNL(); j++) {
                for (int k = 0; k < image.getNC(); k++) {
                    if (image.getBandLinkedList().get(i).getBandTable()[j][k] >= threshold1 &&
                            image.getBandLinkedList().get(i).getBandTable()[j][k] <= threshold2)
                        IMA.get(i)[j][k] = 255;
                    else IMA.get(i)[j][k] = 0;
                }
            }
        }
        return IMA;
    }

    public static boolean binaryToBoolean(int a){
        if(a==1)return true;
        else return false;
    }
}
