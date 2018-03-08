package eu.organicity.annotation.jamaica.www.repository;

import eu.organicity.annotation.jamaica.www.model.CTag;
import org.springframework.data.repository.CrudRepository;


/**
 * Provides operations on the Anomaly entities.
 */
public interface TagsRepository extends CrudRepository<CTag, Long> {
    
    CTag findById(long id);
    
    CTag findByUrn(String urn);
}