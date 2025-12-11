package com.minhacienda.esb.model;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * POJO representing a payment record from CSV file
 */
@CsvRecord(separator = ",", skipFirstLine = true)
public class Pago {

    @DataField(pos = 1, columnName = "id_pago")
    private String idPago;

    @DataField(pos = 2, columnName = "nit")
    private String nit;

    @DataField(pos = 3, columnName = "valor")
    private double valor;

    @DataField(pos = 4, columnName = "fecha_recaudo")
    private String fechaRecaudo;

    @DataField(pos = 5, columnName = "codigo_impuesto")
    private String codigoImpuesto;

    // Default constructor required for unmarshalling
    public Pago() {
    }

    // Constructor with all fields
    public Pago(String idPago, String nit, double valor, String fechaRecaudo, String codigoImpuesto) {
        this.idPago = idPago;
        this.nit = nit;
        this.valor = valor;
        this.fechaRecaudo = fechaRecaudo;
        this.codigoImpuesto = codigoImpuesto;
    }

    // Getters and Setters
    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getFechaRecaudo() {
        return fechaRecaudo;
    }

    public void setFechaRecaudo(String fechaRecaudo) {
        this.fechaRecaudo = fechaRecaudo;
    }

    public String getCodigoImpuesto() {
        return codigoImpuesto;
    }

    public void setCodigoImpuesto(String codigoImpuesto) {
        this.codigoImpuesto = codigoImpuesto;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago='" + idPago + '\'' +
                ", nit='" + nit + '\'' +
                ", valor='" + valor + '\'' +
                ", fechaRecaudo='" + fechaRecaudo + '\'' +
                ", codigoImpuesto='" + codigoImpuesto + '\'' +
                '}';
    }
}

