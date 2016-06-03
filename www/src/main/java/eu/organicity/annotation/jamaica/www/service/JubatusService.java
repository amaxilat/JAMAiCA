package eu.organicity.annotation.jamaica.www.service;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JubatusService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(JubatusService.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("init");
    }

}
