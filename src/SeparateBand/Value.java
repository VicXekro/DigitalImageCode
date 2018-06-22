package SeparateBand;

import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by Xekro on 3/3/2017.
 */
public class Value {

    private SimpleObjectProperty<Byte> byteValue;

    public Value(byte byteValue) {
        this.byteValue = new SimpleObjectProperty<>(byteValue);
    }

    public Byte getByteValue() {
        return byteValue.get();
    }

    public SimpleObjectProperty<Byte> byteValueProperty() {
        return byteValue;
    }

    public void setByteValue(Byte byteValue) {
        this.byteValue.set(byteValue);
    }
}
