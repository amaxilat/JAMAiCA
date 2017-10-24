package eu.organicity.annotation.jamaica.dto;

public class ClassifConfigDTO {

    private long id;
    private String typePat;
    private String idPat;
    private String attribute;
    private String tagDomain;
    private String urlExt;
    private boolean enable;
    private String contextBrokerUrl;
    private String contextBrokerService;
    private String contextBrokerServicePath;
    private String user;

    public ClassifConfigDTO() {
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
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
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "ClassifConfigDTO{" + "id=" + id + '}';
    }
}
