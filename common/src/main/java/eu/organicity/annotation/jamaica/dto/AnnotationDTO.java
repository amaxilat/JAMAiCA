package eu.organicity.annotation.jamaica.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnotationDTO {
    private Long annotationId;
    private String application;
    private String assetUrn;
    private String datetime;
    private double numericValue;
    private String tagUrn;
    private String textValue;
    private String user;

    public Long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(Long annotationId) {
        this.annotationId = annotationId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getAssetUrn() {
        return assetUrn;
    }

    public void setAssetUrn(String assetUrn) {
        this.assetUrn = assetUrn;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public double getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(double numericValue) {
        this.numericValue = numericValue;
    }

    public String getTagUrn() {
        return tagUrn;
    }

    public void setTagUrn(String tagUrn) {
        this.tagUrn = tagUrn;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
