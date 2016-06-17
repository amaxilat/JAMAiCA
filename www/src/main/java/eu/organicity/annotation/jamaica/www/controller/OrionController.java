package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.Attribute;
import com.amaxilatis.orion.model.OrionContextElement;
import com.amaxilatis.orion.model.OrionContextElementWrapper;
import com.amaxilatis.orion.model.SubscriptionUpdate;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.jubat.anomaly.AnomalyClient;

@Controller
public class OrionController extends BaseController {

    /**
     * a log4j logger to print messages.
     */


    protected static final Logger LOGGER = Logger.getLogger(OrionController.class);

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
    @RequestMapping(value = "/api/v1/notifyContext/{contextConnectionId}", method = RequestMethod.POST, produces = "application/json")
    SubscriptionUpdate notifyContext(@RequestBody final SubscriptionUpdate subscriptionUpdate, @PathVariable("contextConnectionId") String subscriptionId) {
        LOGGER.debug("[call] notifyContext");
        try {
            LOGGER.info(subscriptionUpdate);

            AnomalyConfig anomalyConfig;
            ClassifConfig classifConfig;


            for (final OrionContextElementWrapper wrapper : subscriptionUpdate.getContextResponses()) {

                final OrionContextElement element = wrapper.getContextElement();
                for (final Attribute contextElementAttribute : element.getAttributes()) {

                    if ((anomalyConfig = anomalyConfigRepository.findBySubscriptionId(subscriptionId)) != null) {
                        if (contextElementAttribute.getType().equals(anomalyConfig.getAttribute())) {
                            // start jubatus training for anomaly detection
                            final AnomalyClient client = new AnomalyClient(anomalyConfig.getJubatus_config(), anomalyConfig.getJubatusPort(), "test", 1);
                            jubatusService.calcScore(client, contextElementAttribute.getValue());
                        }

                        LOGGER.info(element.getId());
                        LOGGER.info(element.getType());
                        LOGGER.info(element.getIsPattern());
                        LOGGER.info(element.getAttributes());


                    } else if ((classifConfig = classifConfigRepository.findBySubscriptionId(subscriptionId)) != null) {


                    } else {

                        LOGGER.error("SubscriptionId: " + subscriptionId + " Not Found.");
                        return null;
                    }
                }
            }


        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return subscriptionUpdate;
    }
}
