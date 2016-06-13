package eu.organicity.annotation.jamaica.www.model;


/**
 * Created by katdel on 30-May-16.
 */


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class AnomalyConfig {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String type_pat;
    private String id_pat;
    private String tags;
    private String url_ext;
    private String url_orion;
    private int jubatus_port;
    private String jubatus_config;
    private String subscription_id;

    protected AnomalyConfig() {}

    public AnomalyConfig(String type_pat, String id_pat, String tags, String url_ext, String url_orion, int jubatus_port, String jubatus_config, String subscription_id) {
        this.type_pat = type_pat;
        this.id_pat = id_pat;
        this.tags = tags;
        this.url_ext = url_ext;
        this.url_orion = url_orion;
        this.jubatus_port = jubatus_port;
        this.jubatus_config = jubatus_config;
        this.subscription_id = subscription_id;

    }

    @Override
    public String toString() {
        return String.format(
                "AnomalyConfig[id=%d, type_pat='%s', id_pat='%s', tags='%s', url_ext='%s', url_orion='%s', jubatus_port='%d', jubatus_config='%s', subscription_id='%s']",
                id, type_pat, id_pat, tags, url_ext, url_orion, jubatus_port, jubatus_config,subscription_id);
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

    public String getTags(){
        return tags;
    }

    public String getUrl_ext(){
        return url_ext;
    }

    public String getUrl_orion(){
        return url_orion;
    }

    public int getJubatus_port(){
        return jubatus_port;
    }

    public String getSubscription_id(){
        return subscription_id;
    }
}
