package eu.organicity.annotation.jamaica.client;

import eu.organicity.annotation.jamaica.dto.AnomalyConfigDTO;
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

}
