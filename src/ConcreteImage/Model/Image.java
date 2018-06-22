package ConcreteImage.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Xekro on 6/2/2017.
 */
public class Image {

    private BufferedImage bufferedImage;
    private int width;
    private int height;
    private int numberOfBands;
    private String path;
    private LinkedList<int[][]>bandTable = new LinkedList<>();

    public Image(File file){
        try{
            bufferedImage = ImageIO.read(file);
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
            path = file.getPath();
            SampleModel sampleModel = bufferedImage.getSampleModel();
            numberOfBands = sampleModel.getNumBands();
            if(numberOfBands<3 || numberOfBands>3)
                throw new IllegalArgumentException();
            copyColorToTable();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image(BufferedImage image){
        bufferedImage = image;
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        SampleModel sampleModel = bufferedImage.getSampleModel();
        numberOfBands = sampleModel.getNumBands();
        copyColorToTable();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNumberOfBands() {
        return numberOfBands;
    }

    public void setNumberOfBands(int numberOfBands) {
        this.numberOfBands = numberOfBands;
    }

    public LinkedList<int[][]> getBandTable() {
        return bandTable;
    }

    public void setBandTable(LinkedList<int[][]> bandTable) {
        this.bandTable = bandTable;
    }

    private void copyColorToTable(){
        for (int i = 0; i <this.getNumberOfBands() ; i++) {
            int [][] temp = new int[this.getHeight()][this.getWidth()];
            bandTable.add(temp);
        }
        for (int j = 0; j <this.getHeight(); j++) {
            for (int k = 0; k <this.getWidth() ; k++) {
                Color c = new Color(this.getBufferedImage().getRGB(k,j));
                bandTable.get(0)[j][k] = c.getRed();
                bandTable.get(1)[j][k] = c.getGreen();
                bandTable.get(2)[j][k] = c.getBlue();
            }
        }
    }
}
