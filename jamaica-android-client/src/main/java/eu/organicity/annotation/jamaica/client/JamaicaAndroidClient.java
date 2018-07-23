package eu.organicity.annotation.jamaica.client;

import eu.organicity.annotation.jamaica.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigListDTO;
import eu.organicity.annotation.jamaica.dto.ClassifStatsDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.client.OrganicityServiceBaseClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class JamaicaAndroidClient extends OrganicityServiceBaseClient {
    
    private static final String BASE_URL = "http://jamaica.organicity.eu/v1/";
    private final String baseUrl;
    
    public JamaicaAndroidClient(final String token) {
        super(token);
        baseUrl = BASE_URL;
    }
    
    public JamaicaAndroidClient(final String token, final String baseUrl) {
        super(token);
        this.baseUrl = baseUrl;
    }
    
    public JamaicaAndroidClient(String client_id, String client_secret, String username, String password) {
        super(client_id, client_secret, username, password);
        this.baseUrl = BASE_URL;
    }
    
    public AnomalyConfigDTO getAnomalyConfig(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "config/anomaly/" + id, HttpMethod.GET, entity, AnomalyConfigDTO.class).getBody();
    }
    
    public void deleteAnomalyConfig(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(baseUrl + "config/anomaly/" + id, HttpMethod.DELETE, entity, AnomalyConfigDTO.class);
    }
    
    public AnomalyConfigDTO putAnomalyConfig(final AnomalyConfigDTO anomalyConfig) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + getToken());
        HttpEntity<AnomalyConfigDTO> entity = new HttpEntity<>(anomalyConfig, headers);
        return restTemplate.exchange(baseUrl + "config/anomaly/", HttpMethod.PUT, entity, AnomalyConfigDTO.class).getBody();
    }
    
    public ClassifConfigListDTO listClassificationConfig() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "config/classification", HttpMethod.GET, entity, ClassifConfigListDTO.class).getBody();
        
    }
    
    public ClassifConfigDTO getClassificationConfig(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "config/classification/" + id, HttpMethod.GET, entity, ClassifConfigDTO.class).getBody();
        
    }
    
    public void deleteClassificationConfig(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(baseUrl + "config/classification/" + id, HttpMethod.GET, entity, ClassifConfigDTO.class).getBody();
        
    }
    
    public ClassifStatsDTO getClassificationStats(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "config/classification/" + id + "/stats", HttpMethod.GET, entity, ClassifStatsDTO.class).getBody();
    }
    
    public ClassifConfigDTO subscribeClassificationConfig(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "config/classification/" + id + "/subscribe", HttpMethod.GET, entity, ClassifConfigDTO.class).getBody();
    }
    
    public AnomalyConfigDTO subscribeAnomalyConfig(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "config/anomaly/" + id + "/subscribe", HttpMethod.GET, entity, AnomalyConfigDTO.class).getBody();
    }
    
    public TrainDataListDTO trainClassificationConfig(final long id, final String tag, TrainDataListDTO dto) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity<TrainDataListDTO> entity = new HttpEntity<>(dto, headers);
        return restTemplate.exchange(baseUrl + "config/classification/" + id + "/" + tag + "/train", HttpMethod.POST, entity, TrainDataListDTO.class).getBody();
    }
    
    public String doTrainClassificationConfig(final long id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getToken());
        final HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "config/classification/" + id + "/train", HttpMethod.GET, entity, String.class).getBody();
    }
    
    public String classifyValue(final long id, final double value) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + getToken());
        TrainDataDTO dto = new TrainDataDTO(String.valueOf(value));
        HttpEntity<TrainDataDTO> entity = new HttpEntity<>(dto, headers);
        return restTemplate.exchange(baseUrl + "config/classify/" + id, HttpMethod.POST, entity, String.class).getBody();
    }
    
    public ClassifConfigDTO putClassificationConfig(ClassifConfigDTO classificationConfig) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + getToken());
        HttpEntity<ClassifConfigDTO> entity = new HttpEntity<>(classificationConfig, headers);
        return restTemplate.exchange(baseUrl + "config/classification/", HttpMethod.PUT, entity, ClassifConfigDTO.class).getBody();
    }
}
