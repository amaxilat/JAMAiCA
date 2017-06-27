package eu.organicity.annotation.jamaica.www.controller;

import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigListDTO;
import eu.organicity.annotation.jamaica.dto.ClassifStatsDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.annotation.jamaica.www.configuration.OrganicityAccount;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
import eu.organicity.annotation.jamaica.www.service.OrganicityUserDetailsService;
import eu.organicity.annotation.jamaica.www.utils.RandomStringGenerator;
import eu.organicity.annotation.jamaica.www.utils.Utils;
import net.sf.javaml.core.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class ClassificationController extends BaseController {
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClassificationController.class);
    final RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
    
    
    @RequestMapping(value = "/v1/config/classification", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigListDTO listClassificationConfigs(final HttpServletResponse response) {
        LOGGER.debug("[call] listClassificationConfigs");
        
        final ClassifConfigListDTO dto = new ClassifConfigListDTO();
        dto.setClassificationConfigurations(new ArrayList<>());
        for (ClassifConfig classifConfig : classifConfigRepository.findAll()) {
            dto.getClassificationConfigurations().add(Utils.newClassifConfigDTO(classifConfig));
        }
        
        return dto;
    }
    
    
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
        
        final ClassifConfig conf = new ClassifConfig(classificationConfig.getTypePat(), classificationConfig.getIdPat(), classificationConfig.getAttribute(), classificationConfig.getTagDomain(), randomStringGenerator.getUuid(), randUiid, "", System.currentTimeMillis(), false);
        
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
        
        // save anomaly config entry
        ClassifConfig storedConfig = classifConfigRepository.save(conf);
        
        LOGGER.info("successful save new classification job. Returned id: " + storedConfig.getId());
        
        return Utils.newClassifConfigDTO(storedConfig);
    }
    
    /**
     * Adds a new Classification Job to the service.
     * <p>
     *
     * @param classificationConfig the {@link ClassifConfigDTO} that describes the job to add.
     * @param response             the {@link HttpServletResponse} object.
     * @return the added {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classify/{id}", method = RequestMethod.POST, produces = APPLICATION_JSON)
    String classifyData(final HttpServletResponse response, @PathVariable("id") Long id, @RequestBody TrainDataDTO trainDataDTO) {
        LOGGER.debug("[call] classifyData");
        return classificationService.classify(id, trainDataDTO);
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
            LOGGER.error(er.getLocalizedMessage(), er);
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
        Collection<Instance> data = classificationService.train(id);
        return new ArrayList<>();
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
        config.setEnable(true);
        classifConfigRepository.save(config);
        
        return Utils.newClassifConfigDTO(config);
    }
    
    /**
     * Enables an existing Classification Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/stats", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifStatsDTO statsClassificationConfig( final HttpServletResponse response, @PathVariable("id") long id) {
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        if (ou != null) {
            LOGGER.info("[call] statsClassificationConfig " + ou.getUser());
        }
        
        ClassifConfig config = classifConfigRepository.findById(id);
        ClassifStatsDTO dto = new ClassifStatsDTO();
        dto.setClassifConfigDTO(Utils.newClassifConfigDTO(config));
        dto.setClassifications((int) classificationRepository.countByClassificationConfigId(id));
        dto.setTrainDataEntries((int) classificationTrainDataRepository.countByClassificationConfigId(id));
        return dto;
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
