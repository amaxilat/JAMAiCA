package eu.organicity.annotation.jamaica.www.service;


import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class SecurityService {
    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    
    @Autowired
    OrganicityUserDetailsService organicityUserDetailsService;
    
    public Principal getPrincipal() {
        return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    public boolean isAdmin() {
        return organicityUserDetailsService.getCurrentUser().isAdministrator();
    }
    
    public String getUser() {
        return organicityUserDetailsService.getCurrentUser().getUser();
    }
    
    public String getEmail() {
        return organicityUserDetailsService.getCurrentUser().getEmail();
    }
    
    public boolean canViewClassifConfig(final ClassifConfig classification) {
        return isAdmin() || (classification.getUser() != null && classification.getUser().equals(getUser()));
    }
    
    public boolean canManageClassifConfig(final ClassifConfig config) {
        return isAdmin() || (getUser() != null && config != null && config.getUser() != null && config.getUser().equals(getUser()));
    }
}
