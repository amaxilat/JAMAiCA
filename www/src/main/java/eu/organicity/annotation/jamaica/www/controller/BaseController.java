package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.www.service.JubatusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(BaseController.class);

    @Autowired
    JubatusService jubatusService;

    @Value("${application.version}")
    protected String applicationVersion;

}
