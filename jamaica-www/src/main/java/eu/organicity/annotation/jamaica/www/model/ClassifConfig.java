package eu.organicity.annotation.jamaica.www.model;


/**
 * Created by katdel on 03-Jun-16.
 */


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity
public class ClassifConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String typePat;
    private String idPat;
    private String attribute;
    private String tags;
    private String urlExt;
    private String urlOrion;
    private String contextBrokerUrl;
    private String contextBrokerService;
    private String contextBrokerServicePath;
    private String subscriptionId;
    private String user;
    private long lastSubscription;
    private boolean enable;
    
    
    protected ClassifConfig() {
    }
    
    public ClassifConfig(String typePat, String idPat, String attribute, String tags, String urlExt, String urlOrion, String subscriptionId, long lastSubscription, boolean enable) {
        this.typePat = typePat;
        this.idPat = idPat;
        this.attribute = attribute;
        this.tags = tags;
        this.urlExt = urlExt;
        this.urlOrion = urlOrion;
        this.subscriptionId = subscriptionId;
        this.lastSubscription = lastSubscription;
        this.enable = enable;
        this.contextBrokerUrl = contextBrokerUrl;
        this.contextBrokerService = contextBrokerService;
        this.contextBrokerServicePath = contextBrokerServicePath;
        
    }
}
