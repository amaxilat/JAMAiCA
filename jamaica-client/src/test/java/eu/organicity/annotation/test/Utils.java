package eu.organicity.annotation.test;

import eu.organicity.annotation.jamaica.client.JamaicaClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {
    public static JamaicaClient createClient() throws IOException {
        Properties p = new Properties();
        p.load(new FileInputStream("annotations.properties"));
        return new JamaicaClient(p.getProperty("client_id"), p.getProperty("client_secret"), p.getProperty("username"), p.getProperty("password"));
    }
}
