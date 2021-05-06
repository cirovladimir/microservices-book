package se.magnus.microservices.composite.product;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@ComponentScan("se.magnus")
public class ProductCompositeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}

	@Value("${api.common.title}")
	private String apiTitle;
	@Value("${api.common.description}")
	private String apiDescription;
	@Value("${api.common.version}")
	private String apiVersion;
	@Value("${api.common.termsOfServiceUrl}")
	private String apiTermsOfServiceUrl;
	@Value("${api.common.contact.name}")
	private String apiContactName;
	@Value("${api.common.contact.url}")
	private String apiContactUrl;
	@Value("${api.common.contact.email}")
	private String apiContactEmail;
	@Value("${api.common.license}")
	private String apiLicense;
	@Value("${api.common.licenseUrl}")
	private String apiLicenseUrl;

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	Docket docket(){
		return new Docket(DocumentationType.SWAGGER_2)
		.select()
		.apis(RequestHandlerSelectors.basePackage("se.magnus.microservices.composite.product"))
		.paths(PathSelectors.any())
		.build()
		.globalResponseMessage(RequestMethod.GET, Collections.emptyList())
		.apiInfo(new ApiInfo(
			apiTitle, 
			apiDescription, 
			apiVersion, 
			apiTermsOfServiceUrl, 
			new Contact(apiContactName, apiContactUrl, apiContactEmail), 
			apiLicense, 
			apiLicenseUrl, 
			Collections.emptyList()));
	}

}
