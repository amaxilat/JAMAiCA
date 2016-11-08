package eu.organicity.annotation.jamaica.www.repository;


import eu.organicity.annotation.jamaica.www.model.AnomalyTrainData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnomalyTrainDataRepository extends CrudRepository<AnomalyTrainData, Long> {

    AnomalyTrainData findById(long id);

    List<AnomalyTrainData> findByAnomalyConfigId(String anomalyConfigId);
}