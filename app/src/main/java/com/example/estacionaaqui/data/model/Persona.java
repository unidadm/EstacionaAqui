package com.example.estacionaaqui.data.model;

public class Persona {

  private String id;
  private String nombre;
    private String apellido;
    private String correo;
    private String contraseña;
    private  boolean tipo;

    public Persona(){}

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getId() {
        return id;
    }
public boolean getTipo()
{
    return  tipo;
}
    public String getContraseña() {
        return contraseña;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
