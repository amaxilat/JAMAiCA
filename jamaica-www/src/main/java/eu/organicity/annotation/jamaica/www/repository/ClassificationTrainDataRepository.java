package eu.organicity.annotation.jamaica.www.repository;


import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassificationTrainDataRepository extends CrudRepository<ClassificationTrainData, Long> {

    ClassificationTrainData findById(long id);

    List<ClassificationTrainData> findByClassificationConfigId(long classificationConfigId);
    
    long countByClassificationConfigId(long classificationConfigId);
}