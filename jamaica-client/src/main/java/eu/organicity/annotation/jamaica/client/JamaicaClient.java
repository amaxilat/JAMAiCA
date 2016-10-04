package eu.organicity.annotation.jamaica.client;

import eu.organicity.annotation.jamaica.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

    public void deleteAnomalyConfig(final long id) {
        restTemplate.delete(BASE_URL + "config/anomaly/" + id, AnomalyConfigDTO.class);
    }

    public AnomalyConfigDTO putAnomalyConfig(final AnomalyConfigDTO anomalyConfig) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnomalyConfigDTO> entity = new HttpEntity<>(anomalyConfig, headers);
        return restTemplate.exchange(BASE_URL + "config/anomaly/", HttpMethod.PUT, entity, AnomalyConfigDTO.class).getBody();
    }

    public ClassifConfigDTO getClassificationConfig(final long id) {
        return restTemplate.getForObject(BASE_URL + "config/classification/" + id, ClassifConfigDTO.class);

    }

    public void deleteClassificationConfig(final long id) {
        restTemplate.delete(BASE_URL + "config/classification/" + id, ClassifConfigDTO.class);

    }

    public ClassifConfigDTO putClassificationConfig(ClassifConfigDTO classificationConfig) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClassifConfigDTO> entity = new HttpEntity<>(classificationConfig, headers);
        return restTemplate.exchange(BASE_URL + "config/classification/", HttpMethod.PUT, entity, ClassifConfigDTO.class).getBody();
    }
}
