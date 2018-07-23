package eu.organicity.annotation.jamaica.www;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebCreateClassificationDTO {
    private String typePat;
    private String idPat;
    private String attribute;
    private String tags;
    
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
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    @Override
    public String toString() {
        return "CreateClassificationDTO{" + "typePat='" + typePat + '\'' + ", idPat='" + idPat + '\'' + ", attribute='" + attribute + '\'' + ", tags='" + tags + '\'' + '}';
    }
}
