package eu.organicity.annotation.jamaica.examples.utils;


import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

public class Utils {
    public static Datum makeDatum(double value) {
        return new Datum().addNumber("value", value);
    }

    public static LabeledDatum makeTrainDatum(String label, double height) {
        return new LabeledDatum(label, makeDatum(height));
    }
}