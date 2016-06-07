package eu.organicity.annotation.jamaica.www.service;

/**
 * Created by katdel on 06-Jun-16.
 */

import com.amaxilatis.orion.OrionClient;
import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class OrionService {

    private String serverUrl;
    private String token ;
    private String service ;
    private String servicePath;
    private OrionClient client;


    @PostConstruct
    public void init() {

        this.serverUrl = "http://195.220.224.30:1026/";
        this.token = "";
        this.service = "organicity";
        this.servicePath = "/";
        this.client = new OrionClient(this.serverUrl, this.token, this.service, this.servicePath);

    }

    public SubscriptionResponse subscribeToOrion(OrionEntity entity, String[] attributes, String reference, String[] conditions, String duration) throws IOException
    {
        return client.subscribeChange(entity,attributes, reference, conditions, duration);
    }

}
