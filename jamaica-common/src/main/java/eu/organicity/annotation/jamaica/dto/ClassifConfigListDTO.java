package eu.organicity.annotation.jamaica.dto;

import java.util.List;

public class ClassifConfigListDTO {
    private List<ClassifConfigDTO> classificationConfigurations;
    
    public List<ClassifConfigDTO> getClassificationConfigurations() {
        return classificationConfigurations;
    }
    
    public void setClassificationConfigurations(List<ClassifConfigDTO> classificationConfigurations) {
        this.classificationConfigurations = classificationConfigurations;
    }
}
