package eu.organicity.annotation.service.client;

import eu.organicity.annotation.service.dto.AnnotationDTO;
import eu.organicity.annotation.service.dto.TagDTO;
import eu.organicity.annotation.service.dto.TagDomainDTO;
import eu.organicity.client.OrganicityServiceBaseClient;
import eu.organicity.client.exception.UnauthorizedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class AnnotationServiceClient extends OrganicityServiceBaseClient {
    private static final String BASE_URL = "https://annotations.organicity.eu/";

    public AnnotationServiceClient(final String token) {
        super(token);
    }

    public TagDomainDTO getTagDomains() {
        return restTemplate.getForObject(BASE_URL + "tagDomains", TagDomainDTO.class);
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

    private TagDomainDTO postTagDomain(final TagDomainDTO tagDomainDTO) throws UnauthorizedException {
        HttpEntity<TagDomainDTO> entity = new HttpEntity<>(tagDomainDTO, headers);
        return restTemplate.exchange(BASE_URL + "admin/tagDomains", HttpMethod.POST, entity, TagDomainDTO.class).getBody();
    }

    public TagDTO addTag(final String tagDomain, final String urn, final String name) {
        final TagDTO dto = new TagDTO();
        dto.setUrn(urn);
        dto.setName(name);
        return postTag2TagDomain(tagDomain, dto);
    }

    private TagDTO postTag2TagDomain(final String tagDomain, final TagDTO tagDTO) {
        HttpEntity<TagDTO> entity = new HttpEntity<>(tagDTO, headers);
        return restTemplate.exchange(BASE_URL + "admin/tagDomains/" + tagDomain + "/tags", HttpMethod.POST, entity, TagDTO.class).getBody();
    }

}
