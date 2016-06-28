package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.www.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.GET, produces = "application/json")
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
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.DELETE, produces = "application/json")
    ClassifConfigDTO deleteClassificationConfig(final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");

        ClassifConfig config = classifConfigRepository.findById(id);
        classifConfigRepository.delete(id);

        return new ClassifConfigDTO(config);
    }
}
