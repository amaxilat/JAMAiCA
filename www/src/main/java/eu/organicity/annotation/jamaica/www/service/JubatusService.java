package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.jamaica.www.repository.AnomalyRepository;
import eu.organicity.annotation.jamaica.www.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    AnomalyRepository anomalyRepository;

    @Autowired
    AnnotationService annotationService;

    @Value("${application.env}")
    private String env;

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
        if ("production".equals(env)) {
            try {
                return Runtime.getRuntime().exec("source /opt/jubatus/profile ; jubaclassifier -p " + port + " -f classifier.json");
            } catch (Exception e) {
                LOGGER.error(e, e);
                return null;
            }
        } else {
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
        if ("production".equals(env)) {
            try {
                LOGGER.info("launching jubatus...");
                return Runtime.getRuntime().exec("jubaanomaly -p " + port + " -f /home/amaxilatis/anomaly.json");
            } catch (Exception e) {
                LOGGER.error(e, e);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Calculates the anomaly score for an Orion attribute.
     *
     * @param anomalyClient   an instance of {@see AnomalyClient}.
     * @param value           the value that needs to be checked oven the Jubatus instance.
     * @param entityId        the id of the entity that produced the value.
     * @param attribute       the attribute to be tested.
     * @param anomalyConfigId the id of the anomaly detection job performed.
     */
    @Async
    public void calcScore(final AnomalyClient anomalyClient, final String value, final String entityId, final String attribute, final long anomalyConfigId) {
        final float score = anomalyClient.calcScore(Utils.makeDatum(Double.parseDouble(value)));
        if (score < 3) {
            LOGGER.info("Value is normal! score: " + score);
        } else {
            LOGGER.info("Value is abnormal! score: " + score);
            annotationService.storeAnomaly(entityId, attribute, value, anomalyConfigId, score);
        }
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
