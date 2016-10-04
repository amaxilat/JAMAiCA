package eu.organicity.annotation.jamaica.www.utils;

/**
 * Created by katdel on 06-Jun-16.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by amaxilatis on 8/1/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reading {
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}