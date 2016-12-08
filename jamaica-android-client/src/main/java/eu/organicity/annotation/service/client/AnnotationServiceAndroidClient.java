package eu.organicity.annotation.service.client;

import eu.organicity.annotation.service.dto.*;
import eu.organicity.client.OrganicityServiceBaseClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Helper Client implementation for the OrganiCity Annotation Service.
 * This client implements the API described here:
 * https://annotations.organicity.eu/swagger-ui.html
 *
 * @author amaxilat@cti.gr
 */
public class AnnotationServiceAndroidClient extends OrganicityServiceBaseClient {
    private String baseUrl = "https://annotations.organicity.eu/";

    public AnnotationServiceAndroidClient(final String token) {
        super(token);
    }

    public AnnotationServiceAndroidClient(final String token, final String baseUrl) {
        super(token);
        this.baseUrl = baseUrl;
    }

    //Get Methods

    /**
     * List all available {@see TagDomainDTO}
     *
     * @return an array of {@see TagDomainDTO} objects
     */
    public TagDomainDTO[] getTagDomains() {
        return restTemplate.getForObject(baseUrl + "tagDomains", TagDomainDTO[].class);
    }

    public TagDomainDTO[] getTagDomainsOfExperiment(String experimentUrn) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(baseUrl + "experiments/" + experimentUrn + "/tagDomains", HttpMethod.GET, entity, TagDomainDTO[].class).getBody();
    }

    /**
     * Retrieves all information for a given {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @return the {@see TagDomainDTO}
     */
    public TagDomainDTO getTagDomain(final String tagDomainUrn) {
        return restTemplate.getForObject(baseUrl + "tagDomains/" + tagDomainUrn, TagDomainDTO.class);
    }

    /**
     * Lists all {@see TagDTO} elements of a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @return an array of {@see TagDTO}
     */
    public TagDTO[] getTags(final String tagDomainUrn) {
        return restTemplate.getForObject(baseUrl + "tagDomains/" + tagDomainUrn + "/tags", TagDTO[].class);
    }

    /**
     * List all {@see ServiceDTO} of a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @return an array of {@see ServiceDTO}
     */
    public ServiceDTO[] tagDomainGetServices(final String tagDomainUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services", HttpMethod.GET, entity, ServiceDTO[].class).getBody();
    }

    /**
     * List all {@see TagDomainDTO} of a {@see ExperimentDTO}
     *
     * @param experimentUrn the urn of the {@see ExperimentDTO}
     * @return an array of {@see TagDomainDTO}
     */
    public TagDomainDTO[] experimentGetTagDomains(final String experimentUrn) {
        return restTemplate.getForObject(baseUrl + "admin/experiments/" + experimentUrn + "/tagDomains", TagDomainDTO[].class);
    }

    //Add Methods

    /**
     * Adds an {@see AnnotationDTO}
     *
     * @param annotationDTO the {@see AnnotationDTO} to add
     * @return the added {@see AnnotationDTO}
     */
    public AnnotationDTO postAnnotation(final AnnotationDTO annotationDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnnotationDTO> entity = new HttpEntity<>(annotationDTO, headers);
        return restTemplate.exchange(baseUrl + "annotations/" + annotationDTO.getAssetUrn(), HttpMethod.POST, entity, AnnotationDTO.class).getBody();
    }

    /**
     * Adds a {@see TagDomainDTO}
     *
     * @param urn         the {@see TagDomainDTO} urn
     * @param description a text description  for the {@see TagDomainDTO}
     * @return the added {@see TagDomainDTO}
     */
    public TagDomainDTO addTagDomain(final String urn, final String description) {
        final TagDomainDTO dto = new TagDomainDTO();
        dto.setUrn(urn);
        dto.setDescription(description);
        return postTagDomain(dto);
    }

    /**
     * Adds a {@see TagDomainDTO}
     *
     * @param urn         the {@see TagDomainDTO} urn
     * @param description a text description  for the {@see TagDomainDTO}
     * @return the added {@see TagDomainDTO}
     */
    public ExperimentDTO addExperiment(final String urn, final String description) {
        final ExperimentDTO dto = new ExperimentDTO();
        dto.setUrn(urn);
        dto.setDescription(description);
        return postExperiment(dto);
    }

    /**
     * Adds a {@see TagDTO} to a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @param urn          the urn of the {@see TagDTO} to add
     * @param name         a text name for the {@see TagDTO}
     * @return the added {@see TagDTO}
     */
    public TagDTO addTag(final String tagDomainUrn, final String urn, final String name) {
        final TagDTO dto = new TagDTO();
        dto.setUrn(urn);
        dto.setName(name);
        return postTag2TagDomain(tagDomainUrn, dto);
    }

    /**
     * Creates a new {@see ServiceDTO}
     *
     * @param serviceDTO the {@see ServiceDTO} to add
     * @return the added {@see ServiceDTO}
     */
    public ServiceDTO servicesCreate(final ServiceDTO serviceDTO) {
        HttpEntity<ServiceDTO> entity = new HttpEntity<>(serviceDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/services", HttpMethod.POST, entity, ServiceDTO.class).getBody();
    }

    /**
     * Adds a {@see ServiceDTO} to a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @param serviceUrn   the urn of the {@see ServiceDTO}
     * @return the {@see TagDomainDTO}
     */
    public TagDomainDTO serviceAddTagDomains(final String tagDomainUrn, final String serviceUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services");
        if (serviceUrn != null) {
            builder = builder.queryParam("serviceUrn", serviceUrn);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, TagDomainDTO.class);
    }

    /**
     * Adds a new {@see ExperimentDTO}
     *
     * @param experimentDTO the {@see ExperimentDTO} to add
     * @return the added {@see ExperimentDTO}
     */
    public ExperimentDTO experimentCreate(final ExperimentDTO experimentDTO) {
        return restTemplate.postForObject(baseUrl + "admin/applications", experimentDTO, ExperimentDTO.class);
    }

    /**
     * Adds new {@see ExperimentDTO}
     *
     * @param tagDomainUrn  the urn of the {@see TagDomainDTO}
     * @param experimentUrn the urn of the {@see ExperimentDTO}
     * @return the updated {@see ExperimentDTO}
     */
    public ExperimentDTO experimentAddTagDomains(final String tagDomainUrn, final String experimentUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/experiments/" + experimentUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, ExperimentDTO.class);
    }

    //Remove Methods

    /**
     * Deletes a {@see TagDomainDTO} and all the {@see TagDTO} it contains.
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     */
    public void removeTagDomain(final String tagDomainUrn) {
        final TagDomainDTO domain = getTagDomain(tagDomainUrn);

        if (domain == null) {
            return;
        }

        if (domain.getTags() != null && !domain.getTags().isEmpty()) {
            for (final TagDTO tagDTO : domain.getTags()) {
                removeTag(tagDomainUrn, tagDTO.getUrn());
            }
        }
        deleteTagDomain(tagDomainUrn);
    }

    /**
     * Removes a {@see TagDTO} from a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @param tagUrn       the urn of the {@see TagDTO}
     */
    public void removeTag(final String tagDomainUrn, final String tagUrn) {
        HttpEntity<String> entity = new HttpEntity<>(tagUrn, headers);
        restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/tags", HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Deletes a {@see ServiceDTO}
     *
     * @param serviceUrn the urn of the {@see ServiceDTO}
     */
    public void serviceDelete(final String serviceUrn) {
        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(baseUrl + "admin/services/" + serviceUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Removes a {@see TagDomainDTO} from a {@see ServiceDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @param serviceUrn   the urn of the {@see ServiceDTO}
     */
    public void serviceRemoveTagDomains(final String tagDomainUrn, final String serviceUrn) {
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services");
//        if (serviceUrn != null) {
//            builder = builder.queryParam("serviceUrn", serviceUrn);
//        }

        HttpEntity entity = new HttpEntity(headers);
        restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/services?serviceUrn=" + serviceUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Deletes an {@see ExperimentDTO}
     *
     * @param applicationUrn the urn of the {@see ExperimentDTO}
     */
    public void experimentDelete(final String applicationUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(baseUrl + "admin/applications/" + applicationUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Removes a {@see TagDomainDTO} from the {@see ExperimentDTO}
     *
     * @param tagDomainUrn  the urn of the {@see TagDomainDTO}
     * @param experimentUrn the urn of the {@see ExperimentDTO}
     */
    public void experimentRemoveTagDomains(final String tagDomainUrn, final String experimentUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "admin/experiments/" + experimentUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        restTemplate.delete(builder.build().encode().toUri());
    }

    //internal calls

    private TagDomainDTO postTagDomain(final TagDomainDTO tagDomainDTO) {
        HttpEntity<TagDomainDTO> entity = new HttpEntity<>(tagDomainDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/tagDomains", HttpMethod.POST, entity, TagDomainDTO.class).getBody();
    }

    private ExperimentDTO postExperiment(final ExperimentDTO experimentDTO) {
        HttpEntity<ExperimentDTO> entity = new HttpEntity<>(experimentDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/experiments", HttpMethod.POST, entity, ExperimentDTO.class).getBody();
    }

    private void deleteTagDomain(final String tagDomainUrn) {
        //https://annotations.organicity.eu/admin/tagDomains/urn:oc:tagDomain:AnomalyDetection:0
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomainUrn + "/", HttpMethod.DELETE, entity, String.class);
    }

    private TagDTO postTag2TagDomain(final String tagDomain, final TagDTO tagDTO) {
        HttpEntity<TagDTO> entity = new HttpEntity<>(tagDTO, headers);
        return restTemplate.exchange(baseUrl + "admin/tagDomains/" + tagDomain + "/tag", HttpMethod.POST, entity, TagDTO.class).getBody();
    }


}
