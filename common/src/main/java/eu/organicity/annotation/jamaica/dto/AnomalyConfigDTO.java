package eu.organicity.annotation.jamaica.dto;

public class AnomalyConfigDTO {

    private long id;
    private String typePat;
    private String idPat;
    private String attribute;
    private String tagDomain;
    private String urlExt;
    private String enable;
    private String contextBrokerUrl;
    private String contextBrokerService;
    private String contextBrokerServicePath;

    public AnomalyConfigDTO() {
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

    public String getTagDomain() {
        return tagDomain;
    }

    public void setTagDomain(final String tagDomain) {
        this.tagDomain = tagDomain;
    }

    public String getUrlExt() {
        return urlExt;
    }

    public void setUrlExt(final String urlExt) {
        this.urlExt = urlExt;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getContextBrokerUrl() {
        return contextBrokerUrl;
    }

    public void setContextBrokerUrl(String contextBrokerUrl) {
        this.contextBrokerUrl = contextBrokerUrl;
    }

    public String getContextBrokerService() {
        return contextBrokerService;
    }

    public void setContextBrokerService(String contextBrokerService) {
        this.contextBrokerService = contextBrokerService;
    }

    public String getContextBrokerServicePath() {
        return contextBrokerServicePath;
    }

    public void setContextBrokerServicePath(String contextBrokerServicePath) {
        this.contextBrokerServicePath = contextBrokerServicePath;
    }
}
