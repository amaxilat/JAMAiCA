package eu.organicity.annotation.jamaica.examples;

import eu.organicity.annotation.jamaica.examples.utils.Utils;
import org.apache.log4j.Logger;
import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.EstimateResult;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Example that retrieves data from a local file and trains a jubatus classification instance and then tests this instance with the same values to extract success score.
 */
public class ClassificationExample {

    private static final int SKIP_LINES = 10;

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(ClassificationExample.class);

    public static void main(String[] args) throws Exception {
        //Connect to the server
        String host = "150.140.5.27";
        int port = 9199;
        String name = "test";

        final Process p = Utils.launchJubaclassifier(port);

        ClassifierClient client = new ClassifierClient(host, port, name, 1);

        //Build the train dataset from the input file
        List<LabeledDatum> trainData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("humidity.classified"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String label = line.split(",")[0];
                Double value = Double.valueOf(line.split(",")[1]);
                trainData.add(Utils.makeTrainDatum(label, value));
                for (int i = 0; i < SKIP_LINES; i++) {
                    br.readLine();
                }
                //train in batches to avoid timeouts
                if (trainData.size() == 100) {
                    client.train(trainData);
                    trainData.clear();
                }
            }

        } catch (Exception e) {
            LOGGER.error(e, e);
        }


        double correct = 0;
        double total = 0;
        //test data from the original dataset against the trained data to validate the correctness of the trained data
        try (BufferedReader br = new BufferedReader(new FileReader("humidity.classified"))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String label = line.split(",")[0];
                final Double value = Double.valueOf(line.split(",")[1]);

                final List<Datum> testData = new ArrayList<>();
                testData.add(Utils.makeDatum(value));
                List<List<EstimateResult>> results = client.classify(testData);
                double maxScore = Double.MIN_NORMAL;
                String maxLabel = "";
                for (List<EstimateResult> result : results) {
                    for (EstimateResult r : result) {
                        if (r.score > maxScore) {
                            maxScore = r.score;
                            maxLabel = r.label;
                        }
                    }
                    if (maxLabel.equals(label)) {
                        correct++;
                    }
                    total++;

                }
            }
            LOGGER.info("correct: " + correct + "\ttotal: " + total + "\tscore : " + correct / total);
        } catch (Exception e) {
            LOGGER.error(e, e);
        }

        System.exit(0);
    }
}