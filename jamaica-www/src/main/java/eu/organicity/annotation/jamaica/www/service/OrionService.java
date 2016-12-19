package eu.organicity.annotation.jamaica.www.service;

/**
 * Created by katdel on 06-Jun-16.
 */

import com.amaxilatis.orion.OrionClient;
import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class OrionService {

    @Value("${orion.serverUrl}")
    private String contextBrokerUrl;

    @Value("${orion.token}")
    private String token;

    @Value("${orion.service}")
    private String service;

    @Value("${orion.servicePath}")
    private String servicePath;

    private OrionClient client;


    @PostConstruct
    public void init() {
        this.client = new OrionClient(this.contextBrokerUrl, this.token, this.service, this.servicePath);
    }

    /**
     * Creates a subscription to Orion for the provided parameters.
     *
     * @param entity        the {@link OrionEntity} used to define the selection parameters of the context subscription.
     * @param attributes    the attributes we are interested in.
     * @param reference     the reference url for the subscription the Orion server will use for context notifications.
     * @param conditions    the conditions that will trigger the context notifications.
     * @param duration      the duration for the context subscription.
     * @return
     */
    public SubscriptionResponse subscribeToOrion(
            final OrionEntity entity, final String[] attributes, final String reference, final String[] conditions,
            final String duration, AnomalyConfig anomalyConfig) throws IOException {
        if (contextBrokerUrl.equals(anomalyConfig.getContextBrokerUrl())) {
            return subscribeToOrion(entity, attributes, reference, conditions, duration, client);
        } else {
            final OrionClient newClient = new OrionClient(anomalyConfig.getContextBrokerUrl(),
                    "", anomalyConfig.getContextBrokerService(), anomalyConfig.getContextBrokerServicePath());
            return subscribeToOrion(entity, attributes, reference, conditions, duration, newClient);
        }
    }

    /**
     * Creates a subscription to Orion for the provided parameters.
     *
     * @param entity        the {@link OrionEntity} used to define the selection parameters of the context subscription.
     * @param attributes    the attributes we are interested in.
     * @param reference     the reference url for the subscription the Orion server will use for context notifications.
     * @param conditions    the conditions that will trigger the context notifications.
     * @param duration      the duration for the context subscription.
     * @return
     */
    public SubscriptionResponse subscribeToOrion(
            final OrionEntity entity, final String[] attributes, final String reference, final String[] conditions,
            final String duration, ClassifConfig anomalyConfig) throws IOException {
        return subscribeToOrion(entity, attributes, reference, conditions, duration, client);
    }

    /**
     * Creates a subscription to Orion for the provided parameters.
     *
     * @param entity     the {@link OrionEntity} used to define the selection parameters of the context subscription.
     * @param attributes the attributes we are interested in.
     * @param reference  the reference url for the subscription the Orion server will use for context notifications.
     * @param conditions the conditions that will trigger the context notifications.
     * @param duration   the duration for the context subscription.
     * @param client     the orion client to use to subscribe.
     * @return
     */
    public SubscriptionResponse subscribeToOrion(
            final OrionEntity entity, final String[] attributes, final String reference, final String[] conditions,
            final String duration, OrionClient client) throws IOException {
        return client.subscribeChange(entity, attributes, reference, conditions, duration);
    }

    public String getContextBrokerUrl() {
        return contextBrokerUrl;
    }

    public String getContextBrokerService() {
        return service;
    }

    public String getContextBrokerServicePath() {
        return servicePath;
    }
}
