package Model;


/**
 * Created by Xekro on 4/21/2017.
 */
public class LinearMask {
    public static final String ZERO_METHOD = "Zero Method";
    public static final String SYMMETRIC_METHOD = "Symmetric Method";
    public static final String CIRCULAR_METHOD = "Circular Method";
    public static final String ZERO = "Zero";
    public static final String SYMMETRIC = "Symmetric";
    public static final String CIRCULAR= "Circular";

    private int centerX;
    private int centerY;
    private int [][] mask;
    private int NCmask;
    private int NLmask;
    private String borderFeeling;
    private String name;

    public LinearMask(int NCmask, int NLmask, int centerX, int centerY, int[][] mask){
        this.NCmask = NCmask;
        this.NLmask = NLmask;
        this.centerX = centerX;
        this.centerY = centerY;
        this.mask = mask;
    }

    public LinearMask(int NCmask, int NLmask, int centerX, int centerY, int[][] mask, String name){
        this.NCmask = NCmask;
        this.NLmask = NLmask;
        this.centerX = centerX;
        this.centerY = centerY;
        this.mask = mask;
        this.name = name;
    }

    public LinearMask(){}

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int[][] getMask() {
        return mask;
    }

    public void setMask(int[][] mask) {
        this.mask = mask;
    }

    public int getNCmask() {
        return NCmask;
    }

    public void setNCmask(int NCmask) {
        this.NCmask = NCmask;
    }

    public int getNLmask() {
        return NLmask;
    }

    public void setNLmask(int NLmask) {
        this.NLmask = NLmask;
    }

    public String getBorderFeeling() {
        return borderFeeling;
    }

    public void setBorderFeeling(String borderFeeling) {
        this.borderFeeling = borderFeeling;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
