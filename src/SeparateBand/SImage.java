package SeparateBand;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Xekro on 3/3/2017.
 */
public class SImage {

    private byte sizeOfBans;
    private int NC;
    private int NL;
    private String format;
    private int numberOfBans;
    private String creationType;

    public SImage(Byte b){

    }

    public SImage(byte sizeOfBans, int NC, int NL, String format, int numberOfBans, String creationType) {
        this.sizeOfBans = sizeOfBans;
        this.NC = NC;
        this.NL = NL;
        this.format = format;
        this.numberOfBans = numberOfBans;
        this.creationType = creationType;
    }


    public void setSizeOfBans(byte sizeOfBans) {
        this.sizeOfBans = sizeOfBans;
    }

    public void setNC(int NC) {
        this.NC = NC;
    }

    public void setNL(int NL) {
        this.NL = NL;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setNumberOfBans(int numberOfBans) {
        this.numberOfBans = numberOfBans;
    }

    public void setCreationType(String creationType) {
        this.creationType = creationType;
    }

    public byte getSizeOfBans() {
        return sizeOfBans;
    }

    public int getNC() {
        return NC;
    }

    public int getNL() {
        return NL;
    }

    public String getFormat() {
        return format;
    }

    public int getNumberOfBans() {
        return numberOfBans;
    }

    public String getCreationType() {
        return creationType;
    }

}
