package eu.organicity.annotation.jamaica.client;

import eu.organicity.annotation.jamaica.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.client.OrganicityServiceBaseClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.logging.Logger;

public class JamaicaClient extends OrganicityServiceBaseClient {
    private static final Logger LOGGER = Logger.getLogger(JamaicaClient.class.getName());

    private static final String BASE_URL = "http://jamaica.organicity.eu/v1/";

    public JamaicaClient(final String token) {
        super(token);
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

    public ClassifConfigDTO subscribeClassificationConfig(final long id) {
        return restTemplate.getForObject(BASE_URL + "config/classification/" + id + "/subscribe", ClassifConfigDTO.class);
    }

    public AnomalyConfigDTO subscribeAnomalyConfig(final long id) {
        return restTemplate.getForObject(BASE_URL + "config/anomaly/" + id + "/subscribe", AnomalyConfigDTO.class);
    }

    public TrainDataListDTO trainClassificationConfig(final long id, final String tag, TrainDataListDTO dto) {
        return restTemplate.postForObject(BASE_URL + "config/classification/" + id + "/" + tag + "/train", dto, TrainDataListDTO.class);
    }

    public String doTrainClassificationConfig(final long id) {
        return restTemplate.getForObject(BASE_URL + "config/classification/" + id + "/train", String.class);
    }

    public ClassifConfigDTO putClassificationConfig(ClassifConfigDTO classificationConfig) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClassifConfigDTO> entity = new HttpEntity<>(classificationConfig, headers);
        return restTemplate.exchange(BASE_URL + "config/classification/", HttpMethod.PUT, entity, ClassifConfigDTO.class).getBody();
    }
}
