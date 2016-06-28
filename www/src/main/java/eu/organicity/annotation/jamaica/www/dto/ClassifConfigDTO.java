package eu.organicity.annotation.jamaica.www.dto;

import eu.organicity.annotation.jamaica.www.model.ClassifConfig;

public class ClassifConfigDTO {

    private long id;
    private String typePat;
    private String idPat;
    private String attribute;
    private String tagDomain;
    private String urlExt;

    public ClassifConfigDTO() {
    }

    public ClassifConfigDTO(final ClassifConfig config) {
        id = config.getId();
        typePat = config.getTypePat();
        idPat = config.getIdPat();
        attribute = config.getAttribute();
        tagDomain = config.getTags();
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

    public void setTypePat(String typePat) {
        this.typePat = typePat;
    }

    public String getIdPat() {
        return idPat;
    }

    public void setIdPat(String idPat) {
        this.idPat = idPat;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTagDomain() {
        return tagDomain;
    }

    public void setTagDomain(String tagDomain) {
        this.tagDomain = tagDomain;
    }

    public String getUrlExt() {
        return urlExt;
    }

    public void setUrlExt(String urlExt) {
        this.urlExt = urlExt;
    }
}
