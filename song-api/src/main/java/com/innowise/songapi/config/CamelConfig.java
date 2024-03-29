package com.innowise.songapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.songapi.route.SqsRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.component.aws2.sqs.Sqs2Component;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;


@Configuration
public class CamelConfig {

    @Bean
    public CamelContext camelContext(ApplicationContext applicationContext,
                                     SqsClient sqsClient,
                                     SqsRouteBuilder sqsRouteBuilder,
                                     ObjectMapper objectMapper) throws Exception {

        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        camelContext.getRegistry().bind("sqsClient", sqsClient);
        camelContext.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");

        Sqs2Component sqs2Component = new Sqs2Component();
        sqs2Component.setCamelContext(camelContext);
        sqs2Component.start();

        camelContext.addRoutes(sqsRouteBuilder);

        return camelContext;
    }
}
