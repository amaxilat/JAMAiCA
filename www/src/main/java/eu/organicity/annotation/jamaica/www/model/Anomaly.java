package eu.organicity.annotation.jamaica.www.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Describes a detected Anomaly Entry.
 */
@Entity
public class Anomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String entityId;
    private String entityAttribute;
    private String attributeValue;
    private double score;
    private long anomalyConfigId;
    private long startTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityAttribute() {
        return entityAttribute;
    }

    public void setEntityAttribute(String entityAttribute) {
        this.entityAttribute = entityAttribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getAnomalyConfigId() {
        return anomalyConfigId;
    }

    public void setAnomalyConfigId(long anomalyConfigId) {
        this.anomalyConfigId = anomalyConfigId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
