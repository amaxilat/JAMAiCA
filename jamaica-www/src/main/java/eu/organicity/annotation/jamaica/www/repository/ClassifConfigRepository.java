package eu.organicity.annotation.jamaica.www.repository;

/**
 * Created by katdel on 03-Jun-16.
 */

import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface ClassifConfigRepository extends CrudRepository<ClassifConfig, Long> {

    ClassifConfig findById(long id);

    List<ClassifConfig> findByTags(String tags);

    @Query("SELECT max(p.jubatusPort) FROM ClassifConfig p")
    Integer findMaxJubatusPort();

    ClassifConfig findBySubscriptionId(String subscriptionId);
    ClassifConfig findByUrlOrion(String urlOrion);

}