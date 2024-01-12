package pl.edu.agh.managementlibrarysystem.utils;

import javafx.beans.property.*;

public class FxmlPropertyFactory {
    public static StringProperty stringProperty(String value) {
        return new SimpleStringProperty(value);
    }

    public static IntegerProperty integerProperty(Integer value) {
        return new SimpleIntegerProperty(value);
    }

    public static DoubleProperty doubleProperty(Double value) {
        return new SimpleDoubleProperty(value);
    }

    public static LongProperty longProperty(Long value) {
        return new SimpleLongProperty(value);
    }
}
