package eu.organicity.annotation.jamaica.examples;

import eu.organicity.annotation.jamaica.examples.utils.Utils;
import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.EstimateResult;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ClassificationExample {

    public static void main(String[] args) throws Exception {
        //Connect to the server
        String host = "150.140.5.98";
        int port = 9199;
        String name = "test";

        ClassifierClient client = new ClassifierClient(host, port, name, 1);

        //Build the train dataset from the input file
        List<LabeledDatum> trainData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("humidity.classified"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String label = line.split(",")[0];
                Double value = Double.valueOf(line.split(",")[1]);
                trainData.add(Utils.makeTrainDatum(label, value));
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                //train in batches to avoid timeouts
                if (trainData.size() == 100) {
                    client.train(trainData);
                    trainData.clear();
                }
            }

        } catch (Exception e) {
        }


        double correct = 0;
        double total = 0;
        //test data
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
                    System.out.println("correct : " + correct);
                    System.out.println("total : " + total);
                    System.out.println("score : " + correct / total);
                }
            }
        } catch (Exception e) {
        }


        System.exit(0);
    }
}