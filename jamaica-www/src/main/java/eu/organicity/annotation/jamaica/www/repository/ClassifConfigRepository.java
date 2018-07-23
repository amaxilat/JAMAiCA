package eu.organicity.annotation.jamaica.www.repository;

/**
 * Created by katdel on 03-Jun-16.
 */

import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassifConfigRepository extends CrudRepository<ClassifConfig, Long> {
    
    ClassifConfig findById(long id);
    
    List<ClassifConfig> findByTags(String tags);
    
    ClassifConfig findBySubscriptionId(String subscriptionId);
    
    ClassifConfig findByUrlOrion(String urlOrion);
    
    List<ClassifConfig> findByUser(String user);
    
}