package com.minhacienda.esb.transformation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minhacienda.esb.model.Pago;

@ApplicationScoped
@Named("transformationComponent")
public class TransformationComponent {
    private static final Logger log = LoggerFactory.getLogger(TransformationComponent.class);

    public void processPagoImp001(Pago pago) {
        pago.setValor(pago.getValor()*0.95);
        log.info("Processing Pago IMP_001: {}", pago);
    }

    public void processPagoDefault(Pago pago) {
        log.info("Processing Pago Default: {}", pago);
    }
}
