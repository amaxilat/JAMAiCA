package eu.organicity.annotation.test;

import eu.organicity.annotation.jamaica.client.JamaicaClient;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigListDTO;
import eu.organicity.annotation.jamaica.dto.ClassifStatsDTO;

import java.io.IOException;

public class GetClassifStatsTest {
    
    
    public static void main(String[] args) throws InterruptedException, IOException {
        final JamaicaClient client = Utils.createClient();
        final ClassifConfigListDTO confis = client.listClassificationConfig();
        for (final ClassifConfigDTO classifConfigDTO : confis.getClassificationConfigurations()) {
            final ClassifStatsDTO stats = client.getClassificationStats(classifConfigDTO.getId());
            System.out.println(stats);
        }
    }
}
