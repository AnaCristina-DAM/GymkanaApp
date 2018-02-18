package com.example.anacristina.gymkana;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Elementos del layout:
    EditText nom;
    EditText edad;
    EditText localidad;
    EditText provincia;
    Button comenzar;

    // Datos del jugador:
    Persona persona;

    // BASE DE DATOS:
    SQLiteDatabase b_datos;

    // CONSTANTE UTILIZADA PARA SOLICITAR PERMISOS:
    private static final int PERMISOS = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referenciamos los distintos elementos del layout:
        nom = (EditText) findViewById(R.id.et_nombre);
        edad = (EditText) findViewById(R.id.et_edad);
        localidad = (EditText) findViewById(R.id.et_localidad);
        provincia = (EditText) findViewById(R.id.et_provincia);
        comenzar = (Button) findViewById(R.id.btn_comenzar);

        // Ponemos el botón a la escucha:
        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Comprobamos que el usuario haya rellenado todos los campos:

                if ( (nom.getText().length()>0) && (edad.getText().length()>0) && (localidad.getText().length()>0) && (provincia.getText().length()>0) ){

                    // Si el usuario ha rellenado todos los campos, registramos los datos:
                    persona = new Persona(nom.getText().toString(),
                            Integer.parseInt(edad.getText().toString()),
                            localidad.getText().toString(),
                            provincia.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    // Pasamos los datos del jugador a la actividad "MapsActivity":
                    intent.putExtra("JUGADOR", persona);
                    // Iniciamos el juego:
                    System.out.println("----- MAPAS -----");
                    startActivity(intent);

                }

                // Si el usuario no ha rellenado todos los campos, mostramos un mensaje:
                else{
                    String text = getResources().getString(R.string.am_error);
                    Spannable centeredText = new SpannableString(text);
                    centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    Toast.makeText( MainActivity.this, centeredText, Toast.LENGTH_SHORT ).show();
                }

            }
        });

        // Comprobamos que el dispositivo disponga de acceso a Internet:
        if(!conexion(MainActivity.this)){
            // Si el dispositivo no tiene conexión, mostramos un mensaje y cerramos la aplicación:
            // Mensaje:
            String text = getResources().getString(R.string.cx_noConexion);
            Spannable centeredText = new SpannableString(text);
            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            Toast.makeText( MainActivity.this, centeredText, Toast.LENGTH_SHORT ).show();
            // Cerramos la aplicación:
            finish();
        }

        // Comprobamos los permisos:
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, PERMISOS);
        }


        // >>> BASE DE DATOS:

        // Utilizamos un objeto "SQLiteDatabase" para crear una base de datos en caso de que no exista ninguna o conectarnos
        // a la base de datos de la aplicación en caso de que se haya creado previamente:
        b_datos = this.openOrCreateDatabase("GymkanaApp", MODE_PRIVATE, null);

        // Comprobamos si existe la tabla "LUGARES":
        boolean t_existe = false;
        Cursor cursor = b_datos.rawQuery("SELECT DISTINCT name FROM sqlite_master WHERE TYPE='table' AND name= 'LUGARES'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0){
                t_existe = true;
            }
            cursor.close();
        }

        // Si la tabla "LUGARES" existe, la eliminamos:
        if (t_existe){
            String sql_Borrar = "DROP TABLE LUGARES;";
            b_datos.execSQL(sql_Borrar);
        }

        // Creamos la tabla "LUGARES":
        String sql_Tabla = "CREATE TABLE IF NOT EXISTS LUGARES (ID INTEGER PRIMARY KEY, NOMBRE VARCHAR, LATITUD REAL, LONGITUD REAL, PISTA VARCHAR);";
        b_datos.execSQL(sql_Tabla);

        // Introducimos los datos de los lugares en la tabla:
        // LUGAR 1:
        String sql_Insertar1 = "INSERT INTO LUGARES (ID, NOMBRE, LATITUD, LONGITUD, PISTA) " +
                                    "VALUES ('1', " +
                                            "'I.E.S. Juan Bosco'," +
                                            "'39.39192219660964'," +
                                            "'-3.222019672393799'," +
                                            "'Instituto que ocupa la posición correspondiente al primer número primo.');";
        b_datos.execSQL(sql_Insertar1);
        // LUGAR 2:
        String sql_Insertar2 = "INSERT INTO LUGARES (ID, NOMBRE, LATITUD, LONGITUD, PISTA) " +
                                    "VALUES ('2', " +
                                            "'I.E.S. María Zambrano'," +
                                            "'39.39188073913813'," +
                                            "'-3.221096992492676'," +
                                            "'Lugar cuyo nombre hace referencia a la ganadora del Premio Cervantes de 1988.');";
        b_datos.execSQL(sql_Insertar2);
        // LUGAR 3:
        String sql_Insertar3 = "INSERT INTO LUGARES (ID, NOMBRE, LATITUD, LONGITUD, PISTA) " +
                                    "VALUES ('3', " +
                                            "'Multicines CineMancha'," +
                                            "'39.39264769837342'," +
                                            "'-3.219396471977234'," +
                                            "'Lugar que existe gracias a los Hermanos Lumière.');";
        b_datos.execSQL(sql_Insertar3);
        // LUGAR 4:
        String sql_Insertar4 = "INSERT INTO LUGARES (ID, NOMBRE, LATITUD, LONGITUD, PISTA) " +
                                    "VALUES ('4', " +
                                            "'Old Dublin'," +
                                            "'39.39234298855147'," +
                                            "'-3.2206732034683228'," +
                                            "'Ecalp der a ot og.');";
        b_datos.execSQL(sql_Insertar4);
        // LUGAR 5:
        String sql_Insertar5 = "INSERT INTO LUGARES (ID, NOMBRE, LATITUD, LONGITUD, PISTA) " +
                                    "VALUES ('5', " +
                                            "'Cervecería - La Antigua'," +
                                            "'39.39211290066128'," +
                                            "'-3.2222208380699158'," +
                                            "'Cervecería más nueva de la zona.');";
        b_datos.execSQL(sql_Insertar5);

    }

    // Resultado de la solicitud de permisos:
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISOS: {
                // Si la solicitud es cancelada, no se otorgarán permisos a la aplicación y el array resultante estará vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISOS OTORGADOS:
                    String text = getResources().getString(R.string.permisos_otorgados);
                    Spannable centeredText = new SpannableString(text);
                    centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    Toast.makeText(this, centeredText, Toast.LENGTH_SHORT).show();
                } else {
                    // PERMISOS DENEGADOS:
                    String text = getResources().getString(R.string.permisos_denegados);
                    Spannable centeredText = new SpannableString(text);
                    centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    Toast.makeText(this, centeredText, Toast.LENGTH_SHORT).show();
                    // Cerramos la aplicación:
                    finish();
                }
                return;
            }
        }
    }

    // Método encargado de comprobar si el dispositivo tiene conexión:
    public static boolean conexion(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

}

