package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.jamaica.www.configuration.OrganicityAccount;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrganicityUserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganicityUserDetailsService.class);
    
    public static OrganicityAccount getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            try {
                LOGGER.info("Principal : " + ((KeycloakPrincipal) authentication.getPrincipal()).getName());
                OrganicityAccount oa = new OrganicityAccount((KeycloakPrincipal) authentication.getPrincipal(), authentication.getAuthorities());
                oa.parse();
                return oa;
            } catch (Exception e) {
                SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                SecurityContextHolder.clearContext();
                throw new AccessDeniedException("Access Denied::Not proper Organicity Token", e);
            }
        }
        return null;
    }
}