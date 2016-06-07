package eu.organicity.annotation.jamaica.www.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

/**
 * Created by amaxilatis on 8/1/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganicityData {

    Set<Reading> readings;

    public Set<Reading> getReadings() {
        return readings;
    }

    public void setReadings(Set<Reading> readings) {
        this.readings = readings;
    }
}