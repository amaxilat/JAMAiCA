package eu.organicity.annotation.jamaica.www.utils;


import org.apache.log4j.Logger;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

public class Utils {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(Utils.class);

    public static Datum makeDatum(double value) {
        return new Datum().addNumber("value", value);
    }

    public static LabeledDatum makeTrainDatum(String label, double height) {
        return new LabeledDatum(label, makeDatum(height));
    }

}