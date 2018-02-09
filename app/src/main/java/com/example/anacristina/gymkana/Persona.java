package com.example.anacristina.gymkana;

/**
 * Created by Azahara on 09/02/2018.
 */

public class Persona {

    private String nombre;
    private int edad;
    private String localidad;
    private String provincia;

    public Persona(String nombre) {
        this.nombre = nombre;
    }

    public Persona(String nombre, int edad, String localidad, String provincia) {
        this.nombre = nombre;
        this.edad = edad;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
