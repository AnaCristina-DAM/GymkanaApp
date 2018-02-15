package com.example.anacristina.gymkana;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Jonathan
 */

public class GestionFicheros {

    // Si sólo podremos leer de la tarjeta SD:
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    // Si el dispositivo tiene una tarjeta SD:
    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    // Método que vamos a utilizar para escribir un fichero:
    public static void escribirFichero(String nombre, String texto){
        try {
            if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
                File file = new File(Environment.getExternalStorageDirectory(), nombre );
                OutputStreamWriter outw = new OutputStreamWriter(new FileOutputStream(file));
                outw.write(texto);
                outw.close();
            }
        } catch (Exception e) {
            Log.wtf("ERROR", e.getMessage().toString());

        }
    }

    // Método que vamos a utilizar para leer un fichero:
    public static String leerFichero(String nombre){
        try{
            if(isExternalStorageAvailable()){
                File file = new File(Environment.getExternalStorageDirectory(), nombre);
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String t = br.readLine();
                br.close();
                return t;
            } else {return "";}
        } catch(Exception e){
            Log.wtf("ERROR", e.getMessage().toString());
            return "";}

    }

}
