/**
 * Created by amaxilatis on 1/10/15.
 */
package eu.organicity.annotation.jamaica.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.organicity.annotation.jamaica.examples.model.OrganicityData;
import eu.organicity.annotation.jamaica.examples.model.Reading;
import eu.organicity.annotation.jamaica.examples.utils.Utils;
import org.apache.log4j.Logger;
import us.jubat.anomaly.AnomalyClient;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnomalyExample {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(AnomalyExample.class);

    private static final String URL_PATTERN = "http://146.169.46.162:8081/api/v1/entities/urn:oc:entity:london:aqn:%s/readings?all_intervals=true&attribute_id=urn:oc:attributeType:chemicalAgentAtmosphericConcentration:airParticlesPM10&from=%s&function=sum&rollup=24h&to=%s";
    private static final String TIME_FORMAT = "Y-MM-dd'T'00:00'Z'";
    private static final String[] DEVICES = new String[]{"EA7", "BG2", "BQ7", "BQ8", "BX1", "BX2", "BT4", "BL0", "CD1", "CD9", "CT3", "CT4", "CT8", "EA8", "EI8"};

    public static void main(String[] args) throws IOException {
        //Connect to the server
        final String host = "150.140.5.98";
        final int port = 9199;
        final String name = "test";

        final AnomalyClient client = new AnomalyClient(host, port, name, 1);

        long start = System.currentTimeMillis();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);

        final String to = simpleDateFormat.format(new Date());
        final String from = simpleDateFormat.format(new Date(System.currentTimeMillis() - 60 * 60 * 60 * 1000));
        for (final String device : DEVICES) {
            final String url = String.format(URL_PATTERN, device, from, to);
            LOGGER.debug(url);
            final OrganicityData cdata = new ObjectMapper().readValue(new URL(url), OrganicityData.class);
            for (final Reading reading : cdata.getReadings()) {
                if (Double.parseDouble(reading.getValue()) < 50) {
                    client.add(Utils.makeDatum(Double.parseDouble(reading.getValue())));
                }
            }
        }
        LOGGER.info("Data was imported successfully in " + (System.currentTimeMillis() - start) + " ms");

        double correct = 0;
        double total = 0;

        for (final String device : DEVICES) {
            final String url = String.format(URL_PATTERN, device, from, to);
            LOGGER.debug(url);
            final OrganicityData cdata = new ObjectMapper().readValue(new URL(url), OrganicityData.class);
            for (final Reading reading : cdata.getReadings()) {
                float score = client.calcScore(Utils.makeDatum(Double.parseDouble(reading.getValue())));
                if (Double.parseDouble(reading.getValue()) < 50) {
                    if (score <= 5) {
                        correct++;
                    }
                } else {
                    if (score > 5) {
                        correct++;
                    }
                }
                total++;
            }
            LOGGER.info("score so far: " + correct + "/" + total + ", score: " + correct / total);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}