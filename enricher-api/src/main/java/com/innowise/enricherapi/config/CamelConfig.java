package com.innowise.enricherapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.enricherapi.model.SpotifySongMetadata;
import com.innowise.enricherapi.route.SqsRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.component.aws2.sqs.Sqs2Component;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.io.InputStream;

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

//        camelContext.getTypeConverterRegistry()
//                .addTypeConverter(SpotifySongMetadata.class, InputStream.class,
//                        new SpotifyJsonConverter(objectMapper));

        Sqs2Component sqs2Component = new Sqs2Component();
        camelContext.addComponent("aws2-sqs", sqs2Component);
        sqs2Component.start();

        camelContext.addRoutes(sqsRouteBuilder);

        return camelContext;
    }
}
