package eu.organicity.annotation.jamaica.www.repository;


/**
 * Created by katdel on 03-Jun-16.
 */


import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface AnomalyConfigRepository extends CrudRepository<AnomalyConfig, Long> {

    AnomalyConfig findById(long id);

    List<AnomalyConfig> findByTags(String tags);

    @Query("SELECT max(p.jubatusPort) FROM AnomalyConfig p")
    Integer findMaxJubatusPort();

    AnomalyConfig findBySubscriptionId(String subscriptionId);
    AnomalyConfig findByUrlOrion(String urlOrion);
}