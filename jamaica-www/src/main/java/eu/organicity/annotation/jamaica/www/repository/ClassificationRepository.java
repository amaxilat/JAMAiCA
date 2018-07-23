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
    
    Page<Classification> findAll(Pageable page);
    Classification findById(long id);
    
    Set<Classification> findByClassificationConfigId(long classificationConfigId);
    
    @Query(value = "select distinct entities_id from classification where classification_config_id = ?1", nativeQuery = true)
    Set<String> findDistinctEntitiesIdByClassificationConfigId(long classificationConfigId);
    
    Set<Classification> findFirstDistinctEntitiesIdByClassificationConfigIdOrderByStartTimeDesc(long classificationConfigId);
    
    @Query(value = "select * from classification where classification_config_id = ?1 and entities_id = ?2 order by start_time desc limit 1", nativeQuery = true)
    Set<Classification> findFirstByClassificationConfigIdAndEntitiesId(long classificationConfigId, final long entitiesId);
    
    @Query(value = "select * from (select * from classification where classification_config_id = ?1) as qt1 group by entities_id order by start_time desc", nativeQuery = true)
    Set<Classification> findFirstByClassificationConfigId(long classificationConfigId);
    
    @Query(value = "select * from (select * from classification where classification_config_id = ?1) as qt1 order by start_time desc", nativeQuery = true)
    Set<Classification> findAllByClassificationConfigId(long classificationConfigId);
    
    Page<Classification> findByClassificationConfigId(long classificationConfigId, Pageable p);
    
    @Query(value = "select count(*) from classification where classification_config_id = ?1", nativeQuery = true)
    long countByClassificationConfigId(long classificationConfigId);
    
}