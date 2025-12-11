package com.minhacienda.esb.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import com.minhacienda.esb.properties.AmqProducerConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AMQProducerRoute extends RouteBuilder {

    @Inject
    protected AmqProducerConfig amqProducerConfig;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
            .redeliveryDelay(1000)
            .maximumRedeliveries(3)
            .logRetryAttempted(true)
            .retryAttemptedLogLevel(LoggingLevel.WARN)
            .log("Exception Unexpected: ${exception.message}")
        .end();

        from("amqpproducer-component:" + amqProducerConfig.getQueueName())
            .log("Receiving message from AMQ: ${body}")
            .to("direct:transformationRoute")
        .end();
    }
}

