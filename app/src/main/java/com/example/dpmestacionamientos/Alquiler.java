package com.example.dpmestacionamientos;

import java.util.Date;

public class Alquiler {

    private String id;
    private String idpersonacliente;
    private String idpersonadueno;
    private String idestacionamiento;
    private String distrito;
    private String estacionamiento;

    private String fechainiciostring;
    private String horainiciostring;
    private Integer fechainiciointeger;
    private Integer horainiciointeger;

    private String fechafinstring;
    private String horafinstring;
    private Integer fechafininteger;
    private Integer horafininteger;

    private Integer cantidadhoras;
    private Double preciohora;

    public Alquiler() {

    }

    public Alquiler(String estacionamiento, String fechainiciostring) {
        this.estacionamiento = estacionamiento;
        this.fechainiciostring = fechainiciostring;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdpersonacliente() {
        return idpersonacliente;
    }

    public void setIdpersonacliente(String idpersonacliente) {
        this.idpersonacliente = idpersonacliente;
    }

    public String getIdpersonadueno() {
        return idpersonadueno;
    }

    public void setIdpersonadueno(String idpersonadueno) {
        this.idpersonadueno = idpersonadueno;
    }

    public String getIdestacionamiento() {
        return idestacionamiento;
    }

    public void setIdestacionamiento(String idestacionamiento) {
        this.idestacionamiento = idestacionamiento;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getEstacionamiento() {
        return estacionamiento;
    }

    public void setEstacionamiento(String estacionamiento) {
        this.estacionamiento = estacionamiento;
    }

    public String getFechainiciostring() {
        return fechainiciostring;
    }

    public void setFechainiciostring(String fechainiciostring) {
        this.fechainiciostring = fechainiciostring;
    }

    public String getHorainiciostring() {
        return horainiciostring;
    }

    public void setHorainiciostring(String horainiciostring) {
        this.horainiciostring = horainiciostring;
    }

    public Integer getFechainiciointeger() {
        return fechainiciointeger;
    }

    public void setFechainiciointeger(Integer fechainiciointeger) {
        this.fechainiciointeger = fechainiciointeger;
    }

    public Integer getHorainiciointeger() {
        return horainiciointeger;
    }

    public void setHorainiciointeger(Integer horainiciointeger) {
        this.horainiciointeger = horainiciointeger;
    }

    public String getFechafinstring() {
        return fechafinstring;
    }

    public void setFechafinstring(String fechafinstring) {
        this.fechafinstring = fechafinstring;
    }

    public String getHorafinstring() {
        return horafinstring;
    }

    public void setHorafinstring(String horafinstring) {
        this.horafinstring = horafinstring;
    }

    public Integer getFechafininteger() {
        return fechafininteger;
    }

    public void setFechafininteger(Integer fechafininteger) {
        this.fechafininteger = fechafininteger;
    }

    public Integer getHorafininteger() {
        return horafininteger;
    }

    public void setHorafininteger(Integer horafininteger) {
        this.horafininteger = horafininteger;
    }

    public Integer getCantidadhoras() {
        return cantidadhoras;
    }

    public void setCantidadhoras(Integer cantidadhoras) {
        this.cantidadhoras = cantidadhoras;
    }

    public Double getPreciohora() {
        return preciohora;
    }

    public void setPreciohora(Double preciohora) {
        this.preciohora = preciohora;
    }
}
