package eu.organicity.annotation.jamaica.www.utils;


import org.apache.log4j.Logger;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

public class Utils {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(Utils.class);

    /**
     * Creates a {@see Datum} to be used in queries to a Jubatus instance.
     *
     * @param value the value of the {@see Datum}.
     * @return the created {@see Datum}.
     */
    public static Datum makeDatum(double value) {
        return new Datum().addNumber("value", value);
    }

    /**
     * Creates a {@see LabeledDatum} to be used in queries to a Jubatus instance.
     *
     * @param label the label of the {@see Datum}.
     * @param value the value of the {@see Datum}.
     * @return the created {@see LabeledDatum}.
     */
    public static LabeledDatum makeTrainDatum(String label, double value) {
        return new LabeledDatum(label, makeDatum(value));
    }

}