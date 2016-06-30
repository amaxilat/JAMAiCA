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
public class AnomalyConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String typePat;
    private String idPat;
    private String attribute;
    private String tags;
    private String urlExt;
    private String urlOrion;
    private int jubatusPort;
    private String jubatusConfig;
    private String subscriptionId;
    private long lastSubscription;
    private boolean enable;

    public AnomalyConfig() {
    }

    public AnomalyConfig(String typePat, String idPat, String attribute, String tags, String urlExt, String urlOrion, int jubatusPort, String jubatusConfig, String subscriptionId, long lastSubscription, boolean enable) {
        this.typePat = typePat;
        this.idPat = idPat;
        this.attribute = attribute;
        this.tags = tags;
        this.urlExt = urlExt;
        this.urlOrion = urlOrion;
        this.jubatusPort = jubatusPort;
        this.jubatusConfig = jubatusConfig;
        this.subscriptionId = subscriptionId;
        this.lastSubscription = lastSubscription;
        this.enable = enable;
    }

    @Override
    public String toString() {
        return String.format(
                "AnomalyConfig[id=%d, typePat='%s', idPat='%s',attribute='%s', tags='%s', urlExt='%s', urlOrion='%s', jubatusPort='%d', jubatusConfig='%s', subscriptionId='%s']",
                id, typePat, idPat, attribute, tags, urlExt, urlOrion, jubatusPort, jubatusConfig, subscriptionId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJubatusConfig() {
        return jubatusConfig;
    }

    public String getTypePat() {
        return typePat;
    }

    public void setTypePat(String typePat) {
        this.typePat = typePat;
    }

    public String getIdPat() {
        return idPat;
    }

    public void setIdPat(String idPat) {
        this.idPat = idPat;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUrlExt() {
        return urlExt;
    }

    public String getUrlOrion() {
        return urlOrion;
    }

    public int getJubatusPort() {
        return jubatusPort;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public long getLastSubscription() {
        return lastSubscription;
    }

    public void setLastSubscription(long lastSubscription) {
        this.lastSubscription = lastSubscription;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnomalyConfig that = (AnomalyConfig) o;

        if (id != that.id) return false;
        if (!typePat.equals(that.typePat)) return false;
        if (!idPat.equals(that.idPat)) return false;
        return attribute.equals(that.attribute);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + typePat.hashCode();
        result = 31 * result + idPat.hashCode();
        result = 31 * result + attribute.hashCode();
        return result;
    }
}
