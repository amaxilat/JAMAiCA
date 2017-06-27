package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.AnomalyTrainData;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import eu.organicity.annotation.jamaica.www.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AnomalyController extends BaseController {

    /**
     * a log4j logger to print messages.
     */


    protected static final Logger LOGGER = LoggerFactory.getLogger(AnomalyController.class);
    final RandomStringGenerator randomStringGenerator = new RandomStringGenerator();

    /**
     * Adds a new Anomaly Detection Job to the service.
     * <p>
     *
     * @param response      the {@link HttpServletResponse} object.
     * @param anomalyConfig the {@link AnomalyConfigDTO} that describes the job to add.
     * @return the added {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/anomaly", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    AnomalyConfigDTO putAnomalyConfig(final HttpServletResponse response, @RequestBody AnomalyConfigDTO anomalyConfig) {
        LOGGER.debug("[call] putAnomalyConfig");

        final String randUiid = randomStringGenerator.getUuid();

        AnomalyConfig config = new AnomalyConfig(anomalyConfig.getTypePat(), anomalyConfig.getIdPat(), anomalyConfig.getAttribute(), anomalyConfig.getTagDomain(), randomStringGenerator.getUuid(), randUiid, basePort, jubatusHost, "", System.currentTimeMillis(), false,
                anomalyConfig.getContextBrokerUrl(), anomalyConfig.getContextBrokerService(), anomalyConfig.getContextBrokerServicePath());

        //Add default orion settings
        if (anomalyConfig.getContextBrokerUrl() == null) {
            config.setContextBrokerUrl(orionService.getContextBrokerUrl());
        }
        if (anomalyConfig.getContextBrokerService() == null) {
            config.setContextBrokerService(orionService.getContextBrokerService());
        }
        if (anomalyConfig.getContextBrokerServicePath() == null) {
            config.setContextBrokerServicePath(orionService.getContextBrokerServicePath());
        }

        if (anomalyConfigRepository.count() > 0) {
            // get max jubatus port entry
            Integer maxJubatusPortEntry = anomalyConfigRepository.findMaxJubatusPort();
            if (maxJubatusPortEntry == null) {
                maxJubatusPortEntry = 1;
            }
            // add 1 to create next port number
            basePort = maxJubatusPortEntry + 1;
        }

        config.setJubatusPort(basePort);

        // save anomaly config entry
        AnomalyConfig storedConfig = anomalyConfigRepository.save(config);
        LOGGER.info("successful save new anomaly detection job. Returned id: " + storedConfig.getId());

        return Utils.newAnomalyConfigDTO(storedConfig);
//
//
//        try {
//            // subscribe to Orion
//            SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "/v1/notifyContext/" + randUiid, cond, "P1D", storedConfig);
//
//
//            final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
//            LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);
//
//            if (anomalyConfigRepository.count() > 0) {
//                // get max jubatus port entry
//                Integer maxJubatusPortEntry = anomalyConfigRepository.findMaxJubatusPort();
//                if (maxJubatusPortEntry == null) {
//                    maxJubatusPortEntry = 1;
//                }
//                // add 1 to create next port number
//                basePort = maxJubatusPortEntry + 1;
//            }
//
//            jubatusService.launchJubaanomaly(basePort);
//
//            // save anomaly config entry
//            storedConfig.setSubscriptionId(subscriptionId);
//            storedConfig = anomalyConfigRepository.save(storedConfig);
//            LOGGER.info("successful save new anomaly detection job. Returned id: " + storedConfig.getId());
//
//            return Utils.newAnomalyConfigDTO(storedConfig);
//        } catch (IOException er) {
//            LOGGER.error(er, er);
//
//        } catch (DataAccessException er) {
//            LOGGER.error(er, er);
//
//        }
//        return null;


    }

    /**
     * Gets the information of an existing Anomaly Detection Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link AnomalyConfigDTO}.
     * @return the existing {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/anomaly/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    AnomalyConfigDTO getAnomalyConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getAnomalyConfig");

        AnomalyConfig config = anomalyConfigRepository.findById(id);

        return Utils.newAnomalyConfigDTO(config);
    }

    /**
     * Removes the information of an existing Anomaly Detection Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link AnomalyConfigDTO}.
     * @return the existing {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/anomaly/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    AnomalyConfigDTO deleteAnomalyConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getAnomalyConfig");

        AnomalyConfig config = anomalyConfigRepository.findById(id);
        anomalyConfigRepository.delete(id);

        return Utils.newAnomalyConfigDTO(config);
    }

    /**
     * Train a Jubatus instance for an existing Anomaly Detection Job with the supplied data.
     *
     * @param trainDataDTO the {@link TrainDataListDTO } object to use as input for training the Jubatus instance.
     * @param id           the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link TrainDataListDTO}.
     */
    @RequestMapping(value = "/v1/config/anomaly/{id}/train", method = RequestMethod.POST, produces = APPLICATION_JSON)
    TrainDataListDTO trainAnomaly(@RequestBody TrainDataListDTO trainDataDTO, @PathVariable("id") long id) {
        LOGGER.debug("[call] trainAnomaly");
        AnomalyConfig anomalyConfig = anomalyConfigRepository.findById(id);

        List<AnomalyTrainData> trainDataList = new ArrayList<>();
        for (final TrainDataDTO singleTrainData : trainDataDTO.getData()) {

            AnomalyTrainData data = new AnomalyTrainData();
            data.setAnomalyConfigId(id);
            data.setValue(singleTrainData.getValue());

            trainDataList.add(data);
        }

        if (!trainDataList.isEmpty()) {
            anomalyTrainDataRepository.save(trainDataList);
        }
        return trainDataDTO;
    }

    /**
     * Subscribe for an existing Anomaly Detection Job with the supplied data.
     *
     * @param id the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/anomaly/{id}/subscribe", method = RequestMethod.GET, produces = APPLICATION_JSON)
    AnomalyConfigDTO subscribeAnomaly(@PathVariable("id") long id) {
        LOGGER.debug("[call] subscribeAnomaly");
        AnomalyConfig anomalyConfig = anomalyConfigRepository.findById(id);

        OrionEntity e = new OrionEntity();
        e.setId(anomalyConfig.getIdPat());
        e.setIsPattern("true");
        e.setType(anomalyConfig.getTypePat());
        String[] cond = new String[1];
        cond[0] = "TimeInstant";

        try {
            // subscribe to Orion
            SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "v1/notifyContext/" + anomalyConfig.getUrlOrion(), cond, "P1D", anomalyConfig);

            final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
            LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);

            anomalyConfig.setSubscriptionId(r.getSubscribeResponse().getSubscriptionId());
            anomalyConfig.setLastSubscription(System.currentTimeMillis());
            // update anomaly config entry
            anomalyConfigRepository.save(anomalyConfig);
            LOGGER.info("successful updated the subscription for the anomaly detection job " + anomalyConfig.getId() + " new subscriptionId is " + anomalyConfig.getSubscriptionId());
        } catch (IOException | DataAccessException er) {
            LOGGER.error(er.getLocalizedMessage(), er);
        }

        return Utils.newAnomalyConfigDTO(anomalyConfig);
    }


    @Scheduled(cron = "0 0 * * * ?")
    void checkSubscriptions() {
        LOGGER.info("Checking subscriptions...");
        for (final AnomalyConfig anomalyConfig : anomalyConfigRepository.findAll()) {
            if (System.currentTimeMillis() - anomalyConfig.getLastSubscription() > 24 * 60 * 60 * 1000) {
                LOGGER.info("Re-Subscribing for " + anomalyConfig.getId());


                OrionEntity e = new OrionEntity();
                e.setId(anomalyConfig.getIdPat());
                e.setIsPattern("true");
                e.setType(anomalyConfig.getTypePat());
                String[] cond = new String[1];
                cond[0] = "TimeInstant";

                try {
                    // subscribe to Orion
                    SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "v1/notifyContext/" + anomalyConfig.getUrlOrion(), cond, "P1D", anomalyConfig);

                    final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
                    LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);

                    anomalyConfig.setSubscriptionId(r.getSubscribeResponse().getSubscriptionId());
                    anomalyConfig.setLastSubscription(System.currentTimeMillis());
                    // update anomaly config entry
                    anomalyConfigRepository.save(anomalyConfig);
                    LOGGER.info("successful updated the subscription for the anomaly detection job " + anomalyConfig.getId() + " new subscriptionId is " + anomalyConfig.getSubscriptionId());

                } catch (IOException | DataAccessException er) {
                    LOGGER.error(er.getLocalizedMessage(), er);
                }
            }
        }
    }

    /**
     * Enable an existing Anomaly Detection Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link AnomalyConfigDTO}.
     * @return the existing {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/anomaly/{id}/enable", method = RequestMethod.POST, produces = APPLICATION_JSON)
    AnomalyConfigDTO enableAnomalyConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] enableAnomalyConfig");

        AnomalyConfig config = anomalyConfigRepository.findById(id);
        config.setEnable(true);
        anomalyConfigRepository.save(config);
        return Utils.newAnomalyConfigDTO(config);

    }

    /**
     * Disable an existing Anomaly Detection Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link AnomalyConfigDTO}.
     * @return the existing {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/anomaly/{id}/disable", method = RequestMethod.POST, produces = APPLICATION_JSON)
    AnomalyConfigDTO disableAnomalyConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] disableAnomalyConfig");

        AnomalyConfig config = anomalyConfigRepository.findById(id);
        config.setEnable(false);
        anomalyConfigRepository.save(config);
        return Utils.newAnomalyConfigDTO(config);

    }
}
