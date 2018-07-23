package eu.organicity.annotation.jamaica.www;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebClassificationTrainDataDTO {
    private String tag;
    private String value;
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "WebClassificationTrainDataDTO{" + "tag='" + tag + '\'' + ", value='" + value + '\'' + '}';
    }
}
