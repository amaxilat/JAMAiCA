package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.client.AnnotationServiceClient;
import eu.organicity.annotation.common.dto.AnnotationDTO;
import eu.organicity.annotation.common.dto.CreateAnnotationDTO;
import eu.organicity.annotation.common.dto.TagDomainDTO;
import eu.organicity.annotation.jamaica.www.model.Anomaly;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.CEntity;
import eu.organicity.annotation.jamaica.www.model.CTag;
import eu.organicity.annotation.jamaica.www.model.Classification;
import eu.organicity.annotation.jamaica.www.repository.AnomalyConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.AnomalyRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationRepository;
import eu.organicity.annotation.jamaica.www.repository.EntitiesRepository;
import eu.organicity.annotation.jamaica.www.repository.TagsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class AnnotationService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AnnotationService.class);
    
    @Autowired
    AnomalyRepository anomalyRepository;
    @Autowired
    EntitiesRepository entitiesRepository;
    @Autowired
    TagsRepository tagsRepository;
    @Autowired
    ClassificationRepository classificationRepository;
    @Autowired
    AnomalyConfigRepository anomalyConfigRepository;
    @Autowired
    WsService wsService;
    
    @Value("${application.env}")
    private String env;
    
    @Value("${application.annotationUrl}")
    public String annotationUrl;
    
    private RestTemplate annotationRestTemplate;
    private AnnotationServiceClient annotation = new AnnotationServiceClient("");
    
    @PostConstruct
    public void init() {
        LOGGER.debug("init");
    }
    
    public void storeAnomaly(final String entityId, final String attribute, final String value, final long anomalyConfigId, final double score) {
        storeAnomalyLocaly(entityId, attribute, value, anomalyConfigId, score);
        //        storeAnomaly2Annotation(entityId, attribute, value, anomalyConfigRepository.findById(anomalyConfigId), score);
    }
    
    public void storeAnomalyLocaly(final String entityId, final String attribute, final String value, final long anomalyConfigId, final double score) {
        final Anomaly anomaly = new Anomaly();
        anomaly.setEntityId(entityId);
        anomaly.setEntityAttribute(attribute);
        anomaly.setAttributeValue(value);
        anomaly.setScore(score);
        anomaly.setAnomalyConfigId(anomalyConfigId);
        anomaly.setStartTime(System.currentTimeMillis());
        anomalyRepository.save(anomaly);
    }
    
    /**
     * TODO: extend the {@link AnnotationDTO} to include also the attribute.
     *
     * @param entityId
     * @param attribute
     * @param value
     * @param anomalyConfig
     * @param score
     */
    public AnnotationDTO storeAnomaly2Annotation(final String entityId, final String attribute, final String value, final AnomalyConfig anomalyConfig, final double score) {
        
        final TagDomainDTO domain = annotation.getTagDomain(anomalyConfig.getTags());
        
        final CreateAnnotationDTO annotationDTO = new CreateAnnotationDTO();
        annotationDTO.setApplication("jamaica");
        annotationDTO.setAssetUrn(entityId);
        annotationDTO.setTextValue(String.valueOf(score));
        annotationDTO.setTagUrn(domain.getTags().iterator().next().getUrn());
        annotationDTO.setUser("jamaica");
        
        return postAnnotation(annotationDTO);
    }
    
    
    public AnnotationDTO postAnnotation(final CreateAnnotationDTO annotationDTO) {
        LOGGER.info(annotationUrl + "annotations/" + annotationDTO.getAssetUrn());
        return annotation.postAnnotation(annotationDTO);
    }
    
    public void storeClassification(String entityId, String attribute, String value, long classificationConfigId, String tag, double score, long diff) {
        wsService.notifyClassification(entityId, attribute, value, classificationConfigId, tag, score);
        storeClassificationLocaly(entityId, attribute, value, classificationConfigId, tag, score, diff);
        try {
            storeClassification2Annotation(entityId, attribute, value, classificationConfigId, tag, score);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }
    
    public AnnotationDTO storeClassification2Annotation(final String entityId, final String attribute, final String value, final long classificationConfigId, final String tag, final double score) {
        
        final CreateAnnotationDTO annotationDTO = new CreateAnnotationDTO();
        annotationDTO.setApplication("jamaica");
        annotationDTO.setAssetUrn(entityId);
        annotationDTO.setTextValue(String.valueOf(score));
        annotationDTO.setTagUrn(tag);
        annotationDTO.setUser("jamaica");
        
        return postAnnotation(annotationDTO);
    }
    
    public void storeClassificationLocaly(final String entityId, final String attribute, final String value, final long classificationConfigId, final String tag, final double score, long diff) {
        CEntity entity = entitiesRepository.findByUrn(entityId);
        if (entity == null) {
            entity = entitiesRepository.save(new CEntity(null, entityId));
        }
        CTag ctag = tagsRepository.findByUrn(tag);
        if (ctag == null) {
            ctag = tagsRepository.save(new CTag(null, tag));
        }
        final Classification classification = new Classification();
        classification.setEntity(entity);
        classification.setEntityAttribute(attribute);
        classification.setAttributeValue(value);
        classification.setTag(ctag);
        classification.setScore(score);
        classification.setClassificationConfigId(classificationConfigId);
        classification.setStartTime(System.currentTimeMillis());
        classification.setProcessingTime(diff);
        classificationRepository.save(classification);
    }
    
}
