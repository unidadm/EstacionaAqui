package com.example.estacionaaqui;

public class Estacionamiento {
    private String id;
    private String idpersona;
    private String nombre;
    private String direccion;
    private String distrito;
    private String direcciongooglemaps;
    private String telefono;
    private Double preciohora;
    private Double largo;
    private Double ancho;
    private Double altura;
    private String tipo;
    private String ubicacion;
    private String rutaimagen;
    private Double latitud;
    private Double longitud;

    public Estacionamiento() {

    }

    public Estacionamiento(String nombre, String distrito, Double preciohora) {
        this.nombre = nombre;
        this.distrito = distrito;
        this.preciohora = preciohora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(String idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDirecciongooglemaps() {
        return direcciongooglemaps;
    }

    public void setDirecciongooglemaps(String direcciongooglemaps) {
        this.direcciongooglemaps = direcciongooglemaps;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getPreciohora() {
        return preciohora;
    }

    public void setPreciohora(Double preciohora) {
        this.preciohora = preciohora;
    }

    public Double getLargo() {
        return largo;
    }

    public void setLargo(Double largo) {
        this.largo = largo;
    }

    public Double getAncho() {
        return ancho;
    }

    public void setAncho(Double ancho) {
        this.ancho = ancho;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getRutaimagen() {
        return rutaimagen;
    }

    public void setRutaimagen(String rutaimagen) {
        this.rutaimagen = rutaimagen;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
