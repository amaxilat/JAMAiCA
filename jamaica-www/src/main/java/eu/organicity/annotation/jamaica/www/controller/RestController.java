package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.dto.VersionDTO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RestController extends BaseController {

    /**
     * a log4j logger to print messages.
     */


    protected static final Logger LOGGER = Logger.getLogger(RestController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = APPLICATION_JSON)
    String getHome() {
        LOGGER.debug("[call] /");
        return "redirect:/swagger-ui.html";
    }

    @ResponseBody
    @RequestMapping(value = "/v1/version", method = RequestMethod.GET, produces = APPLICATION_JSON)
    VersionDTO getVersion(final HttpServletResponse response) {
        LOGGER.debug("[call] getVersion");
        return new VersionDTO(applicationVersion);
    }
}
