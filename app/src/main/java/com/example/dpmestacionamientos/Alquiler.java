package com.example.dpmestacionamientos;

public class Alquiler {

    private String estacionamiento;
    private String fechahora;

    public Alquiler(String estacionamiento, String fechahora) {
        this.estacionamiento = estacionamiento;
        this.fechahora = fechahora;
    }

    public String getEstacionamiento() {
        return estacionamiento;
    }

    public void setEstacionamiento(String estacionamiento) {
        this.estacionamiento = estacionamiento;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

}
