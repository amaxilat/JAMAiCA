package eu.organicity.annotation.test;


import eu.organicity.annotation.service.client.AnnotationServiceClient;
import eu.organicity.annotation.service.dto.TagDomainDTO;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TagDomainsTests {
    protected static final Logger LOGGER = Logger.getLogger(TagDomainsTests.class);

    AnnotationServiceClient client;


    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        client = new AnnotationServiceClient("");
    }

    @Test
    public void getTagDomains() throws Exception {
        TagDomainDTO[] domains = client.getTagDomains();

        Assert.assertNotNull(domains);

        for (TagDomainDTO domain : domains) {
            Assert.assertNotNull(domain);
            LOGGER.info(domain);
        }
    }

}
