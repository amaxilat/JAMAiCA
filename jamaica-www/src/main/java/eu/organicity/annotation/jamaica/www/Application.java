package eu.organicity.annotation.jamaica.www;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = "eu.organicity.annotation.jamaica.www")
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    /**
     * Initializes this application when running as a standalone application.
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Initializes this application when running in a servlet container (e.g. Tomcat)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}