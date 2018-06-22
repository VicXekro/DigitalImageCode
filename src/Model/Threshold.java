package Model;

import javafx.scene.paint.Color;

/**
 * Created by Xekro on 6/24/2017.
 */
public class Threshold {
    private Color color;
    private int threshold1;
    private int threshold2;

    public Threshold(Color color, int threshold1, int threshold2) {
        this.color = color;
        this.threshold1 = threshold1;
        this.threshold2 = threshold2;
    }

    public Color getColor() {
        return color;
    }

    public int getThreshold1() {
        return threshold1;
    }

    public int getThreshold2() {
        return threshold2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setThreshold1(int threshold1) {
        this.threshold1 = threshold1;
    }

    public void setThreshold2(int threshold2) {
        this.threshold2 = threshold2;
    }
}
