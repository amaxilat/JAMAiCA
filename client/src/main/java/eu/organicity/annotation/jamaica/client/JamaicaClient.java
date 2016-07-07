package eu.organicity.annotation.jamaica.client;

import eu.organicity.annotation.jamaica.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import org.springframework.web.client.RestTemplate;

public class JamaicaClient {
    private static final String BASE_URL = "http://jamaica.organicity.eu/v1/";
    private final RestTemplate restTemplate;

    public JamaicaClient() {
        restTemplate = new RestTemplate();

    }

    public AnomalyConfigDTO getAnomalyConfig(final long id) {
        return restTemplate.getForObject(BASE_URL + "config/anomaly/" + id, AnomalyConfigDTO.class);
    }

    public void deleteAnomalyConfig(final long id){
        restTemplate.delete(BASE_URL + "config/anomaly/" + id, AnomalyConfigDTO.class);
    }

    public AnomalyConfigDTO putAnomalyConfig(AnomalyConfigDTO anomalyConfig){

        restTemplate.put(BASE_URL + "config/anomaly/", anomalyConfig, AnomalyConfigDTO.class);
        return anomalyConfig;
    }

    public ClassifConfigDTO getClassificationConfig(final long id){
        return restTemplate.getForObject(BASE_URL + "config/classification/" + id, ClassifConfigDTO.class);

    }

    public void deleteClassificationConfig(final long id){
        restTemplate.delete(BASE_URL + "config/classification/" + id, ClassifConfigDTO.class);

    }

    public ClassifConfigDTO putClassificationConfig(ClassifConfigDTO classificationConfig){

        restTemplate.put(BASE_URL + "config/classification/", classificationConfig, ClassifConfigDTO.class);
        return classificationConfig;
    }
}
