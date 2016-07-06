package eu.organicity.annotation.jamaica.www.repository;

import eu.organicity.annotation.jamaica.www.model.Anomaly;
import org.springframework.data.repository.CrudRepository;


/**
 * Provides operations on the {@see Anomaly} entities.
 */
public interface AnomalyRepository extends CrudRepository<Anomaly, Long> {

    Anomaly findById(long id);

}