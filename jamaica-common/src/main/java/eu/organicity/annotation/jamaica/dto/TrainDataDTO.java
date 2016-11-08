package eu.organicity.annotation.jamaica.dto;

/**
 * Created by amaxilatis on 28/6/2016.
 */
public class TrainDataDTO {
    private String value;

    public TrainDataDTO() {
    }

    public TrainDataDTO(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TrainDataDTO{" +
                "value='" + value + '\'' +
                '}';
    }
}
