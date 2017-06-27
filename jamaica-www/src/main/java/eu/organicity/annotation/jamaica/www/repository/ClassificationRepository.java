package eu.organicity.annotation.jamaica.www.repository;

import eu.organicity.annotation.jamaica.www.model.Classification;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;


/**
 * Provides operations on the Anomaly entities.
 */
public interface ClassificationRepository extends CrudRepository<Classification, Long> {
    
    Classification findById(long id);
    
    Set<Classification> findByClassificationConfigId(long classificationConfigId);
    
    long countByClassificationConfigId(long classificationConfigId);
    
}