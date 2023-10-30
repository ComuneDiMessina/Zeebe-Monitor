package it.almaviva.eai.zeebe.monitor.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import it.almaviva.eai.zeebe.monitor.ZeebeSimpleMonitorApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .forCodeGeneration(true);


        docket = docket.select()
                .apis(RequestHandlerSelectors.basePackage("it.almaviva.eai.zeebe.monitor"))
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build() .protocols(Sets.newHashSet("http", "https"));

        return docket;
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ZeebeMonitorBeAPI")
                .description("Servizi REST Workflow")
                .version(ZeebeSimpleMonitorApp.class.getPackage().getImplementationVersion())
                .build();
    }

}
