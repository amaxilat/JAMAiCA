package eu.organicity.annotation.jamaica.www.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import eu.organicity.annotation.jamaica.www.dto.AnnotationDTO;
import eu.organicity.annotation.jamaica.www.dto.TagDomainDTO;
import eu.organicity.annotation.jamaica.www.model.Anomaly;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.repository.AnomalyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

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

    @Value("${application.annotationUrl}")
    public String annotationUrl;

    private RestTemplate annotationRestTemplate;

    @PostConstruct
    public void init() {
        LOGGER.debug("init");
        annotationRestTemplate = new RestTemplate();

    }

    @Async
    public void storeAnomaly(final String entityId, final String attribute, final String value, final long anomalyConfigId, final double score) {
        storeAnomalyLocaly(entityId, attribute, value, anomalyConfigId, score);
    }

    public void storeAnomalyLocaly(final String entityId, final String attribute, final String value, final long anomalyConfigId, final double score) {
        final Anomaly anomaly = new Anomaly();
        anomaly.setEntityId(entityId);
        anomaly.setEntityAttribute(attribute);
        anomaly.setAttributeValue(value);
        anomaly.setScore(score);
        anomaly.setAnomalyConfigId(anomalyConfigId);
        anomalyRepository.save(anomaly);
    }

    /**
     * TODO: extend the {@see AnnotationDTO} to include also the attribute.
     *
     * @param entityId
     * @param attribute
     * @param value
     * @param anomalyConfig
     * @param score
     */
    public void storeAnomaly2Annotation(final String entityId, final String attribute, final String value, final AnomalyConfig anomalyConfig, final double score) {

        final TagDomainDTO domain = retrieveTagDomain(anomalyConfig.getTags());

        final AnnotationDTO annotationDTO = new AnnotationDTO();
        annotationDTO.setAnnotationId(null);
        annotationDTO.setApplication(null);
        annotationDTO.setAssetUrn(entityId);
        annotationDTO.setDatetime(String.valueOf(System.currentTimeMillis()));
        annotationDTO.setNumericValue(score);
        annotationDTO.setTagUrn(domain.getTags().iterator().next().getUrn());
        annotationDTO.setTextValue(null);
        annotationDTO.setUser(null);

        postAnnotation(annotationDTO);
    }


    public TagDomainDTO retrieveTagDomain(final String tagDomainName) {
        try {
            return new ObjectMapper().readValue(new URL(annotationUrl + "tagDomains/" + tagDomainName), TagDomainDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postAnnotation(final AnnotationDTO annotationDTO) {
        return annotationRestTemplate.postForObject(
                annotationUrl + "annotations/" + annotationDTO.getAssetUrn(), annotationDTO, String.class);
    }

}
