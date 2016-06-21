package eu.organicity.annotation.jamaica.www.dto;

import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;

public class AnomalyConfigDTO {

    private long id;
    private String typePat;
    private String idPat;
    private String attribute;
    private String tags;
    private String urlExt;

    public AnomalyConfigDTO() {
    }

    public AnomalyConfigDTO(final AnomalyConfig config) {
        id = config.getId();
        typePat = config.getTypePat();
        idPat = config.getIdPat();
        attribute = config.getAttribute();
        tags = config.getTags();
        urlExt = config.getUrlExt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypePat() {
        return typePat;
    }

    public void setTypePat(final String typePat) {
        this.typePat = typePat;
    }

    public String getIdPat() {
        return idPat;
    }

    public void setIdPat(final String idPat) {
        this.idPat = idPat;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(final String tags) {
        this.tags = tags;
    }

    public String getUrlExt() {
        return urlExt;
    }

    public void setUrlExt(final String urlExt) {
        this.urlExt = urlExt;
    }
}
