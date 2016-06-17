package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.jamaica.www.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import us.jubat.anomaly.AnomalyClient;

import javax.annotation.PostConstruct;

@Service
public class JubatusService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(JubatusService.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("init");
    }

    /**
     * Launches a jubaclassifier instance in the given port.
     *
     * @param port the port for the jubaclassified RPC interface.
     * @return the {@see Process} object of the launched jubaclassifier instance.
     */
    public Process launchJubaclassifier(final int port) {
        try {
            return Runtime.getRuntime().exec("jubaclassifier -p " + port + " -f classifier.json");
        } catch (Exception e) {
            LOGGER.error(e, e);
            return null;
        }
    }

    /**
     * Launches a jubaanomaly instance in the given port.
     *
     * @param port the port for the jubaanomaly RPC interface.
     * @return the {@see Process} object of the launched jubaanomaly instance.
     */
    public Process launchJubaanomaly(final int port) {
        try {
            return Runtime.getRuntime().exec("jubaanomaly -p " + port + " -f anomaly.json");
        } catch (Exception e) {
            LOGGER.error(e, e);
            return null;
        }
    }

    /**
     * Calculates the anomaly score for an Orion attribute.
     *
     * @param anomalyClient an instance of {@see AnomalyClient}.
     * @param value         the value that needs to be checked oven the Jubatus instance.
     */
    @Async
    public void calcScore(final AnomalyClient anomalyClient, final String value) {
        double correct = 0;
        double total = 0;

        float score = anomalyClient.calcScore(Utils.makeDatum(Double.parseDouble(value)));
        if (Double.parseDouble(value) < 50) {
            if (score <= 5) {
                correct++;
            }
        } else {
            if (score > 5) {
                correct++;
            }
        }

        total++;
        LOGGER.info("score so far: " + correct + "/" + total + ", score: " + correct / total);

    }

    /**
     * Trains a jubatus instance with the provided data.
     *
     * @param anomalyClient an instance of {@see AnomalyClient}.
     */
    @Async
    public void trainAnomaly(final AnomalyClient anomalyClient) {
    }

}
