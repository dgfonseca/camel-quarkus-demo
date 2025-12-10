package com.minhacienda.esb.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

import com.minhacienda.esb.model.Pago;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransformationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
       BindyCsvDataFormat bindyCsv = new BindyCsvDataFormat(Pago.class);
       JacksonDataFormat format = new JacksonDataFormat(Pago.class);

        from("direct:transformationRoute")
            .unmarshal(bindyCsv)
            .split(body())
                .log("Processing Pago: ${body}")
                .to("direct:processPago")
            .end()
        .end();

        from("direct:processPago")
            .choice()
                .when(simple("${body.codigoImpuesto} == 'IMP_001'"))
                    .bean("transformationComponent", "processPagoImp001")
                .otherwise()
                    .bean("transformationComponent", "processPagoDefault")
            .end()
            .marshal(format)
            .to("direct:processPagoQueue")
        .end();
    }
}
