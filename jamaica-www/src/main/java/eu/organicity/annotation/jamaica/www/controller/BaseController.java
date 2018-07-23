package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.www.repository.AnomalyConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.AnomalyRepository;
import eu.organicity.annotation.jamaica.www.repository.AnomalyTrainDataRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassifConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationTrainDataRepository;
import eu.organicity.annotation.jamaica.www.service.AnnotationService;
import eu.organicity.annotation.jamaica.www.service.ClassificationService;
import eu.organicity.annotation.jamaica.www.service.JubatusService;
import eu.organicity.annotation.jamaica.www.service.OrionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
    protected static final String APPLICATION_JSON = "application/json";
    protected static final String TEXT_PLAIN = "text/plain";
    protected static final String TEXT_CSV = "text/csv";

    @Autowired
    AnomalyConfigRepository anomalyConfigRepository;

    @Autowired
    AnomalyTrainDataRepository anomalyTrainDataRepository;

    @Autowired
    AnomalyRepository anomalyRepository;

    @Autowired
    ClassifConfigRepository classifConfigRepository;

    @Autowired
    ClassificationTrainDataRepository classificationTrainDataRepository;

    @Autowired
    ClassificationRepository classificationRepository;
    @Autowired
    ClassificationService classificationService;
    @Autowired
    AnnotationService annotationService;

    @Autowired
    JubatusService jubatusService;

    @Autowired
    OrionService orionService;

    @Value("${jubatus.host}")
    protected String jubatusHost;

    @Value("${jubatus.basePort}")
    protected int basePort;

    @Value("${application.version}")
    protected String applicationVersion;

    @Value("${application.baseUrl}")
    protected String baseUrl;

    @Value("${orion.serverUrl}")
    protected String contextBrokerServerUrl;

}
