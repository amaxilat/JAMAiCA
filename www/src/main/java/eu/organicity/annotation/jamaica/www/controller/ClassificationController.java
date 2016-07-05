package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import eu.organicity.annotation.jamaica.www.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.LabeledDatum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Controller
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
     * @param response             the {@see HttpServletResponse} object.
     * @param classificationConfig the {@see ClassifConfigDTO} that describes the job to add.
     * @return the added {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/v1/config/classification", method = RequestMethod.PUT, produces = "application/json")
    ClassifConfigDTO putClassificationConfig(final HttpServletResponse response, @RequestBody ClassifConfigDTO classificationConfig) {
        LOGGER.debug("[call] putClassificationConfig");

        OrionEntity e = new OrionEntity();
        e.setId(classificationConfig.getIdPat());
        e.setIsPattern("true");
        e.setType(classificationConfig.getTypePat());
        String[] cond = new String[1];
        cond[0] = "TimeInstant";

        final String randUiid = randomStringGenerator.getUuid();


        try {
            // subscribe to Orion
            SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "api/v1/notifyContext/" + randUiid, cond, "P1D");


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
            ClassifConfig storedConfig = classifConfigRepository.save(new ClassifConfig(classificationConfig.getTypePat(), classificationConfig.getIdPat(), classificationConfig.getAttribute(), classificationConfig.getTagDomain(), randomStringGenerator.getUuid(), randUiid, basePort, jubatusHost, subscriptionId,System.currentTimeMillis(), false));
            LOGGER.info("successful save new classification job. Returned id: " + storedConfig.getId());

            return Utils.newClassifConfigDTO(storedConfig);

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
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.GET, produces = "application/json")
    ClassifConfigDTO getClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);

        return Utils.newClassifConfigDTO(config);
    }


    /**
     * Train a Jubatus instance for an existing Classification Job with the supplied data.
     *
     * @param trainDataDTO the {@see TrainDataListDTO } object to use as input for training the Jubatus instance.
     * @param id           the id of the requested {@see AnomalyConfigDTO}.
     * @return the used {@see TrainDataListDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/v1/config/classification/{id}/{tag}/train", method = RequestMethod.POST, produces = "application/json")
    TrainDataListDTO trainClassification(@RequestBody TrainDataListDTO trainDataDTO, @PathVariable("id") long id, @PathVariable("tag") String tag) {
        LOGGER.debug("[call] trainClassification");
        ClassifConfig classification = classifConfigRepository.findById(id);

        try {

            ClassifierClient client = new ClassifierClient(classification.getJubatusConfig(), classification.getJubatusPort(), "test", 1);
            List<LabeledDatum> trainData = new ArrayList<>();

            for (final TrainDataDTO singleTrainData : trainDataDTO.getData()) {
                LOGGER.info(singleTrainData);
                trainData.add(Utils.makeTrainDatum(tag, Double.parseDouble(singleTrainData.getValue())));
                client.train(trainData);

            }
            return trainDataDTO;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes the information of an existing Classification Job.
     *
     * @param response the {@see HttpServletResponse} object.
     * @param id       the id of the requested {@see ClassifConfigDTO}.
     * @return the existing {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.DELETE, produces = "application/json")
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
                    SubscriptionResponse r = orionService.subscribeToOrion(e, null, baseUrl + "api/v1/notifyContext/" + classifConfig.getUrlOrion(), cond, "P1D");

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
     * @param response the {@see HttpServletResponse} object.
     * @param id       the id of the requested {@see ClassifConfigDTO}.
     * @return the existing {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/v1/config/classification/{id}/enable", method = RequestMethod.GET, produces = "application/json")
    ClassifConfigDTO enableClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] enableClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);
        config.setEnable(true);
        classifConfigRepository.save(config);

        return Utils.newClassifConfigDTO(config);
    }

    /**
     * Disable an existing Classification Job.
     *
     * @param response the {@see HttpServletResponse} object.
     * @param id       the id of the requested {@see ClassifConfigDTO}.
     * @return the existing {@see ClassifConfigDTO}.
     */
    @ResponseBody
    @RequestMapping(value = "/v1/config/classification/{id}/disable", method = RequestMethod.GET, produces = "application/json")
    ClassifConfigDTO disableClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] enableClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);
        config.setEnable(false);
        classifConfigRepository.save(config);

        return Utils.newClassifConfigDTO(config);
    }
}
