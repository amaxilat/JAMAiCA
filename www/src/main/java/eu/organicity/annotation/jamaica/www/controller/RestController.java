package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.Attribute;
import com.amaxilatis.orion.model.OrionContextElement;
import com.amaxilatis.orion.model.OrionContextElementWrapper;
import com.amaxilatis.orion.model.SubscriptionUpdate;
import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.www.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.www.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.www.dto.VersionDTO;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.jubat.anomaly.AnomalyClient;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RestController extends BaseController {

    /**
     * a log4j logger to print messages.
     */


    protected static final Logger LOGGER = Logger.getLogger(RestController.class);
    final RandomStringGenerator randomStringGenerator = new RandomStringGenerator();

    @ResponseBody
    @RequestMapping(value = "/api/v1/version", method = RequestMethod.GET, produces = "application/json")
    VersionDTO getVersion(final HttpServletResponse response) {
        LOGGER.debug("[call] getVersion");
        return new VersionDTO(applicationVersion);
    }

    /**
     * Adds a new Anomaly Detection Job to the service.
     * <p>
     *
     * @param response      the {@see HttpServletResponse} object.
     * @param anomalyConfig the {@see AnomalyConfigDTO} that describes the job to add.
     * @return the added {@see AnomalyConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/anomaly", method = RequestMethod.PUT, produces = "application/json")
    AnomalyConfigDTO putAnomalyConfig(final HttpServletResponse response, @RequestBody AnomalyConfigDTO anomalyConfig) {
        LOGGER.debug("[call] putAnomalyConfig");


        OrionEntity e = new OrionEntity();
        e.setId(anomalyConfig.getIdPat());
        e.setIsPattern("true");
        e.setType("urn:oc:entitytype:iotdevice");
        String[] cond = new String[1];
        cond[0] = "TimeInstant";

        final String randUiid = randomStringGenerator.getUuid();


        try {
            // subscribe to Orion
            SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "notifyContext/" + randUiid, cond, "P1D");


            final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
            LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);

            if (anomalyConfigRepository.count() > 0) {
                // get max jubatus port entry
                Integer maxJubatusPortEntry = anomalyConfigRepository.findMaxJubatusPort();
                if (maxJubatusPortEntry == null) {
                    maxJubatusPortEntry = 1;
                }
                // add 1 to create next port number
                basePort = maxJubatusPortEntry + 1;
            }

            // save anomaly config entry
            AnomalyConfig storedConfig = anomalyConfigRepository.save(new AnomalyConfig(anomalyConfig.getTypePat(), anomalyConfig.getIdPat(), anomalyConfig.getAttribute(), "tags", randomStringGenerator.getUuid(), randUiid, basePort, jubatusHost, subscriptionId));
            LOGGER.info("successful save new anomaly detection job. Returned id: " + storedConfig.getId());

            return new AnomalyConfigDTO(storedConfig);

        } catch (IOException er) {
            LOGGER.error(er, er);

        } catch (DataAccessException er) {
            LOGGER.error(er, er);

        }
        return null;


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
     * Removes the information of an existing Anomaly Detection Job.
     *
     * @param response the {@see HttpServletResponse} object.
     * @param id       the id of the requested {@see AnomalyConfigDTO}.
     * @return the existing {@see AnomalyConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/anomaly/{id}", method = RequestMethod.DELETE, produces = "application/json")
    AnomalyConfigDTO deleteAnomalyConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getAnomalyConfig");

        AnomalyConfig config = anomalyConfigRepository.findById(id);
        anomalyConfigRepository.delete(id);

        return new AnomalyConfigDTO(config);
    }

    /**
     * Adds a new Classification Job to the service.
     * <p>
     *
     * @param response             the {@see HttpServletResponse} object.
     * @param classificationConfig the {@see ClassifConfigDTO} that describes the job to add.
     * @return the added {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/classification", method = RequestMethod.PUT, produces = "application/json")
    ClassifConfigDTO putClassificationConfig(final HttpServletResponse response, @RequestBody ClassifConfigDTO classificationConfig) {
        LOGGER.debug("[call] putClassificationConfig");

        OrionEntity e = new OrionEntity();
        e.setId(classificationConfig.getIdPat());
        e.setIsPattern("true");
        e.setType("urn:oc:entitytype:iotdevice");
        String[] cond = new String[1];
        cond[0] = "TimeInstant";

        final String randUiid = randomStringGenerator.getUuid();


        try {
            // subscribe to Orion
            SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "notifyContext/" + randUiid, cond, "P1D");


            final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
            LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);

            if (anomalyConfigRepository.count() > 0) {
                // get max jubatus port entry
                Integer maxJubatusPortEntry = classifConfigRepository.findMaxJubatusPort();
                if (maxJubatusPortEntry == null) {
                    maxJubatusPortEntry = 1;
                }
                // add 1 to create next port number
                basePort = maxJubatusPortEntry + 1;
            }

            // save anomaly config entry
            ClassifConfig storedConfig = classifConfigRepository.save(new ClassifConfig(classificationConfig.getTypePat(), classificationConfig.getIdPat(), classificationConfig.getAttribute(), "tags", randomStringGenerator.getUuid(), randUiid, basePort, jubatusHost, subscriptionId));
            LOGGER.info("successful save new classification job. Returned id: " + storedConfig.getId());

            return new ClassifConfigDTO(storedConfig);

        } catch (IOException er) {
            LOGGER.error(er, er);

        } catch (DataAccessException er) {
            LOGGER.error(er, er);

        }
        return null;


    }

    /**
     * Gets the information of an existing Classification Job.
     *
     * @param response the {@see HttpServletResponse} object.
     * @param id       the id of the requested {@see ClassifConfigDTO}.
     * @return the existing {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/classification/{id}", method = RequestMethod.GET, produces = "application/json")
    ClassifConfigDTO getClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);

        return new ClassifConfigDTO(config);
    }

    /**
     * Removes the information of an existing Classification Job.
     *
     * @param response the {@see HttpServletResponse} object.
     * @param id       the id of the requested {@see ClassifConfigDTO}.
     * @return the existing {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/config/classification/{id}", method = RequestMethod.DELETE, produces = "application/json")
    ClassifConfigDTO deleteClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);
        classifConfigRepository.delete(id);

        return new ClassifConfigDTO(config);
    }

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
