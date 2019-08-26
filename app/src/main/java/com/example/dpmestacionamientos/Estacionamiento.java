package com.example.dpmestacionamientos;

public class Estacionamiento {

    private String nombre;
    private String distrito;
    private String precio;

    public Estacionamiento(String nombre, String distrito, String precio) {
        this.nombre = nombre;
        this.distrito = distrito;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }


}
