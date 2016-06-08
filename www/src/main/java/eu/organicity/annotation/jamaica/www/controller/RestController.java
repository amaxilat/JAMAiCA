package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.OrionContextElement;
import com.amaxilatis.orion.model.OrionContextElementWrapper;
import com.amaxilatis.orion.model.SubscriptionUpdate;
import eu.organicity.annotation.jamaica.www.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.www.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.www.dto.VersionDTO;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RestController extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(RestController.class);


    @ResponseBody
    @RequestMapping(value = "/api/v1/version", method = RequestMethod.GET, produces = "application/json")
    VersionDTO getVersion(final HttpServletResponse response) {
        LOGGER.debug("[call] getVersion");
        return new VersionDTO(applicationVersion);
    }

    /**
     * Adds a new Anomaly Detection Job to the service.
     * <p>
     * TODO: implement this.
     *
     * @param response      the {@see HttpServletResponse} object.
     * @param anomalyConfig the {@see AnomalyConfigDTO} that describes the job to add.
     * @return the added {@see AnomalyConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/anomaly", method = RequestMethod.PUT, produces = "application/json")
    AnomalyConfigDTO putAnomalyConfig(final HttpServletResponse response, @ModelAttribute AnomalyConfigDTO anomalyConfig) {
        LOGGER.debug("[call] putAnomalyConfig");


        return anomalyConfig;
    }

    /**
     * Gets the information of an existing Anomaly Detection Job.
     *
     * @param response the {@see HttpServletResponse} object.
     * @param id       the id of the requested {@see AnomalyConfigDTO}.
     * @return the existing {@see AnomalyConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/anomaly/{id}", method = RequestMethod.GET, produces = "application/json")
    AnomalyConfigDTO getAnomalyConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getAnomalyConfig");

        AnomalyConfig config = anomalyConfigRepository.findById(id);

        return new AnomalyConfigDTO(config);
    }

    /**
     * Adds a new Classification Job to the service.
     * <p>
     * TODO: implement this.
     *
     * @param response             the {@see HttpServletResponse} object.
     * @param classificationConfig the {@see ClassifConfigDTO} that describes the job to add.
     * @return the added {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/classification", method = RequestMethod.PUT, produces = "application/json")
    ClassifConfigDTO putClassificationConfig(final HttpServletResponse response, @ModelAttribute ClassifConfigDTO classificationConfig) {
        LOGGER.debug("[call] putClassificationConfig");


        return classificationConfig;
    }

//    TODO: implement the getClassificationConfig like the getAnomalyConfig


    /**
     * A method that handles subscription updates from Orion or users and starts the data validation against Jubatus.
     *
     * TODO : find the attributes of interest.
     * TODO : find the concerning subscription.
     * TODO : forward the request to Jubatus using Jubatus Service - should be ASYNC.
     *
     * @param subscriptionUpdate the {@see SubscriptionUpdate} received from the Orion Context Broker.
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/notifyContext/{contextConnectionId}", method = RequestMethod.POST, produces = "application/json")
    SubscriptionUpdate notifyContext(@RequestBody final SubscriptionUpdate subscriptionUpdate) {
        LOGGER.debug("[call] notifyContext");
        try {
            LOGGER.info(subscriptionUpdate);
            for (final OrionContextElementWrapper wrapper : subscriptionUpdate.getContextResponses()) {
                final OrionContextElement element = wrapper.getContextElement();
                LOGGER.info(element.getId());
                LOGGER.info(element.getType());
                LOGGER.info(element.getIsPattern());
                LOGGER.info(element.getAttributes());
            }
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return subscriptionUpdate;
    }
}
