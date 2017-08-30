package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.repository.ClassifConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationTrainDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class SecurityService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    
    @Autowired
    ClassifConfigRepository classifConfigRepository;
    @Autowired
    ClassificationRepository classificationRepository;
    @Autowired
    ClassificationTrainDataRepository classificationTrainDataRepository;
    
    public boolean canManage(Principal principal, ClassifConfig config) {
        return principal.getName() != null && config != null && config.getUser() != null && config.getUser().equals(principal);
    }
}
