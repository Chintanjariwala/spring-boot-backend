package com.demo.springbootemployee.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.demo.springbootemployee"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Employee REST APIs",
				"This REST API is very good",
				"Version-1.0",
				"https://www.shopspring.com/terms",
				new Contact("Chintan Jariwala", "www.example.com", "chintan@example.com"),
				"GNU Public V2.0",
				"https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html",
				Collections.emptyList());
	}

}
