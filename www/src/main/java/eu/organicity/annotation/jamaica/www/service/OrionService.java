package eu.organicity.annotation.jamaica.www.service;

/**
 * Created by katdel on 06-Jun-16.
 */

import com.amaxilatis.orion.OrionClient;
import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class OrionService {

    @Value("${orion.serverUrl}")
    private String serverUrl;

    @Value("${orion.token}")
    private String token;

    @Value("${orion.service}")
    private String service;

    @Value("${orion.servicePath}")
    private String servicePath;

    private OrionClient client;


    @PostConstruct
    public void init() {
        this.client = new OrionClient(this.serverUrl, this.token, this.service, this.servicePath);
    }

    /**
     * Creates a subscription to Orion for the provided parameters.
     *
     * @param entity     the {@see OrionEntity} used to define the selection parameters of the context subscription.
     * @param attributes the attributes we are interested in.
     * @param reference  the reference url for the subscription the Orion server will use for context notifications.
     * @param conditions the conditions that will trigger the context notifications.
     * @param duration   the duration for the context subscription.
     * @return
     * @throws IOException
     */
    public SubscriptionResponse subscribeToOrion(
            final OrionEntity entity, final String[] attributes, final String reference, final String[] conditions,
            final String duration) throws IOException {
        return client.subscribeChange(entity, attributes, reference, conditions, duration);
    }

}
