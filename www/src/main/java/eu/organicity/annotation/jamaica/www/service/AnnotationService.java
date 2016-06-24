package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.jamaica.www.model.Anomaly;
import eu.organicity.annotation.jamaica.www.repository.AnomalyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AnnotationService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(AnnotationService.class);

    @Autowired
    AnomalyRepository anomalyRepository;

    @Value("${application.env}")
    private String env;

    @PostConstruct
    public void init() {
        LOGGER.debug("init");
    }

    @Async
    public void storeAnomaly(final String entityId, final String attribute, final String value, final long anomalyConfigId, final double score) {
        storeAnomalyLocaly(entityId, attribute, value, anomalyConfigId, score);
    }

    public void storeAnomalyLocaly(final String entityId, final String attribute, final String value, final long anomalyConfigId, final double score) {
        Anomaly anomaly = new Anomaly();
        anomaly.setEntityId(entityId);
        anomaly.setEntityAttribute(attribute);
        anomaly.setAttributeValue(value);
        anomaly.setScore(score);
        anomaly.setAnomalyConfigId(anomalyConfigId);
        anomalyRepository.save(anomaly);
    }

}
