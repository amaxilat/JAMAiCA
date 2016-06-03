package eu.organicity.annotation.jamaica.www.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket mainApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Version 1")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/api/v1/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Organicity JAMAiCA API")
                .description("Organicity JAMAiCA API")
                .termsOfServiceUrl("http://www.organicity.eu")
                .contact(new Contact("Organicity Helpdesk", "https://support.zoho.com/portal/organicity/home", "helpdesk@organicity.eu)"))
                .version("1").build();
    }

}
