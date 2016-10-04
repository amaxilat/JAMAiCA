package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.Attribute;
import com.amaxilatis.orion.model.OrionContextElement;
import com.amaxilatis.orion.model.OrionContextElementWrapper;
import com.amaxilatis.orion.model.SubscriptionUpdate;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.jubat.anomaly.AnomalyClient;

@Controller
public class ContextController extends BaseController {

    /**
     * a log4j logger to print messages.
     */


    protected static final Logger LOGGER = Logger.getLogger(ContextController.class);

    /**
     * A method that handles subscription updates from Orion or users and starts the data validation against Jubatus.
     * <p>
     * TODO : find the attributes of interest.
     * TODO : find the concerning subscription.
     * TODO : forward the request to Jubatus using Jubatus Service - should be ASYNC.
     *
     * @param subscriptionUpdate the {@see SubscriptionUpdate} received from the Orion Context Broker.
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/v1/notifyContext/{contextConnectionId}", "/api/v1/notifyContext/{contextConnectionId}"}, method = RequestMethod.POST, produces = APPLICATION_JSON)
    SubscriptionUpdate notifyContext(@RequestBody final SubscriptionUpdate subscriptionUpdate, @PathVariable("contextConnectionId") String contextConnectionId) {
        LOGGER.debug("[call] notifyContext " + subscriptionUpdate.getSubscriptionId());
        try {
            final String subscriptionId = subscriptionUpdate.getSubscriptionId();

            AnomalyConfig anomalyConfig;
            ClassifConfig classifConfig;


            for (final OrionContextElementWrapper wrapper : subscriptionUpdate.getContextResponses()) {

                final OrionContextElement element = wrapper.getContextElement();
                for (final Attribute contextElementAttribute : element.getAttributes()) {
                    anomalyConfig = anomalyConfigRepository.findBySubscriptionId(subscriptionId);
                    if (anomalyConfig == null) {
                        anomalyConfig = anomalyConfigRepository.findByUrlOrion(contextConnectionId);
                    }
                    if (anomalyConfig != null) {
                        if (contextElementAttribute.getType().equals(anomalyConfig.getAttribute())) {
                            // start jubatus training for anomaly detection if anomaly config enable is true
                            if (anomalyConfig.isEnable()) {
                                LOGGER.info(element.getId() + " value:" + contextElementAttribute.getValue());
                                final AnomalyClient client = new AnomalyClient(anomalyConfig.getJubatusConfig(), anomalyConfig.getJubatusPort(), "test", 1);
                                jubatusService.calcScore(client, contextElementAttribute.getValue(), element.getId(), contextElementAttribute.getType(), anomalyConfig.getId());
                            } else {
                                LOGGER.warn("SubscriptionId: " + subscriptionId + " Disabled.");
                            }
                        }
                    } else if ((classifConfig = classifConfigRepository.findBySubscriptionId(subscriptionId)) != null) {


                    } else {
                        LOGGER.error("SubscriptionId: " + subscriptionId + " Not Found.");
                    }
                }
            }


        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return subscriptionUpdate;
    }
}
