package eu.organicity.annotation.jamaica.www.service;


import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigListDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.annotation.jamaica.www.WebCreateClassificationDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.model.Classification;
import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
import eu.organicity.annotation.jamaica.www.repository.ClassifConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationTrainDataRepository;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import eu.organicity.annotation.jamaica.www.utils.Utils;
import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ClassificationService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClassificationService.class);
    
    @Autowired
    ClassifConfigRepository classifConfigRepository;
    @Autowired
    ClassificationRepository classificationRepository;
    @Autowired
    ClassificationTrainDataRepository classificationTrainDataRepository;
    
    @Autowired
    OrionService orionService;
    @Autowired
    SecurityService securityService;
    
    @Value("${application.baseUrl}")
    protected String baseUrl;
    
    final RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
    
    private Map<Long, Classifier> classifierMap = new HashMap<>();
    
    @PostConstruct
    public void init() {
        LOGGER.debug("init");
    }
    
    public Collection<Instance> train(long id) {
        final ClassifConfig classification = classifConfigRepository.findById(id);
        if (classification == null) {
            return null;
        }
        final List<ClassificationTrainData> data = classificationTrainDataRepository.findByClassificationConfigId(classification.getId());
        if (data.isEmpty()) {
            return null;
        }
        return train(classification, data);
    }
    
    public Collection<Instance> train(ClassifConfig classification, final List<ClassificationTrainData> data) {
        final Collection<Instance> trainData = new ArrayList<>();
        
        for (final ClassificationTrainData singleTrainData : data) {
            try {
                final SparseInstance instance = new SparseInstance();
                instance.put(0, Double.parseDouble(singleTrainData.getValue()));
                instance.setClassValue(singleTrainData.getTag());
                trainData.add(instance);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        final Classifier knn = new LibSVM();
        knn.buildClassifier(new DefaultDataset(trainData));
        classifierMap.put(classification.getId(), knn);
        return trainData;
    }
    
    
    public String classify(final Long id, final TrainDataDTO trainDataDTO) {
        return classify(id, trainDataDTO.getValue());
    }
    
    public String classify(final Long id, final String value) {
        if (!classifierMap.containsKey(id)) {
            if (train(id) == null) {
                return null;
            }
        }
        final SparseInstance instance = new SparseInstance();
        instance.put(0, Double.valueOf(value));
        return (String) classifierMap.get(id).classify(instance);
    }
    
    public ClassifConfig create(Principal principal, final ClassifConfigDTO classificationConfig) {
        final String randUiid = randomStringGenerator.getUuid();
        
        final ClassifConfig conf = new ClassifConfig(classificationConfig.getTypePat(), classificationConfig.getIdPat(), classificationConfig.getAttribute(), classificationConfig.getTagDomain(), randomStringGenerator.getUuid(), randUiid, "", System.currentTimeMillis(), false);
        
        //Add default orion settings
        if (classificationConfig.getContextBrokerUrl() == null) {
            conf.setContextBrokerUrl(orionService.getContextBrokerUrl());
        }
        if (classificationConfig.getContextBrokerService() == null) {
            conf.setContextBrokerService(orionService.getContextBrokerService());
        }
        if (classificationConfig.getContextBrokerServicePath() == null) {
            conf.setContextBrokerServicePath(orionService.getContextBrokerServicePath());
        }
        if (principal.getName() != null) {
            conf.setUser(principal.getName());
        }
        // save anomaly config entry
        ClassifConfig storedConfig = classifConfigRepository.save(conf);
        
        LOGGER.info("successful save new classification job. Returned id: " + storedConfig.getId());
        
        return storedConfig;
    }
    
    public ClassifConfig create(Principal principal, final WebCreateClassificationDTO webDTO) {
        final String randUiid = randomStringGenerator.getUuid();
        
        final ClassifConfig conf = new ClassifConfig(webDTO.getTypePat(), webDTO.getIdPat(), webDTO.getAttribute(), webDTO.getTags(), randomStringGenerator.getUuid(), randUiid, "", System.currentTimeMillis(), false);
        
        conf.setUser(principal.getName());
        
        //Add default orion settings
        conf.setContextBrokerUrl(orionService.getContextBrokerUrl());
        conf.setContextBrokerService(orionService.getContextBrokerService());
        conf.setContextBrokerServicePath(orionService.getContextBrokerServicePath());
        
        
        // save anomaly config entry
        ClassifConfig storedConfig = classifConfigRepository.save(conf);
        
        LOGGER.info("successful save new classification job. Returned id: " + storedConfig.getId());
        
        return storedConfig;
    }
    
    public ClassificationTrainData addTrainData(long id, String tag, String value) {
        final ClassificationTrainData data = new ClassificationTrainData();
        data.setClassificationConfigId(id);
        data.setTag(tag);
        data.setValue(value);
        return classificationTrainDataRepository.save(data);
    }
    
    @Transactional
    public void delete(final long id) {
        classifConfigRepository.delete(id);
        final List<ClassificationTrainData> elems = classificationTrainDataRepository.findByClassificationConfigId(id);
        classificationTrainDataRepository.delete(elems);
        final Set<Classification> classifications = classificationRepository.findByClassificationConfigId(id);
        classificationRepository.delete(classifications);
        
    }
    
    public ClassifConfig enable(final long id) {
        return enable(id, true);
    }
    
    public ClassifConfig enable(final long id, final boolean state) {
        ClassifConfig config = classifConfigRepository.findById(id);
        config.setEnable(state);
        classifConfigRepository.save(config);
        return config;
    }
    
    public ClassifConfig subscribe(final long id) {
        
        ClassifConfig storedConfig = classifConfigRepository.findById(id);
        
        try {
            
            final OrionEntity e = new OrionEntity();
            e.setId(storedConfig.getIdPat());
            e.setIsPattern("true");
            e.setType(storedConfig.getTypePat());
            String[] cond = new String[1];
            cond[0] = "TimeInstant";
            
            // subscribe to Orion
            final SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "v1/notifyContext/" + storedConfig.getUrlOrion(), cond, "P1D", storedConfig);
            
            final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
            LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);
            
            storedConfig.setSubscriptionId(subscriptionId);
            storedConfig = classifConfigRepository.save(storedConfig);
            
        } catch (IOException | DataAccessException er) {
            LOGGER.error(er.getLocalizedMessage(), er);
        }
        
        return storedConfig;
    }
    
    public TrainDataListDTO train(final long id, final String tag, final TrainDataListDTO trainDataDTO) {
        final List<ClassificationTrainData> trainDataList = new ArrayList<>();
        for (final TrainDataDTO singleTrainData : trainDataDTO.getData()) {
            
            final ClassificationTrainData data = new ClassificationTrainData();
            data.setClassificationConfigId(id);
            data.setTag(tag);
            data.setValue(singleTrainData.getValue());
            trainDataList.add(data);
        }
        
        if (!trainDataList.isEmpty()) {
            classificationTrainDataRepository.save(trainDataList);
        }
        return trainDataDTO;
    }
    
    public ClassifConfigListDTO findByUser() {
        final ClassifConfigListDTO dto = new ClassifConfigListDTO();
        dto.setClassificationConfigurations(new ArrayList<>());
        if (securityService.isAdmin()) {
            for (final ClassifConfig classifConfig : classifConfigRepository.findAll()) {
                dto.getClassificationConfigurations().add(Utils.newClassifConfigDTO(classifConfig));
            }
        } else {
            for (final ClassifConfig classifConfig : classifConfigRepository.findByUser(securityService.getUser())) {
                dto.getClassificationConfigurations().add(Utils.newClassifConfigDTO(classifConfig));
            }
        }
        return dto;
    }
    
    public ClassifConfigDTO findById(final int id) {
        return Utils.newClassifConfigDTO(classifConfigRepository.findById(id));
    }
}
