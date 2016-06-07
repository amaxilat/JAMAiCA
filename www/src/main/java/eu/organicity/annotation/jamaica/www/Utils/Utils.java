package eu.organicity.annotation.jamaica.www.Utils;


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

    public static Process launchJubaclassifier(int port) {
        try {
            return Runtime.getRuntime().exec("jubaclassifier -p " + port + " -f generic.json");
        } catch (Exception e) {
            LOGGER.error(e, e);
            return null;
        }
    }

    public static Process launchJubaanomaly(int port) {
        try {
            return Runtime.getRuntime().exec("jubaanomaly -p " + port + " -f anomaly.json");
        } catch (Exception e) {
            LOGGER.error(e, e);
            return null;
        }
    }
}