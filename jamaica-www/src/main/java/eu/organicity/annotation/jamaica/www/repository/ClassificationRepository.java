package eu.organicity.annotation.jamaica.www.repository;

import eu.organicity.annotation.jamaica.www.model.Classification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;


/**
 * Provides operations on the Anomaly entities.
 */
public interface ClassificationRepository extends CrudRepository<Classification, Long> {
    
    Classification findById(long id);
    
    Set<Classification> findByClassificationConfigId(long classificationConfigId);
    
    @Query(value = "select distinct entity_id from classification where classification_config_id = ?1", nativeQuery = true)
    Set<String> findDistinctEntityIdByClassificationConfigId(long classificationConfigId);
    
    Set<Classification> findFirstDistinctEntityIdByClassificationConfigIdOrderByStartTimeDesc(long classificationConfigId);
    
    @Query(value = "select * from classification where classification_config_id = ?1 and entity_id = ?2 order by start_time desc limit 1", nativeQuery = true)
    Set<Classification> findFirstByClassificationConfigIdAndEntityId(long classificationConfigId, final String entityId);
    
    @Query(value = "select * from (select * from classification where classification_config_id = ?1) as qt1 group by entity_id order by start_time desc", nativeQuery = true)
    Set<Classification> findFirstByClassificationConfigId(long classificationConfigId);
    
    @Query(value = "select * from (select * from classification where classification_config_id = ?1) as qt1 order by start_time desc", nativeQuery = true)
    Set<Classification> findAllByClassificationConfigId(long classificationConfigId);
    
    Page<Classification> findByClassificationConfigId(long classificationConfigId, Pageable p);
    
    @Query(value = "select count(*) from classification where classification_config_id = ?1", nativeQuery = true)
    long countByClassificationConfigId(long classificationConfigId);
    
}