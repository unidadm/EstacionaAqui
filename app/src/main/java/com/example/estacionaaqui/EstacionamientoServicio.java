package com.example.estacionaaqui;

public class EstacionamientoServicio {
    private String id;
    private String idestacionamiento;
    private String idservicio;
    private Double tarifa;
    private String descripcion;

    public EstacionamientoServicio() {

    }

    public EstacionamientoServicio(String descripcion, Double tarifa) {
        this.descripcion = descripcion;
        this.tarifa = tarifa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdestacionamiento() {
        return idestacionamiento;
    }

    public void setIdestacionamiento(String idestacionamiento) {
        this.idestacionamiento = idestacionamiento;
    }

    public String getIdservicio() {
        return idservicio;
    }

    public void setIdservicio(String idservicio) {
        this.idservicio = idservicio;
    }

    public Double getTarifa() {
        return tarifa;
    }

    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
