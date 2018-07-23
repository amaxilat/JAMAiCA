package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.www.configuration.OrganicityAccount;
import eu.organicity.annotation.jamaica.www.service.OrganicityUserDetailsService;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class OrganicityProfileController {


    @RequestMapping(value = {"OrganicityProfile"}, method = RequestMethod.GET)
    public final String profile() {
        JSONObject jsonObject = new JSONObject();
        OrganicityAccount ou = OrganicityUserDetailsService.getCurrentUser();
        jsonObject.append("OrganicityProfile", ou.toString());
        if (ou.isAdministrator()) {
            jsonObject.append("role", "administrator");
        }
        if (ou.isExperimenter()) {
            jsonObject.append("role", "experimenter");
        }
        jsonObject.append("token", ou.getKeycloakSecurityContext().getTokenString());
        return jsonObject.toString(2);
    }


    @RequestMapping(value = {"logout"}, method = RequestMethod.GET)
    public final void logout(HttpServletResponse response) throws IOException {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        SecurityContextHolder.clearContext();
        response.sendRedirect("/OrganicityProfile");
    }

}

