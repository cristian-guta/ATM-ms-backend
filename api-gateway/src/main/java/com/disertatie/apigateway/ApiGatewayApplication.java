package com.disertatie.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import springfox.documentation.swagger.web.UiConfiguration;


@SpringBootApplication
@EnableZuulProxy
@EnableEurekaServer
public class ApiGatewayApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public ServerCodecConfigurer serverCodecConfigurer()
	{
		return ServerCodecConfigurer.create();
	}

	@Bean
	UiConfiguration uiConfig()
	{
		return new UiConfiguration("validatorUrl", "list", "alpha", "schema",
				UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
	}

}
