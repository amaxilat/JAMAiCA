package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigListDTO;
import eu.organicity.annotation.jamaica.dto.ClassifStatsDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataDTO;
import eu.organicity.annotation.jamaica.dto.TrainDataListDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
import eu.organicity.annotation.jamaica.www.service.SecurityService;
import eu.organicity.annotation.jamaica.www.utils.Utils;
import net.sf.javaml.core.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class ClassificationController extends BaseController {
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClassificationController.class);
    
    @Autowired
    SecurityService securityService;
    
    @RequestMapping(value = "/v1/config/classification", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigListDTO listClassificationConfigs(final Principal principal) {
        LOGGER.debug("[call] listClassificationConfigs");
        if (principal != null) {
            return classificationService.findByUser(principal);
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    
    /**
     * Adds a new Classification Job to the service.
     * <p>
     *
     * @param classificationConfig the {@link ClassifConfigDTO} that describes the job to add.
     * @return the added {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    ClassifConfigDTO putClassificationConfig(final Principal principal, @RequestBody ClassifConfigDTO classificationConfig) {
        LOGGER.debug("[call] putClassificationConfig");
        if (principal != null) {
            ClassifConfig storedConfig = classificationService.create(principal, classificationConfig);
            return Utils.newClassifConfigDTO(storedConfig);
        } else {
            throw new AccessDeniedException("");
        }
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
    String classifyData(final Principal principal, final @PathVariable("id") Long id, final @RequestBody TrainDataDTO trainDataDTO) {
        LOGGER.debug("[call] classifyData");
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            return classificationService.classify(id, trainDataDTO);
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Gets the information of an existing Classification Job.
     *
     * @param response the {@link HttpServletResponse} object.
     * @param id       the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO getClassificationConfig(final Principal principal, final HttpServletResponse response, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            return Utils.newClassifConfigDTO(config);
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Subscribe for an existing Anomaly Detection Job with the supplied data.
     *
     * @param id the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link AnomalyConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/subscribe", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO subscribeClassif(final Principal principal, @PathVariable("id") long id) {
        LOGGER.debug("[call] subscribeClassif");
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            final ClassifConfig storedConfig = classificationService.subscribe(id);
            return Utils.newClassifConfigDTO(storedConfig);
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Train a Jubatus instance for an existing Classification Job with the supplied data.
     *
     * @param trainDataDTO the {@link TrainDataListDTO } object to use as input for training the Jubatus instance.
     * @param id           the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link TrainDataListDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/{tag}/train", method = RequestMethod.POST, produces = APPLICATION_JSON)
    TrainDataListDTO trainClassification(final Principal principal, final @RequestBody TrainDataListDTO trainDataDTO, final @PathVariable("id") long id, final @PathVariable("tag") String tag) {
        LOGGER.debug("[call] trainClassification");
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            return classificationService.train(id, tag, trainDataDTO);
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Train a Jubatus instance for an existing Classification Job with the supplied data.
     *
     * @param id the id of the requested {@link AnomalyConfigDTO}.
     * @return the used {@link TrainDataListDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/train", method = RequestMethod.GET, produces = APPLICATION_JSON)
    List<ClassificationTrainData> doTrainClassification(final Principal principal, @PathVariable("id") long id) {
        LOGGER.debug("[call] doTrainClassification");
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            Collection<Instance> data = classificationService.train(id);
            return new ArrayList<>();
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Removes the information of an existing Classification Job.
     *
     * @param id the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    ClassifConfigDTO deleteClassificationConfig(final Principal principal, @PathVariable("id") long id) {
        LOGGER.debug("[call] getClassificationConfig");
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            classifConfigRepository.delete(id);
            return Utils.newClassifConfigDTO(config);
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Enables an existing Classification Job.
     *
     * @param id the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/enable", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO enableClassificationConfig(final Principal principal, @PathVariable("id") long id) {
        LOGGER.debug("[call] enableClassificationConfig");
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            ClassifConfig storedConfig = classificationService.enable(id);
            return Utils.newClassifConfigDTO(storedConfig);
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Enables an existing Classification Job.
     *
     * @param id the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/stats", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifStatsDTO statsClassificationConfig(final Principal principal, @PathVariable("id") long id) {
        LOGGER.info("[call] statsClassificationConfig ");
        
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            ClassifStatsDTO dto = new ClassifStatsDTO();
            dto.setClassifConfigDTO(Utils.newClassifConfigDTO(config));
            dto.setClassifications((int) classificationRepository.countByClassificationConfigId(id));
            dto.setTrainDataEntries((int) classificationTrainDataRepository.countByClassificationConfigId(id));
            return dto;
        } else {
            throw new AccessDeniedException("");
        }
    }
    
    /**
     * Disable an existing Classification Job.
     *
     * @param id the id of the requested {@link ClassifConfigDTO}.
     * @return the existing {@link ClassifConfigDTO}.
     */
    @RequestMapping(value = "/v1/config/classification/{id}/disable", method = RequestMethod.GET, produces = APPLICATION_JSON)
    ClassifConfigDTO disableClassificationConfig(final Principal principal, @PathVariable("id") long id) {
        LOGGER.debug("[call] enableClassificationConfig");
        
        final ClassifConfig config = classifConfigRepository.findById(id);
        if (securityService.canManage(principal, config)) {
            config.setEnable(false);
            ClassifConfig storedConfig = classifConfigRepository.save(config);
            return Utils.newClassifConfigDTO(storedConfig);
        } else {
            throw new AccessDeniedException("");
        }
    }
}
