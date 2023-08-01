package com.innowise.songapi.route;

import com.innowise.songapi.model.SongMetadata;
import com.innowise.songapi.service.SongMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqsRouteBuilder extends RouteBuilder {

    private static final String SQS_URI = "aws2-sqs://%s?amazonSQSClient=#sqsClient&autoCreateQueue=true";

    @Override
    public void configure() {
        from(String.format(SQS_URI,"songmetadata-queue"))
                .convertBodyTo(SongMetadata.class)
                .bean(SongMetadataService.class,"save");
    }
}
