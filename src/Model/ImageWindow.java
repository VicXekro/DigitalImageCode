package Model;

/**
 * Created by Xekro on 5/10/2017.
 */
public class ImageWindow extends LinearMask {

    private int n;
    private int imageFactor;
    private int numberOfViews;
    private String adaptiveFilterMethod;

    public ImageWindow(int windowSize){
        super.setNCmask(windowSize);
        super.setNLmask(windowSize);
        super.setCenterX(windowSize/2);
        super.setCenterY(windowSize/2);
        this.n = windowSize;
    }

    public ImageWindow(int numberOfColumns, int numberOfLines){
        super.setNCmask(numberOfColumns);
        super.setNLmask(numberOfLines);
        super.setCenterX(numberOfColumns/2);
        super.setCenterY(numberOfLines/2);
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getImageFactor() {
        return imageFactor;
    }

    public void setImageFactor(int imageFactor) {
        this.imageFactor = imageFactor;
    }

    public int getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(int numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public String getAdaptiveFilterMethod() {
        return adaptiveFilterMethod;
    }

    public void setAdaptiveFilterMethod(String adaptiveFilterMethod) {
        this.adaptiveFilterMethod = adaptiveFilterMethod;
    }
}
