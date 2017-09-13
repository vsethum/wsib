package com.vijays.springboot;


import static springfox.documentation.builders.PathSelectors.regex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vijays.springboot.listener.EmployeeAuditMessageListener;

import hello.Application;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@SpringBootApplication(scanBasePackages={"com.vijays.springboot"})

// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class SpringBootEmployeeRestAPI {
	
	public final static String AUDIT_MESSAGE_QUEUE = "audit-message-queue";
	private static final Logger log = LoggerFactory.getLogger(Application.class);

 
    public static void main(String[] args) {
        SpringApplication.run(SpringBootEmployeeRestAPI.class, args);
    }   
    
   
    //OpenAPI Documentation for this API project
    @Configuration
    @EnableSwagger2
    class SwaggerConfig {
        @Bean
        public Docket productApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    //.apis(RequestHandlerSelectors.basePackage("com.vijays.springboot.controller"))
                    .apis(RequestHandlerSelectors.any())   
                    .paths(regex("/api.*"))
                    .build()
                    .apiInfo(metaData());
        }
        private ApiInfo metaData() {
            ApiInfo apiInfo = new ApiInfo(
                    "Spring Boot REST API's",
                    "REST API's for wsib demo",
                    "1.0",
                    "Terms of service",
                    new Contact("Vijay S", "POC App", "sethumadavan@gmail.com"),
                   "Apache License Version 2.0",
                   "https://www.apache.org/licenses/LICENSE-2.0");
            return apiInfo;
        }
    }
    
    //Rabbit MQ Configuration
    @Bean
	Queue queue() {
		return new Queue(AUDIT_MESSAGE_QUEUE, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(AUDIT_MESSAGE_QUEUE);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
	MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(AUDIT_MESSAGE_QUEUE);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(EmployeeAuditMessageListener receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
}