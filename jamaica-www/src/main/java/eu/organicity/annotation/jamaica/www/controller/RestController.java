package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.dto.VersionDTO;
import eu.organicity.annotation.jamaica.www.repository.AnomalyConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.AnomalyRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassifConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.security.cert.X509Certificate;

@Controller
public class RestController extends BaseController {
    
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
    
    @Value("${orion.serverUrl}")
    private String orionServerUrl;
    
    @Autowired
    AnomalyConfigRepository anomalyConfigRepository;
    @Autowired
    ClassifConfigRepository classifConfigRepository;
    @Autowired
    AnomalyRepository anomalyRepository;
    @Autowired
    ClassificationRepository classificationRepository;
    
    
    @PostConstruct
    public void init() {
        
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
        
        }
    }
    
    
    @RequestMapping(value = "/web/home", method = RequestMethod.GET)
    String home(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("anomaly", anomalyConfigRepository.findByUser(principal.getName()));
        model.addAttribute("classifications", classifConfigRepository.findByUser(principal.getName()));
        LOGGER.debug("[call] /web/home");
        return "home";
    }
    
    @RequestMapping(value = "/web/anomaly", method = RequestMethod.GET)
    String anomaly(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("anomaly", anomalyConfigRepository.findByUser(principal.getName()));
        model.addAttribute("classifications", classifConfigRepository.findByUser(principal.getName()));
        LOGGER.debug("[call] /web/home");
        return "anomaly";
    }
    
    @RequestMapping(value = "/web/classification", method = RequestMethod.GET)
    String classification(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("classifications", classifConfigRepository.findByUser(principal.getName()));
        LOGGER.debug("[call] /web/home");
        return "classification";
    }
    
    @RequestMapping(value = "/web/classification/{id}", method = RequestMethod.GET)
    String classificationView(Principal principal, @PathVariable("id") int id, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("classifications", classifConfigRepository.findByUser(principal.getName()));
        model.addAttribute("classification", classifConfigRepository.findById(id));
        LOGGER.debug("[call] /web/home");
        return "classificationView";
    }
    
    @RequestMapping(value = "/web/classification/{id}/results", method = RequestMethod.GET)
    String classificationResults(Principal principal, @PathVariable("id") int id, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("classification", classifConfigRepository.findById(id));
        model.addAttribute("results", classificationRepository.findByClassificationConfigId(id));
        LOGGER.debug("[call] /web/home");
        return "classificationResults";
    }
    
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
