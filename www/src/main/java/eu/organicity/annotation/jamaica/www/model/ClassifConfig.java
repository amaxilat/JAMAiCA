package eu.organicity.annotation.jamaica.www.model;


/**
 * Created by katdel on 03-Jun-16.
 */


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClassifConfig {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String type_pat;
    private String id_pat;
    private String attribute;
    private String tags;
    private String url_ext;
    private String url_orion;
    private int jubatusPort;
    private String jubatus_config;
    private String subscriptionId;

    protected ClassifConfig() {}

    public ClassifConfig(String type_pat, String id_pat, String attribute, String tags, String url_ext, String url_orion, int jubatusPort, String jubatus_config, String subscriptionId) {
        this.type_pat = type_pat;
        this.id_pat = id_pat;
        this.attribute = attribute;
        this.tags = tags;
        this.url_ext = url_ext;
        this.url_orion = url_orion;
        this.jubatusPort = jubatusPort;
        this.jubatus_config = jubatus_config;
        this.subscriptionId = subscriptionId;

    }

    @Override
    public String toString() {
        return String.format(
                "ClassifConfig[id=%d, type_pat='%s', id_pat='%s', attribute='%s', tags='%s', url_ext='%s', url_orion='%s', jubatusPort='%d', jubatus_config='%s', subscriptionId='%s']",
                id, type_pat, id_pat, attribute, tags, url_ext, url_orion, jubatusPort, jubatus_config, subscriptionId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJubatus_config(){
        return jubatus_config;
    }

    public String getType_pat(){
        return type_pat;
    }

    public String getId_pat(){
        return id_pat;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTags(){
        return tags;
    }

    public String getUrl_ext(){
        return url_ext;
    }

    public String getUrl_orion(){
        return url_orion;
    }

    public int getJubatusPort(){
        return jubatusPort;
    }

    public String getSubscriptionId(){
        return subscriptionId;
    }

}
