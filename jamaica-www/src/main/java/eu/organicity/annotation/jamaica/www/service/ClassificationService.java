package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
import eu.organicity.annotation.jamaica.www.repository.ClassifConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationTrainDataRepository;
import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassificationService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClassificationService.class);
    
    @Autowired
    ClassifConfigRepository classifConfigRepository;
    @Autowired
    ClassificationTrainDataRepository classificationTrainDataRepository;
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
}
