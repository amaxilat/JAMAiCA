package eu.organicity.annotation.jamaica.dto;

public class ClassifStatsDTO {
    
    private ClassifConfigDTO classifConfigDTO;
    private Integer trainDataEntries;
    private Integer classifications;
    
    public ClassifStatsDTO() {
    }
    
    public ClassifConfigDTO getClassifConfigDTO() {
        return classifConfigDTO;
    }
    
    public void setClassifConfigDTO(ClassifConfigDTO classifConfigDTO) {
        this.classifConfigDTO = classifConfigDTO;
    }
    
    public Integer getTrainDataEntries() {
        return trainDataEntries;
    }
    
    public void setTrainDataEntries(Integer trainDataEntries) {
        this.trainDataEntries = trainDataEntries;
    }
    
    public Integer getClassifications() {
        return classifications;
    }
    
    public void setClassifications(Integer classifications) {
        this.classifications = classifications;
    }
    
    @Override
    public String toString() {
        return "ClassifStatsDTO{" + "classifConfigDTO=" + classifConfigDTO + ", trainDataEntries=" + trainDataEntries + ", classifications=" + classifications + '}';
    }
}
