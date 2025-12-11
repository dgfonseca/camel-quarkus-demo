package com.minhacienda.esb.routes;

import org.apache.camel.builder.RouteBuilder;

import com.minhacienda.esb.properties.SqlProducerConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SqlProducerRoute extends RouteBuilder {

    @Inject
    protected SqlProducerConfig sqlProducerConfig;

    @Override
    public void configure() throws Exception {
        from("direct:sqlProducerRoute")
            .log("Sending message to SQL: ${body}")
            .to("sql:" + sqlProducerConfig.getQuery() + "?dataSource=#datasourceProducer")
        .end();
    }
}
