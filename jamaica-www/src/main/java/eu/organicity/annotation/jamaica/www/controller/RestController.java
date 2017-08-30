package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.dto.VersionDTO;
import eu.organicity.annotation.jamaica.www.WebClassificationTrainDataDTO;
import eu.organicity.annotation.jamaica.www.WebCreateClassificationDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

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
        //
        //        try {
        //            // Create a trust manager that does not validate certificate chains
        //            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        //                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        //                    return null;
        //                }
        //
        //                public void checkClientTrusted(X509Certificate[] certs, String authType) {
        //                }
        //
        //                public void checkServerTrusted(X509Certificate[] certs, String authType) {
        //                }
        //            }};
        //            // Install the all-trusting trust manager
        //            SSLContext sc = SSLContext.getInstance("SSL");
        //            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        //            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        //
        //            // Create all-trusting host name verifier
        //            HostnameVerifier allHostsValid = new HostnameVerifier() {
        //                public boolean verify(String hostname, SSLSession session) {
        //                    return true;
        //                }
        //            };
        //
        //            // Install the all-trusting host verifier
        //            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        //        } catch (Exception e) {
        //
        //        }
    }
    
    @RequestMapping(value = "/web/home", method = RequestMethod.GET)
    String home(final Principal principal, final Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("anomaly", anomalyConfigRepository.findByUser(principal.getName()));
        model.addAttribute("classifications", classificationService.findByUser(principal));
        LOGGER.debug("[call] /web/home");
        return "home";
    }
    
    @RequestMapping(value = "/web/anomaly", method = RequestMethod.GET)
    String anomaly(final Principal principal, final Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("anomaly", anomalyConfigRepository.findByUser(principal.getName()));
        model.addAttribute("classifications", classificationService.findByUser(principal));
        LOGGER.debug("[call] /web/home");
        return "anomaly";
    }
    
    @RequestMapping(value = "/web/classification", method = RequestMethod.GET)
    String classification(final Principal principal, final Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("classifications", classificationService.findByUser(principal));
        LOGGER.debug("[call] /web/home");
        return "classification";
    }
    
    @RequestMapping(value = "/web/classification/add", method = RequestMethod.GET)
    String addClassification(final Principal principal, final Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("classifications", classificationService.findByUser(principal));
        LOGGER.debug("[call] /web/home");
        return "classificationCreate";
    }
    
    @RequestMapping(value = "/web/classification/{id}", method = RequestMethod.GET)
    String classificationView(Principal principal, @PathVariable("id") int id, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("classifications", classificationService.findByUser(principal));
        model.addAttribute("classification", classificationService.findById(id));
        model.addAttribute("trainData", classificationTrainDataRepository.findByClassificationConfigId(id));
        LOGGER.debug("[call] /web/home");
        return "classificationView";
    }
    
    @RequestMapping(value = "/web/classification/{id}/results", method = RequestMethod.GET)
    String classificationResults(final Principal principal, @PathVariable("id") final int id, final Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("classifications", classificationService.findByUser(principal));
        model.addAttribute("classification", classificationService.findById(id));
        model.addAttribute("results", classificationRepository.findByClassificationConfigId(id));
        LOGGER.debug("[call] /web/home");
        return "classificationResults";
    }
    
    //ACTIONS
    
    @RequestMapping(value = "/web/classification/add", method = RequestMethod.POST)
    String addClassification(final Principal principal, @ModelAttribute WebCreateClassificationDTO dto) {
        final ClassifConfig storedConfig = classificationService.create(principal, dto);
        return "redirect:/web/classification/" + storedConfig.getId();
    }
    
    @RequestMapping(value = "/web/classification/{id}/train/add", method = RequestMethod.POST)
    String addClassificationTrainData(final Principal principal, @PathVariable("id") final long id, @ModelAttribute WebClassificationTrainDataDTO dto) {
        final ClassificationTrainData data = classificationService.addTrainData(id, dto.getTag(), dto.getValue());
        return "redirect:/web/classification/" + id;
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/delete", method = RequestMethod.POST)
    String deleteClassification(final Principal principal, @PathVariable("id") final long id) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (principal != null && principal.getName() != null && cl != null && cl.getUser() != null) {
            if (cl.getUser().equals(principal.getName())) {
                classificationService.delete(id);
            }
        }
        return "/web/classification/";
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/train", method = RequestMethod.POST)
    String trainClassification(final Principal principal, @PathVariable("id") final long id) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (principal != null && principal.getName() != null && cl != null && cl.getUser() != null) {
            if (cl.getUser().equals(principal.getName())) {
                classificationService.train(id);
                return "/web/classification/" + id;
            }
        }
        return "/web/classification/";
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/subscribe", method = RequestMethod.POST)
    String subscribeClassification(final Principal principal, @PathVariable("id") final long id) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (principal != null && principal.getName() != null && cl != null && cl.getUser() != null) {
            if (cl.getUser().equals(principal.getName())) {
                classificationService.subscribe(id);
                return "/web/classification/" + id;
            }
        }
        return "/web/classification/";
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/enable/{state}", method = RequestMethod.POST)
    String trainClassification(final Principal principal, @PathVariable("id") final long id, @PathVariable("state") final String state) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (principal != null && principal.getName() != null && cl != null && cl.getUser() != null) {
            if (cl.getUser().equals(principal.getName())) {
                if (state.equals("true")) {
                    classificationService.enable(id);
                } else {
                    classificationService.enable(id, false);
                }
                return "/web/classification/" + id;
            }
        }
        return "/web/classification/";
    }
    
    //BASE
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
