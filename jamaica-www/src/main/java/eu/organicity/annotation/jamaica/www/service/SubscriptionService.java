package eu.organicity.annotation.jamaica.www.service;

/**
 * Created by katdel on 06-Jun-16.
 */

import com.amaxilatis.orion.model.subscribe.OrionEntity;
import com.amaxilatis.orion.model.subscribe.SubscriptionResponse;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import eu.organicity.annotation.jamaica.www.repository.ClassifConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SubscriptionService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);
    
    @Value("${application.baseUrl}")
    protected String baseUrl;
    
    @Autowired
    ClassifConfigRepository classifConfigRepository;
    @Autowired
    OrionService orionService;
    
    @Value("${application.env}")
    private String env;
    
    @Scheduled(cron = "0 * * * * ?")
    void checkSubscriptions() {
        if (!env.equals("production")) {
            return;
        }
        LOGGER.info("Checking subscriptions...");
        for (final ClassifConfig classifConfig : classifConfigRepository.findAll()) {
            if (!classifConfig.isEnable())
                continue;
            long diff = System.currentTimeMillis() - classifConfig.getLastSubscription();
            LOGGER.info(String.format("[classification:%d] last:%d diff:%d", classifConfig.getId(), classifConfig.getLastSubscription(), diff));
            if (diff > 24 * 60 * 60 * 1000) {
                LOGGER.info(String.format("[classification:%d] re-subscribing", classifConfig.getId()));
                
                final OrionEntity e = new OrionEntity();
                e.setId(classifConfig.getIdPat());
                e.setIsPattern("true");
                e.setType(classifConfig.getTypePat());
                String[] cond = new String[1];
                cond[0] = "TimeInstant";
                
                try {
                    // subscribe to Orion
                    SubscriptionResponse r = orionService.subscribeToOrion(e, new String[]{classifConfig.getAttribute()}, baseUrl + "v1/notifyContext/" + classifConfig.getUrlOrion(), cond, "P1D", classifConfig);
                    
                    final String subscriptionId = r.getSubscribeResponse().getSubscriptionId();
                    LOGGER.info(String.format("[classification:%d] re-subscribed subscriptionId:%s", classifConfig.getId(), subscriptionId));
                    
                    classifConfig.setSubscriptionId(r.getSubscribeResponse().getSubscriptionId());
                    classifConfig.setLastSubscription(System.currentTimeMillis());
                    // update classification config entry
                    classifConfigRepository.save(classifConfig);
                    LOGGER.info(String.format("[classification:%d] updated db subscriptionId:%s", classifConfig.getId(), classifConfig.getSubscriptionId()));
                } catch (IOException | DataAccessException er) {
                    LOGGER.error(er.getLocalizedMessage(), er);
                }
            } else {
                LOGGER.info(String.format("[classification:%d] no need to re-subscribe", classifConfig.getId()));
            }
        }
    }
}
