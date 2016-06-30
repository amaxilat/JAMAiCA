package eu.organicity.annotation.jamaica.www.test;

import eu.organicity.annotation.jamaica.www.Application;
import eu.organicity.annotation.jamaica.dto.VersionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class RestControllerTest {

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/v1/version");
        template = new TestRestTemplate();
    }

    @Test
    public void getVersion() throws Exception {
        final ResponseEntity<VersionDTO> response = template.getForEntity(base.toString(), VersionDTO.class);
        assertEquals(response.getBody(), new VersionDTO("1"));
    }
}
