package com.example.anacristina.gymkana;

/**
 * Created by AnaCristina on 12/02/2018.
 */

public class Lugar {

    private String nombre;
    private double latitud;
    private double longitud;
    private String pista;


    public Lugar(String nombre, double latitud, double longitud, String pista) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.pista = pista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

}
