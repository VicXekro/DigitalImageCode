package Model;


import java.util.LinkedList;

/**
 * Created by Xekro on 3/20/2017.
 */
public class Image {

    /**Used for format image 8-bits*/
    public static final String FORMAT_8BITS = "8-bits";
    /**Used for format image 16-bits in intensitites*/
    public static final String FORMAT_16BITS_INTENSITIES = "16-bits in Intensities";
    /**Used for format image 16-bits in amplitude*/
    public static final String FORMAT_16BITS_AMPLITUDE = "16-bits in Amplitude";

    /**Used for creation mode automatic*/
    public static final String CREATION_MODE_AUTO = "Automatically";
    /**Used for creation mode Semi-Automatically*/
    public static final String CREATION_MODE_SEMI_AUTO = "Semi-automatically";
    /**Used for creation mode Manually*/
    public static final String CREATION_MODE_MANUAL = "Manually";

    private int NC;
    private int NL;
    private int nbrOfBands;
    private String format;
    private String creationMode;
    private int sizeOfBand;
    private int [] magneticBand;
    private LinkedList<Band> bandLinkedList = new LinkedList<>();

    public Image(int NC, int NL, int nbrOfBands, String format, String creationMode) {
        this.NC = NC;
        this.NL = NL;
        this.nbrOfBands = nbrOfBands;
        this.format = format;
        this.creationMode = creationMode;
        setSizeOfBand(NC, NL);
        this.magneticBand = new int[getSizeOfBand()];
    }

    public Image(){

    }

    public int getNC() {
        return NC;
    }

    public void setNC(int NC) {
        this.NC = NC;
    }

    public int getNL() {
        return NL;
    }

    public void setNL(int NL) {
        this.NL = NL;
    }

    public int getNbrOfBands() {
        return nbrOfBands;
    }

    public void setNbrOfBands(int nbrOfBands) {
        this.nbrOfBands = nbrOfBands;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCreationMode() {
        return creationMode;
    }

    public void setCreationMode(String creationMode) {
        this.creationMode = creationMode;
    }

    public int getSizeOfBand() {
        return sizeOfBand;
    }

    public void setSizeOfBand(int NC, int NL) {
        if(format.equals(FORMAT_16BITS_AMPLITUDE) || format.equals(FORMAT_16BITS_INTENSITIES))
            this.sizeOfBand = NC*NL * 2 * nbrOfBands;
        else
            this.sizeOfBand = NC*NL*nbrOfBands;
    }

    public void setMagneticBand(int[] magneticBand) {
        this.magneticBand = magneticBand;
    }

    public void initiaLizetMagneticBand(int[] magneticBand){
        this.magneticBand = magneticBand;
    }

    public int[] getMagneticBand() {
        return magneticBand;
    }

    public LinkedList<Band> getBandLinkedList() {
        return bandLinkedList;
    }

    public void setBandLinkedList(LinkedList<Band> bandLinkedList) {
        this.bandLinkedList = bandLinkedList;
    }
}
