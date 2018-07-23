package eu.organicity.annotation.jamaica.www.model;


/**
 * Created by katdel on 30-May-16.
 */


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@ToString
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
    private String contextBrokerUrl;
    private String contextBrokerService;
    private String contextBrokerServicePath;
    private String jubatusConfig;
    private String subscriptionId;
    private String user;
    private long lastSubscription;
    private boolean enable;

    public AnomalyConfig() {
    }

    public AnomalyConfig(String typePat, String idPat, String attribute, String tags, String urlExt, String urlOrion,
                         int jubatusPort, String jubatusConfig, String subscriptionId, long lastSubscription,
                         boolean enable, String contextBrokerUrl, String contextBrokerService, String contextBrokerServicePath
    ) {
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
        this.contextBrokerUrl = contextBrokerUrl;
        this.contextBrokerService = contextBrokerService;
        this.contextBrokerServicePath = contextBrokerServicePath;
    }
}
