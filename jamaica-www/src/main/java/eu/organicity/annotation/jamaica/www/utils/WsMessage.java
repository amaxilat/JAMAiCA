package eu.organicity.annotation.jamaica.www.utils;

public class WsMessage {
    private String assetUrn;
    private String tagUrn;
    private long classificationConfig;
    
    public void setAssetUrn(String assetUrn) {
        this.assetUrn = assetUrn;
    }
    
    public String getAssetUrn() {
        return assetUrn;
    }
    
    public void setTagUrn(String tagUrn) {
        this.tagUrn = tagUrn;
    }
    
    public String getTagUrn() {
        return tagUrn;
    }
    
    public void setClassificationConfig(long classificationConfig) {
        this.classificationConfig = classificationConfig;
    }
    
    public long getClassificationConfig() {
        return classificationConfig;
    }
}
