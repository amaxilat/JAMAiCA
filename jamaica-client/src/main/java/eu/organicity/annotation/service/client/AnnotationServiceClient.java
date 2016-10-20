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
public class AnnotationServiceClient extends OrganicityServiceBaseClient {
    private static final String BASE_URL = "https://annotations.organicity.eu/";

    public AnnotationServiceClient(final String token) {
        super(token);
    }

    //Get Methods

    /**
     * List all available {@see TagDomainDTO}
     *
     * @return an array of {@see TagDomainDTO} objects
     */
    public TagDomainDTO[] getTagDomains() {
        return restTemplate.getForObject(BASE_URL + "tagDomains", TagDomainDTO[].class);
    }

    /**
     * Retrieves all information for a given {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @return the {@see TagDomainDTO}
     */
    public TagDomainDTO getTagDomain(final String tagDomainUrn) {
        return restTemplate.getForObject(BASE_URL + "tagDomains/" + tagDomainUrn, TagDomainDTO.class);
    }

    /**
     * Lists all {@see TagDTO} elements of a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @return an array of {@see TagDTO}
     */
    public TagDTO[] getTags(final String tagDomainUrn) {
        return restTemplate.getForObject(BASE_URL + "tagDomains/" + tagDomainUrn + "/tags", TagDTO[].class);
    }

    /**
     * List all {@see ServiceDTO} of a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @return an array of {@see ServiceDTO}
     */
    public ServiceDTO[] tagDomainGetServices(final String tagDomainUrn) {
        return restTemplate.getForObject(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/services", ServiceDTO[].class);
    }

    /**
     * List all {@see TagDomainDTO} of a {@see ApplicationDTO}
     *
     * @param applicationUrn the urn of the {@see ApplicationDTO}
     * @return an array of {@see TagDomainDTO}
     */
    public TagDomainDTO[] applicationGetTagDomains(final String applicationUrn) {
        return restTemplate.getForObject(BASE_URL + "admin/applications/" + applicationUrn + "/tagDomains", TagDomainDTO[].class);
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
        return restTemplate.exchange(BASE_URL + "annotations/" + annotationDTO.getAssetUrn(), HttpMethod.POST, entity, AnnotationDTO.class).getBody();
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
        return restTemplate.postForObject(BASE_URL + "admin/services", serviceDTO, ServiceDTO.class);
    }

    /**
     * Adds a {@see ServiceDTO} to a {@see TagDomainDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @param serviceUrn   the urn of the {@see ServiceDTO}
     * @return the {@see TagDomainDTO}
     */
    public TagDomainDTO serviceAddTagDomains(final String tagDomainUrn, final String serviceUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/services");
        if (serviceUrn != null) {
            builder = builder.queryParam("serviceUrn", serviceUrn);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, TagDomainDTO.class);
    }

    /**
     * Adds a new {@see ApplicationDTO}
     *
     * @param applicationDTO the {@see ApplicationDTO} to add
     * @return the added {@see ApplicationDTO}
     */
    public ApplicationDTO applicationsCreate(final ApplicationDTO applicationDTO) {
        return restTemplate.postForObject(BASE_URL + "admin/applications", applicationDTO, ApplicationDTO.class);
    }

    /**
     * Adds new {@see ApplicationDTO}
     *
     * @param tagDomainUrn   the urn of the {@see TagDomainDTO}
     * @param applicationUrn the urn of the {@see ApplicationDTO}
     * @return the updated {@see ApplicationDTO}
     */
    public ApplicationDTO applicationAddTagDomains(final String tagDomainUrn, final String applicationUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/applications/" + applicationUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, ApplicationDTO.class);
    }

    //Remove Methods

    /**
     * Deletes a {@see TagDomainDTO} and all the {@see TagDTO} it contains.
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     */
    public void removeTagDomain(final String tagDomainUrn) {
        final TagDomainDTO domain = getTagDomain(tagDomainUrn);
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
        restTemplate.exchange(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/tags", HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Deletes a {@see ServiceDTO}
     *
     * @param serviceUrn the urn of the {@see ServiceDTO}
     */
    public void serviceDelete(final String serviceUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(BASE_URL + "admin/services/" + serviceUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Removes a {@see TagDomainDTO} from a {@see ServiceDTO}
     *
     * @param tagDomainUrn the urn of the {@see TagDomainDTO}
     * @param serviceUrn   the urn of the {@see ServiceDTO}
     */
    public void serviceRemoveTagDomains(final String tagDomainUrn, final String serviceUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/services");
        if (serviceUrn != null) {
            builder = builder.queryParam("serviceUrn", serviceUrn);
        }
        restTemplate.delete(builder.build().encode().toUri());
    }

    /**
     * Deletes an {@see ApplicationDTO}
     *
     * @param applicationUrn the urn of the {@see ApplicationDTO}
     */
    public void applicationDelete(final String applicationUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(BASE_URL + "admin/applications/" + applicationUrn, HttpMethod.DELETE, entity, String.class);
    }

    /**
     * Removes a {@see TagDomainDTO} from the {@see ApplicationDTO}
     *
     * @param tagDomainUrn   the urn of the {@see TagDomainDTO}
     * @param applicationUrn the urn of the {@see ApplicationDTO}
     */
    public void applicationRemoveTagDomains(final String tagDomainUrn, final String applicationUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/applications/" + applicationUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        restTemplate.delete(builder.build().encode().toUri());
    }

    //internal calls

    private TagDomainDTO postTagDomain(final TagDomainDTO tagDomainDTO) {
        HttpEntity<TagDomainDTO> entity = new HttpEntity<>(tagDomainDTO, headers);
        return restTemplate.exchange(BASE_URL + "admin/tagDomains", HttpMethod.POST, entity, TagDomainDTO.class).getBody();
    }

    private void deleteTagDomain(final String tagDomainUrn) {
        //https://annotations.organicity.eu/admin/tagDomains/urn:oc:tagDomain:AnomalyDetection:0
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/", HttpMethod.DELETE, entity, String.class);
    }

    private TagDTO postTag2TagDomain(final String tagDomain, final TagDTO tagDTO) {
        HttpEntity<TagDTO> entity = new HttpEntity<>(tagDTO, headers);
        return restTemplate.exchange(BASE_URL + "admin/tagDomains/" + tagDomain + "/tags", HttpMethod.POST, entity, TagDTO.class).getBody();
    }
}
