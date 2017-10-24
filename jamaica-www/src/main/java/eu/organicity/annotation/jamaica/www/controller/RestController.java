package eu.organicity.annotation.jamaica.www.controller;

import eu.organicity.annotation.jamaica.dto.VersionDTO;
import eu.organicity.annotation.jamaica.www.WebClassificationTrainDataDTO;
import eu.organicity.annotation.jamaica.www.WebCreateClassificationDTO;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.model.Classification;
import eu.organicity.annotation.jamaica.www.model.ClassificationTrainData;
import eu.organicity.annotation.jamaica.www.repository.AnomalyConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.AnomalyRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassifConfigRepository;
import eu.organicity.annotation.jamaica.www.repository.ClassificationRepository;
import eu.organicity.annotation.jamaica.www.service.SecurityService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Controller
public class RestController extends BaseController {
    
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(RestController.class);
    private static final String CSV_HEADER_TEXT = "entity,attribute,value,tag,timestamp";
    private static final String CONTENT_DISPOSITION_HEADER = "Content-disposition";
    private static final String ATTACHMENT_PATTERN = "attachment;filename=results-%d.csv";
    private static final String CSV_CONTENT_PATTERN = "%s,%s,%s,%s,%d";
    
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
    @Autowired
    SecurityService securityService;
    
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
    String home(final Model model) {
        model.addAttribute("principal", securityService.getPrincipal());
        model.addAttribute("anomaly", anomalyConfigRepository.findByUser(securityService.getUser()));
        model.addAttribute("classifications", classificationService.findByUser());
        LOGGER.debug("[call] /web/home");
        return "home";
    }
    
    @RequestMapping(value = "/web/anomaly", method = RequestMethod.GET)
    String anomaly(final Model model) {
        model.addAttribute("principal", securityService.getPrincipal());
        model.addAttribute("anomaly", anomalyConfigRepository.findByUser(securityService.getUser()));
        model.addAttribute("classifications", classificationService.findByUser());
        LOGGER.debug("[call] /web/home");
        return "anomaly";
    }
    
    @RequestMapping(value = "/web/classification", method = RequestMethod.GET)
    String classification(final Model model) {
        model.addAttribute("principal", securityService.getPrincipal());
        model.addAttribute("classifications", classificationService.findByUser());
        LOGGER.debug("[call] /web/home");
        return "classification";
    }
    
    @RequestMapping(value = "/web/classification/add", method = RequestMethod.GET)
    String addClassification(final Model model) {
        model.addAttribute("principal", securityService.getPrincipal());
        model.addAttribute("classifications", classificationService.findByUser());
        LOGGER.debug("[call] /web/home");
        return "classificationCreate";
    }
    
    @RequestMapping(value = "/web/classification/{id}", method = RequestMethod.GET)
    String classificationView(@PathVariable("id") int id, Model model) {
        final ClassifConfig classification = classifConfigRepository.findById(id);
        if (securityService.canViewClassifConfig(classification)) {
            model.addAttribute("principal", securityService.getPrincipal());
            model.addAttribute("classifications", classificationService.findByUser());
            model.addAttribute("classification", classificationService.findById(id));
            model.addAttribute("trainData", classificationTrainDataRepository.findByClassificationConfigId(id));
            LOGGER.debug("[call] /web/home");
            return "classificationView";
        } else {
            return "redirect:/web/home/";
        }
    }
    
    @RequestMapping(value = "/web/classification/{id}/results", method = RequestMethod.GET)
    String classificationResults(@PathVariable("id") final int id, final Model model) {
        final ClassifConfig classification = classifConfigRepository.findById(id);
        if (securityService.canViewClassifConfig(classification)) {
            model.addAttribute("principal", securityService.getPrincipal());
            model.addAttribute("classifications", classificationService.findByUser());
            model.addAttribute("classification", classificationService.findById(id));
            
            model.addAttribute("results", classificationRepository.findFirstByClassificationConfigId(id));
            LOGGER.debug("[call] /web/home");
            return "classificationResults";
        } else {
            return "redirect:/web/home/";
        }
    }
    
    @RequestMapping(value = "/web/classification/{id}/results/download", method = RequestMethod.GET)
    String classificationResultsDownload(@PathVariable("id") final int id, HttpServletResponse response) {
        final ClassifConfig classification = classifConfigRepository.findById(id);
        if (securityService.canViewClassifConfig(classification)) {
            response.addHeader(CONTENT_DISPOSITION_HEADER, String.format(ATTACHMENT_PATTERN, id));
            response.setContentType(TEXT_CSV);
            response.setStatus(200);
            ServletOutputStream output = null;
            try {
                output = response.getOutputStream();
                if (output != null) {
                    output.println(CSV_HEADER_TEXT);
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            
            
            final PageRequest page = new PageRequest(0, 2000);
            Page<Classification> results = classificationRepository.findByClassificationConfigId(id, page);
            while (page.getPageNumber() < results.getTotalPages()) {
                for (final Classification result : results) {
                    try {
                        final String resultString = String.format(CSV_CONTENT_PATTERN, result.getEntityId(), result.getEntityAttribute(), result.getAttributeValue(), result.getTag(), result.getStartTime());
                        if (output != null) {
                            output.println(resultString);
                            output.flush();
                        }
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                if (results.nextPageable() == null) {
                    break;
                }
                results = classificationRepository.findByClassificationConfigId(id, results.nextPageable());
            }
            return "";
        } else {
            return "redirect:/web/home/";
        }
    }
    
    //ACTIONS
    
    @RequestMapping(value = "/web/classification/add", method = RequestMethod.POST)
    String addClassification(@ModelAttribute WebCreateClassificationDTO dto) {
        final ClassifConfig storedConfig = classificationService.create(securityService.getPrincipal(), dto);
        return "redirect:/web/classification/" + storedConfig.getId();
    }
    
    @RequestMapping(value = "/web/classification/{id}/train/add", method = RequestMethod.POST)
    String addClassificationTrainData(@PathVariable("id") final long id, @ModelAttribute WebClassificationTrainDataDTO dto) {
        final ClassificationTrainData data = classificationService.addTrainData(id, dto.getTag(), dto.getValue());
        return "redirect:/web/classification/" + id;
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/delete", method = RequestMethod.POST)
    String deleteClassification(@PathVariable("id") final long id) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (securityService.canManageClassifConfig(cl)) {
            classificationService.delete(id);
            return "/web/classification/";
        } else {
            return "redirect:/web/home";
        }
        
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/train", method = RequestMethod.POST)
    String trainClassification(final Principal principal, @PathVariable("id") final long id) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (securityService.canManageClassifConfig(cl)) {
            classificationService.train(id);
            return "/web/classification/" + id;
        } else {
            return "/web/classification/";
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/subscribe", method = RequestMethod.POST)
    String subscribeClassification(final Principal principal, @PathVariable("id") final long id) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (securityService.canManageClassifConfig(cl)) {
            classificationService.subscribe(id);
            return "/web/classification/" + id;
        } else {
            return "/web/classification/";
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/enable/{state}", method = RequestMethod.POST)
    String trainClassification(final Principal principal, @PathVariable("id") final long id, @PathVariable("state") final String state) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (securityService.canManageClassifConfig(cl)) {
            if (state.equals("true")) {
                classificationService.enable(id);
            } else {
                classificationService.enable(id, false);
            }
            return "/web/classification/" + id;
        } else {
            return "/web/classification/";
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/web/classification/{id}/count", method = RequestMethod.GET, produces = "application/json")
    String countClassification(@PathVariable("id") final long id) {
        ClassifConfig cl = classifConfigRepository.findById(id);
        if (securityService.canManageClassifConfig(cl)) {
            JSONObject obj = new JSONObject();
            obj.put("count", classificationRepository.countByClassificationConfigId(id));
            obj.put("id", id);
            return obj.toString();
        } else {
            return "/web/classification/";
        }
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
