package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.Attribute;
import com.amaxilatis.orion.model.OrionContextElementWrapper;
import com.amaxilatis.orion.model.SubscriptionUpdate;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContextController extends BaseController {
    
    /**
     * a log4j logger to print messages.
     */
    
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(ContextController.class);
    
    /**
     * A method that handles subscription updates from Orion or users and starts the data validation against Jubatus.
     * <p>
     * TODO : find the attributes of interest.
     * TODO : find the concerning subscription.
     * TODO : forward the request to Jubatus using Jubatus Service - should be ASYNC.
     *
     * @param subscriptionUpdate the {@link SubscriptionUpdate} received from the Orion Context Broker.
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/v1/notifyContext/{contextConnectionId}", "/api/v1/notifyContext/{contextConnectionId}"}, method = RequestMethod.POST, produces = APPLICATION_JSON)
    SubscriptionUpdate notifyContext(@RequestBody final SubscriptionUpdate subscriptionUpdate, @PathVariable("contextConnectionId") String contextConnectionId) {
        LOGGER.info("[call] notifyContext " + subscriptionUpdate.getSubscriptionId());
        
        AnomalyConfig anomalyConfig = anomalyConfigRepository.findByUrlOrion(contextConnectionId);
        ClassifConfig classifConfig = classifConfigRepository.findByUrlOrion(contextConnectionId);
        
        try {
            if (anomalyConfig != null) {
                LOGGER.info("For contextConnectionId:" + contextConnectionId + " " + "anomalyConfig:" + anomalyConfig.getId() + " attr:" + anomalyConfig.getAttribute());
                if (anomalyConfig.isEnable()) {
                    checkAnomalyConfig(anomalyConfig, subscriptionUpdate);
                }
            } else if (classifConfig != null) {
                LOGGER.info("For contextConnectionId:" + contextConnectionId + " " + "classifConfig:" + classifConfig.getId() + " attr:" + classifConfig.getAttribute());
                if (classifConfig.isEnable()) {
                    checkClassifConfig(classifConfig, subscriptionUpdate);
                }
            } else {
                LOGGER.info("For contextConnectionId:" + contextConnectionId + " none");
            }
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        return subscriptionUpdate;
    }
    
    private void checkAnomalyConfig(AnomalyConfig anomalyConfig, SubscriptionUpdate subscriptionUpdate) {
        for (final OrionContextElementWrapper wrapper : subscriptionUpdate.getContextResponses()) {
            for (final Attribute attribute : wrapper.getContextElement().getAttributes()) {
                if (anomalyConfig.getAttribute().equals(attribute.getType()) || anomalyConfig.getAttribute().equals(attribute.getName())) {
                    LOGGER.info("id:" + wrapper.getContextElement().getId() + " attribute:" + anomalyConfig.getAttribute() + " value:" + attribute.getValue());
                    try {
                    } catch (Exception e) {
                        LOGGER.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        }
    }
    
    private void checkClassifConfig(ClassifConfig classifConfig, SubscriptionUpdate subscriptionUpdate) {
        for (final OrionContextElementWrapper wrapper : subscriptionUpdate.getContextResponses()) {
            for (final Attribute attribute : wrapper.getContextElement().getAttributes()) {
                if (classifConfig.getAttribute().equals(attribute.getType()) || classifConfig.getAttribute().equals(attribute.getName())) {
                    LOGGER.debug("received [id:" + wrapper.getContextElement().getId() + " attribute:" + attribute.getType() + " value:" + attribute.getValue() + "]");
                    try {
                        String value = attribute.getValue().toString();
                        LOGGER.debug("will classify [" + wrapper.getContextElement().getId() + " attribute:" + attribute.getType() + " value:" + attribute.getValue() + "]");
                        long start = System.nanoTime();
                        String resultTag = classificationService.classify(classifConfig.getId(), value);
                        long diff = System.nanoTime() - start;
                        LOGGER.info("classified [" + wrapper.getContextElement().getId() + " attribute:" + attribute.getType() + " value:" + attribute.getValue() + "] as " + resultTag + " took " + diff);
                        if (resultTag != null) {
                            annotationService.storeClassification(wrapper.getContextElement().getId(), attribute.getType(), value, classifConfig.getId(), resultTag, 0, diff);
                        }
                    } catch (ClassCastException e) {
                        LOGGER.error(e.getLocalizedMessage(), e);
                    }
                    
                    //                    try {
                    //                        ClassifierClient client = new ClassifierClient(classifConfig.getJubatusConfig(), classifConfig.getJubatusPort(), "test", 1);
                    //                        jubatusService.calcScore(client, attribute.getValue(),
                    //                                wrapper.getContextElement().getId(), classifConfig.getAttribute(), classifConfig.getId());
                    //                    } catch (UnknownHostException e) {
                    //                        LOGGER.error(e, e);
                    //                    }
                }
            }
        }
    }
}
