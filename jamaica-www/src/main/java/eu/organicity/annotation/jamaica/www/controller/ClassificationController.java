package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import eu.organicity.annotation.jamaica.www.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.LabeledDatum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ClassificationController extends BaseController {

    /**
     * a log4j logger to print messages.
     */


    protected static final Logger LOGGER = Logger.getLogger(ClassificationController.class);
    final RandomStringGenerator randomStringGenerator = new RandomStringGenerator();

    /**
     * Adds a new Classification Job to the service.
     * <p>
     *
     * @param response             the {@link HttpServletResponse} object.
     * @param classificationConfig the {@link ClassifConfigDTO} that describes the job to add.
     * @return the added {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    ClassifConfigDTO putClassificationConfig(final HttpServletResponse response, @RequestBody ClassifConfigDTO classificationConfig) {
        LOGGER.debug("[call] putClassificationConfig");

        final String randUiid = randomStringGenerator.getUuid();

        final ClassifConfig conf = new ClassifConfig(classificationConfig.getTypePat(), classificationConfig.getIdPat(), classificationConfig.getAttribute(), classificationConfig.getTagDomain(), randomStringGenerator.getUuid(), randUiid, basePort, jubatusHost, "", System.currentTimeMillis(), false);

        //Add default orion settings
        if (classificationConfig.getContextBrokerUrl() == null) {
            conf.setContextBrokerUrl(orionService.getContextBrokerUrl());
        }
        if (classificationConfig.getContextBrokerService() == null) {
            conf.setContextBrokerService(orionService.getContextBrokerService());
        }
        if (classificationConfig.getContextBrokerServicePath() == null) {
            conf.setContextBrokerServicePath(orionService.getContextBrokerServicePath());
        }

        if (classifConfigRepository.count() > 0) {
            // get max jubatus port entry
            Integer maxJubatusPortEntry = classifConfigRepository.findMaxJubatusPort();
            if (maxJubatusPortEntry == null) {
                maxJubatusPortEntry = 1;
            }
            // add 1 to create next port number
            basePort = maxJubatusPortEntry + 1;
        }

        conf.setJubatusPort(basePort);

        // save anomaly config entry
        ClassifConfig storedConfig = classifConfigRepository.save(conf);

        LOGGER.info("successful save new classification job. Returned id: " + storedConfig.getId());

        return null;


    }


    /**
     * Gets the information of an existing Classification Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO getClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);

        return Utils.newClassifConfigDTO(config);
    }


    /**
     * Subscribe for an existing Anomaly Detection Job with the supplied data.
     *
     * @param id the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/subscribe", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO subscribeClassif(@PathVariable("id") long id) {

        ClassifConfig storedConfig = classifConfigRepository.findById(id);

        try {

            final OrionEntity e = new OrionEntity();
            e.setId(storedConfig.getIdPat());
            e.setIsPattern("true");
            e.setType(storedConfig.getTypePat());
            String[] cond = new String[1];
            cond[0] = "TimeInstant";

            // subscribe to Orion
            final SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "v1/notifyContext/" + storedConfig.getUrlOrion(), cond, "P1D", storedConfig);

            final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
            LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);

            storedConfig.setSubscriptionId(subscriptionId);
            storedConfig = classifConfigRepository.save(storedConfig);

        } catch (IOException | DataAccessException er) {
            LOGGER.error(er, er);
        }

        return Utils.newClassifConfigDTO(storedConfig);
    }

    /**
     * Train a Jubatus instance for an existing Classification Job with the supplied data.
     *
     * @param trainDataDTO the {@link TrainDataListDTO } object to use as input for training the Jubatus instance.
     * @param id           the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link TrainDataListDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/{tag}/train", method = RequestMethod.POST, produces = APPLICATION_JSON)
    TrainDataListDTO trainClassification(@RequestBody TrainDataListDTO trainDataDTO, @PathVariable("id") long id, @PathVariable("tag") String tag) {
        LOGGER.debug("[call] trainClassification");
        ClassifConfig classification = classifConfigRepository.findById(id);


        List<ClassificationTrainData> trainDataList = new ArrayList<>();
        for (final TrainDataDTO singleTrainData : trainDataDTO.getData()) {

            ClassificationTrainData data = new ClassificationTrainData();
            data.setClassificationConfigId(id);
            data.setTag(tag);
            data.setValue(singleTrainData.getValue());
            trainDataList.add(data);
        }

        if (!trainDataList.isEmpty()) {
            classificationTrainDataRepository.save(trainDataList);
        }

        return trainDataDTO;
    }

    /**
     * Train a Jubatus instance for an existing Classification Job with the supplied data.
     *
     * @param id the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link TrainDataListDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/train", method = RequestMethod.GET, produces = APPLICATION_JSON)
    List<ClassificationTrainData> doTrainClassification(@PathVariable("id") long id) {
        LOGGER.debug("[call] doTrainClassification");
        ClassifConfig classification = classifConfigRepository.findById(id);

        List<ClassificationTrainData> data = classificationTrainDataRepository.findByClassificationConfigId(classification.getId());

        try {
            ClassifierClient client = new ClassifierClient(classification.getJubatusConfig(), classification.getJubatusPort(), "test", 1);
            List<LabeledDatum> trainData = new ArrayList<>();

            for (final ClassificationTrainData singleTrainData : data) {
                LOGGER.info(singleTrainData);
                trainData.add(Utils.makeTrainDatum(singleTrainData.getTag(), Double.parseDouble(singleTrainData.getValue())));
                client.train(trainData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Removes the information of an existing Classification Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    ClassifConfigDTO deleteClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);
        classifConfigRepository.delete(id);

        return Utils.newClassifConfigDTO(config);
    }

    @Scheduled(cron = "0 0 * * * ?")
    void checkSubscriptions() {
        LOGGER.info("Checking subscriptions...");
        for (final ClassifConfig classifConfig : classifConfigRepository.findAll()) {
            if (!classifConfig.isEnable()) continue;
            if (System.currentTimeMillis() - classifConfig.getLastSubscription() > 24 * 60 * 60 * 1000) {
                LOGGER.info("Re-Subscribing for " + classifConfig.getId());


                OrionEntity e = new OrionEntity();
                e.setId(classifConfig.getIdPat());
                e.setIsPattern("true");
                e.setType(classifConfig.getTypePat());
                String[] cond = new String[1];
                cond[0] = "TimeInstant";

                try {
                    // subscribe to Orion
                    SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "v1/notifyContext/" + classifConfig.getUrlOrion(), cond, "P1D", classifConfig);

                    final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
                    LOGGER.info("successful subscription to orion. Returned subscriptionId: " + subscriptionId);

                    classifConfig.setSubscriptionId(r.getSubscribeResponse().getSubscriptionId());
                    classifConfig.setLastSubscription(System.currentTimeMillis());
                    // update classification config entry
                    classifConfigRepository.save(classifConfig);
                    LOGGER.info("successful updated the subscription for the anomaly detection job " + classifConfig.getId() + " new subscriptionId is " + classifConfig.getSubscriptionId());

                } catch (IOException | DataAccessException er) {
                    LOGGER.error(er, er);
                }
            }
        }
    }

    /**
     * Enables an existing Classification Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/enable", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO enableClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] enableClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);
//        config.setEnable(true);
//        classifConfigRepository.save(config);

        List<ClassificationTrainData> classifData = classificationTrainDataRepository.findByClassificationConfigId(id);
        try {
            ClassifierClient client = new ClassifierClient(config.getJubatusConfig(), config.getJubatusPort(), "test", 1);
            List<LabeledDatum> trainData = new ArrayList<>();
            for (ClassificationTrainData classificationTrainData : classifData) {
                trainData.add(Utils.makeTrainDatum(classificationTrainData.getTag(), Double.parseDouble(classificationTrainData.getValue())));
                client.train(trainData);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return Utils.newClassifConfigDTO(config);
    }

    /**
     * Disable an existing Classification Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/disable", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO disableClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] enableClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);
        config.setEnable(false);
        classifConfigRepository.save(config);

        return Utils.newClassifConfigDTO(config);
    }
}
