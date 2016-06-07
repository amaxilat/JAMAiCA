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

    /**
     * Launches a jubaclassifier instance in the given port.
     *
     * @param port the port for the jubaclassified RPC interface.
     * @return the {@see Process} object of the launched jubaclassifier instance.
     */
    public Process launchJubaclassifier(int port) {
        try {
            return Runtime.getRuntime().exec("jubaclassifier -p " + port + " -f classifier.json");
        } catch (Exception e) {
            LOGGER.error(e, e);
            return null;
        }
    }

    /**
     * Launches a jubaanomaly instance in the given port.
     *
     * @param port the port for the jubaanomaly RPC interface.
     * @return the {@see Process} object of the launched jubaanomaly instance.
     */
    public Process launchJubaanomaly(int port) {
        try {
            return Runtime.getRuntime().exec("jubaanomaly -p " + port + " -f anomaly.json");
        } catch (Exception e) {
            LOGGER.error(e, e);
            return null;
        }
    }

}
