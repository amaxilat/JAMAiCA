package eu.organicity.annotation.jamaica.www.model;


/**
 * Created by katdel on 30-May-16.
 */


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class ClassificationTrainData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long classificationConfigId;
    private String tag;
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClassificationConfigId() {
        return classificationConfigId;
    }

    public void setClassificationConfigId(long classificationConfigId) {
        this.classificationConfigId = classificationConfigId;
    }

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
}
