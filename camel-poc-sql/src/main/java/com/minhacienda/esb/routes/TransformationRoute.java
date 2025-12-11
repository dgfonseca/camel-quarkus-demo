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
       JacksonDataFormat format = new JacksonDataFormat(Pago.class);

        from("direct:transformationRoute")
            .unmarshal(format)
            .process(exchange -> {
                Pago pago = exchange.getIn().getBody(Pago.class);
                exchange.getMessage().setHeader("nit", pago.getNit());
                exchange.getMessage().setHeader("valor", pago.getValor());
                exchange.getMessage().setHeader("fecha_recaudo", pago.getFechaRecaudo());
                exchange.getMessage().setHeader("codigo_impuesto", pago.getCodigoImpuesto());
            })
            .to("direct:sqlProducerRoute")
        .end();
    }
}
