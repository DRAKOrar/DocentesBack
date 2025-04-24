package com.pagoDocente.dto;

import lombok.Data;

@Data
public class PagoRequest {
    private String tipoContrato;
    private int horasDiurnas;
    private int horasNocturnas;
    private int horasDominicales;
    private int horasFestivas;

    public PagoRequest() {
    }

    public PagoRequest(String tipoContrato, int horasDiurnas, int horasNocturnas, int horasDominicales, int horasFestivas) {
        this.tipoContrato = tipoContrato;
        this.horasDiurnas = horasDiurnas;
        this.horasNocturnas = horasNocturnas;
        this.horasDominicales = horasDominicales;
        this.horasFestivas = horasFestivas;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public int getHorasDiurnas() {
        return horasDiurnas;
    }

    public void setHorasDiurnas(int horasDiurnas) {
        this.horasDiurnas = horasDiurnas;
    }

    public int getHorasNocturnas() {
        return horasNocturnas;
    }

    public void setHorasNocturnas(int horasNocturnas) {
        this.horasNocturnas = horasNocturnas;
    }

    public int getHorasDominicales() {
        return horasDominicales;
    }

    public void setHorasDominicales(int horasDominicales) {
        this.horasDominicales = horasDominicales;
    }

    public int getHorasFestivas() {
        return horasFestivas;
    }

    public void setHorasFestivas(int horasFestivas) {
        this.horasFestivas = horasFestivas;
    }
}