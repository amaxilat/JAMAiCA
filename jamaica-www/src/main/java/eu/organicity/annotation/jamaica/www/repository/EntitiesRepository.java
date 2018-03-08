package eu.organicity.annotation.jamaica.www.repository;

import eu.organicity.annotation.jamaica.www.model.CEntity;
import org.springframework.data.repository.CrudRepository;


/**
 * Provides operations on the Anomaly entities.
 */
public interface EntitiesRepository extends CrudRepository<CEntity, Long> {
    
    CEntity findById(long id);
    
    CEntity findByUrn(String urn);
}