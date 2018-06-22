package Model;

/**
 * Created by Xekro on 5/16/2017.
 */
public class Mask extends LinearMask {

    private int order;
    private String morphologicalOperation;
    private int threshold1;
    private int threshold2;
    private boolean isThreshHoldDefine;

    public Mask(int NCmask, int NLmask, int centerX, int centerY, int[][] mask, int order) {
        super(NCmask, NLmask, centerX, centerY, mask);
        this.order = order;
    }

    public Mask(){}

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getMorphologicalOperation() {
        return morphologicalOperation;
    }

    public void setMorphologicalOperation(String morphologicalOperation) {
        this.morphologicalOperation = morphologicalOperation;
    }

    public Integer getThreshold1() {
        return threshold1;
    }

    public void setThreshold1(int threshold1) {
        this.threshold1 = threshold1;
    }

    public int getThreshold2() {
        return threshold2;
    }

    public void setThreshold2(int threshold2) {
        this.threshold2 = threshold2;
    }

    public boolean isThreshHoldDefine() {
        return isThreshHoldDefine;
    }

    public void setThreshHoldDefine(boolean threshHoldDefine) {
        isThreshHoldDefine = threshHoldDefine;
    }
}
