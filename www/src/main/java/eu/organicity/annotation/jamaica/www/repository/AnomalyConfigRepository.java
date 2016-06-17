package eu.organicity.annotation.jamaica.www.repository;


/**
 * Created by katdel on 03-Jun-16.
 */


import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnomalyConfigRepository extends CrudRepository<AnomalyConfig, Long> {

    AnomalyConfig findById(long id);

    List<AnomalyConfig> findByTags(String tags);

    AnomalyConfig findTopByJubatusPortDesc();

    AnomalyConfig findBySubscriptionId(String subscription_id);
}