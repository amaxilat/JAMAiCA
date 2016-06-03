package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.www.dto.VersionDTO;
import eu.organicity.annotation.jamaica.www.service.JubatusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RestController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(RestController.class);

    @Autowired
    JubatusService jubatusService;

    @Value("${application.version}")
    private String applicationVersion;

    @ResponseBody
    @RequestMapping(value = "/api/v1/version", method = RequestMethod.GET, produces = "application/json")
    VersionDTO getVersion(final HttpServletResponse response) {
        LOGGER.debug("[call] getVersion");
        return new VersionDTO(applicationVersion);
    }
}
