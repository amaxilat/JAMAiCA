package eu.organicity.annotation.jamaica.www.configuration;

import io.jsonwebtoken.Claims;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * Created by etheodor on 29/1/2015.
 */
public final class OrganicityAccount extends KeycloakPrincipal {
//    private static final Logger LOGGER = LoggerFactory.getLogger(OrganicityAccount.class);
    private String id;
    private String user;
    private Date expiration;
    private String email;
    private Collection<? extends GrantedAuthority> roles;

    public OrganicityAccount(KeycloakPrincipal k, Collection<? extends GrantedAuthority> authorities) {
        super(k.getName(), k.getKeycloakSecurityContext());
        roles = authorities;
    }

    public void parse() throws Exception {
        try {
            JwtParser fwtparser = new JwtParser();
            Claims claims = fwtparser.parseJWT(super.getKeycloakSecurityContext().getTokenString());
            id = claims.getId();
            user = claims.getSubject();
            expiration = claims.getExpiration();
            email = (String) claims.get("email");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (isExperimenter()) {
        }
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getEmail() {
        return email;
    }

    public boolean isExperimenter() {
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals("experimenter"))
                return true;
        }
        return false;
    }

    public boolean isAdministrator() {
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals("administrator"))
                return true;
        }
        return false;
    }

    public boolean isParticipant(String experimentId) {
        if (experimentId == null)
            return false;
        else
            return true;
    }

    private String baseUrl = "https://experimenters.organicity.eu:8443/";

    @Override
    public String toString() {
        return "OrganicityAccount{" + "id='" + id + '\'' + ", user='" + user + '\'' + ", expiration=" + expiration + ", email='" + email + '\'' + ", roles=" + roles + ", baseUrl='" + baseUrl + '\'' + '}';
    }
}
