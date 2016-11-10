package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.Attribute;
import com.amaxilatis.orion.model.OrionContextElementWrapper;
import com.amaxilatis.orion.model.SubscriptionUpdate;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.jubat.classifier.ClassifierClient;

import java.net.UnknownHostException;

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

        AnomalyConfig anomalyConfig = anomalyConfigRepository.findByUrlOrion(contextConnectionId);
        ClassifConfig classifConfig = classifConfigRepository.findByUrlOrion(contextConnectionId);

        try {
            if (anomalyConfig != null) {
                LOGGER.info("For contextConnectionId:" + contextConnectionId + " "
                        + "anomalyConfig:" + anomalyConfig.getId() + " attr:" + anomalyConfig.getAttribute()
                );
                if (anomalyConfig.isEnable()) {
                    checkAnomalyConfig(anomalyConfig, subscriptionUpdate);
                }
            } else if (classifConfig != null) {
                LOGGER.info("For contextConnectionId:" + contextConnectionId + " "
                        + "classifConfig:" + classifConfig.getId() + " attr:" + classifConfig.getAttribute()
                );
                if (classifConfig.isEnable()) {
                    checkClassifConfig(classifConfig, subscriptionUpdate);
                }
            } else {
                LOGGER.info("For contextConnectionId:" + contextConnectionId + " none");
            }
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return subscriptionUpdate;
    }

    private void checkAnomalyConfig(AnomalyConfig anomalyConfig, SubscriptionUpdate subscriptionUpdate) {
        for (final OrionContextElementWrapper wrapper : subscriptionUpdate.getContextResponses()) {
            for (final Attribute attribute : wrapper.getContextElement().getAttributes()) {
                if (anomalyConfig.getAttribute().equals(attribute.getType())
                        || anomalyConfig.getAttribute().equals(attribute.getName())) {
                    LOGGER.info("id:" + wrapper.getContextElement().getId() + " attribute:" + anomalyConfig.getAttribute() + " value:" + attribute.getValue());
                    try {
                    } catch (Exception e) {
                        LOGGER.error(e, e);
                    }
                }
            }
        }
    }

    private void checkClassifConfig(ClassifConfig classifConfig, SubscriptionUpdate subscriptionUpdate) {
        for (final OrionContextElementWrapper wrapper : subscriptionUpdate.getContextResponses()) {
            for (final Attribute attribute : wrapper.getContextElement().getAttributes()) {
                if (classifConfig.getAttribute().equals(attribute.getType())
                        || classifConfig.getAttribute().equals(attribute.getName())) {
                    LOGGER.info("id:" + wrapper.getContextElement().getId() + " attribute:" + attribute.getType() + " value:" + attribute.getValue());
                    try {
                        ClassifierClient client = new ClassifierClient(classifConfig.getJubatusConfig(), classifConfig.getJubatusPort(), "test", 1);
                        jubatusService.calcScore(client, attribute.getValue(),
                                wrapper.getContextElement().getId(), classifConfig.getAttribute(), classifConfig.getId());
                    } catch (UnknownHostException e) {
                        LOGGER.error(e, e);
                    }
                }
            }
        }
    }
}
