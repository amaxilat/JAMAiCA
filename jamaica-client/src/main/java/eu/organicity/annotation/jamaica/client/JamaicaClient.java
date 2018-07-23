package eu.organicity.annotation.jamaica.client;

public class JamaicaClient extends JamaicaAndroidClient {
    public JamaicaClient(final String token) {
        super(token);
    }
    
    public JamaicaClient(final String token, final String baseUrl) {
        super(token, baseUrl);
    }
    
    public JamaicaClient(String client_id, String client_secret, String username, String password) {
        super(client_id, client_secret, username, password);
    }
}
