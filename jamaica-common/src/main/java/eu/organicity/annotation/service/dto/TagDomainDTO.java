package eu.organicity.annotation.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDomainDTO {
    private long id;
    private String urn;
    private String description;
    private Set<TagDTO> tags;
    private ServiceDTO[] services;

    public TagDomainDTO() {
    }

    public TagDomainDTO(long id, String urn, String description, Set<TagDTO> tags) {
        this.id = id;
        this.urn = urn;
        this.description = description;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public ServiceDTO[] getServices() {
        return services;
    }

    public void setServices(ServiceDTO[] services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "TagDomainDTO{" +
                "id=" + id +
                ", urn='" + urn + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", services=" + Arrays.toString(services) +
                '}';
    }
}
