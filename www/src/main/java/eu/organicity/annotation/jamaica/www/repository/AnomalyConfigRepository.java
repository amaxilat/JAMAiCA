package eu.organicity.annotation.jamaica.www.repository;


/**
 * Created by katdel on 03-Jun-16.
 */


import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface AnomalyConfigRepository extends CrudRepository<AnomalyConfig, Long> {

    List<AnomalyConfig> findByTags(String tags);
}