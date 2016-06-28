package eu.organicity.annotation.jamaica.www.dto;

/**
 * Created by amaxilatis on 28/6/2016.
 */
public class TrainDataDTO {
    private String value;

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
