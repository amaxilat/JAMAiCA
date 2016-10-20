package eu.organicity.annotation.service.client;

import eu.organicity.annotation.service.dto.*;
import eu.organicity.client.OrganicityServiceBaseClient;
import eu.organicity.client.exception.UnauthorizedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

public class AnnotationServiceClient extends OrganicityServiceBaseClient {
    private static final String BASE_URL = "https://annotations.organicity.eu/";

    public AnnotationServiceClient(final String token) {
        super(token);
    }

    public TagDomainDTO[] getTagDomains() {
        return restTemplate.getForObject(BASE_URL + "tagDomains", TagDomainDTO[].class);
    }

    public TagDTO[] getTags(final String tagDomain) {
        return restTemplate.getForObject(BASE_URL + "tagDomains/" + tagDomain + "/tags", TagDTO[].class);
    }

    public TagDomainDTO getTagDomain(final String name) {
        return restTemplate.getForObject(BASE_URL + "tagDomains/" + name, TagDomainDTO.class);
    }


    public AnnotationDTO postAnnotation(final AnnotationDTO annotationDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnnotationDTO> entity = new HttpEntity<>(annotationDTO, headers);
        return restTemplate.exchange(BASE_URL + "annotations/" + annotationDTO.getAssetUrn(), HttpMethod.POST, entity, AnnotationDTO.class).getBody();
    }

    public TagDomainDTO addTagDomain(final String name, final String description) throws UnauthorizedException {
        final TagDomainDTO dto = new TagDomainDTO();
        dto.setUrn(name);
        dto.setDescription(description);
        return postTagDomain(dto);
    }

    public void removeTagDomain(final String urn) throws UnauthorizedException {
        final TagDomainDTO domain = getTagDomain(urn);
        if (domain.getTags() != null && !domain.getTags().isEmpty()) {
            for (final TagDTO tagDTO : domain.getTags()) {
                removeTag(urn, tagDTO.getUrn());
            }
        }
        deleteTagDomain(urn);
    }

    private TagDomainDTO postTagDomain(final TagDomainDTO tagDomainDTO) throws UnauthorizedException {
        HttpEntity<TagDomainDTO> entity = new HttpEntity<>(tagDomainDTO, headers);
        return restTemplate.exchange(BASE_URL + "admin/tagDomains", HttpMethod.POST, entity, TagDomainDTO.class).getBody();
    }

    private void deleteTagDomain(final String tagDomainUrn) throws UnauthorizedException {
        //https://annotations.organicity.eu/admin/tagDomains/urn:oc:tagDomain:AnomalyDetection:0
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/", HttpMethod.DELETE, entity, String.class);
    }

    public TagDTO addTag(final String tagDomain, final String urn, final String name) {
        final TagDTO dto = new TagDTO();
        dto.setUrn(urn);
        dto.setName(name);
        return postTag2TagDomain(tagDomain, dto);
    }

    public void removeTag(final String tagDomain, final String name) {
        System.out.println(name);
        HttpEntity<String> entity = new HttpEntity<>(name, headers);
        restTemplate.exchange(BASE_URL + "admin/tagDomains/" + tagDomain + "/tags", HttpMethod.DELETE, entity, String.class);
    }

    private TagDTO postTag2TagDomain(final String tagDomain, final TagDTO tagDTO) {
        HttpEntity<TagDTO> entity = new HttpEntity<>(tagDTO, headers);
        return restTemplate.exchange(BASE_URL + "admin/tagDomains/" + tagDomain + "/tags", HttpMethod.POST, entity, TagDTO.class).getBody();
    }

    public ServiceDTO[] tagDomainGetServices(final String tagDomainUrn) {
        return restTemplate.getForObject(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/services", ServiceDTO[].class);
    }

    public TagDomainDTO serviceAddTagDomains(final String tagDomainUrn, final String serviceUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/services");
        if (serviceUrn != null) {
            builder = builder.queryParam("serviceUrn", serviceUrn);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, TagDomainDTO.class);
    }

    public void serviceRemoveTagDomains(final String tagDomainUrn, final String serviceUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/tagDomains/" + tagDomainUrn + "/services");
        if (serviceUrn != null) {
            builder = builder.queryParam("serviceUrn", serviceUrn);
        }
        restTemplate.delete(builder.build().encode().toUri());
    }

    public ServiceDTO servicesCreate(final ServiceDTO serviceDTO) {
        return restTemplate.postForObject(BASE_URL + "admin/services", serviceDTO, ServiceDTO.class);
    }

    public void serviceDelete(final String applicationUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(BASE_URL + "admin/services/" + applicationUrn, HttpMethod.DELETE, entity, String.class);
    }

    public TagDomainDTO[] applicationGetTagDomains(final String applicationUrn) {
        return restTemplate.getForObject(BASE_URL + "admin/applications/" + applicationUrn + "/tagDomains", TagDomainDTO[].class);
    }

    public TagDomainDTO applicationAddTagDomains(final String tagDomainUrn, final String applicationUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/applications/" + applicationUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.postForObject(builder.build().encode().toUri(), entity, TagDomainDTO.class);
    }

    public void applicationRemoveTagDomains(final String tagDomainUrn, final String applicationUrn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "admin/applications/" + applicationUrn + "/tagDomains");
        if (tagDomainUrn != null) {
            builder = builder.queryParam("tagDomainUrn", tagDomainUrn);
        }
        restTemplate.delete(builder.build().encode().toUri());
    }

    public ApplicationDTO applicationsCreate(final ApplicationDTO applicationDTO) {
        return restTemplate.postForObject(BASE_URL + "admin/applications", applicationDTO, ApplicationDTO.class);
    }

    public void applicationDelete(final String applicationUrn) {
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        restTemplate.exchange(BASE_URL + "admin/applications/" + applicationUrn, HttpMethod.DELETE, entity, String.class);
    }
}
