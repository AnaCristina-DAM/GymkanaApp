package com.example.anacristina.gymkana;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Elementos del layout:
    private Toolbar toolbar;
    private GoogleMap mMap;

    // Datos del jugador:
    Persona persona;

    // BASE DE DATOS:
    SQLiteDatabase b_datos;

    // ARRAY - Lugares:
    ArrayList<Lugar> lugares;

    // LUGAR:
    Lugar lugar;

    // INDICE:
    int indice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // TOOLBAR - Menú de opciones del juego:
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // JUGADOR:
        // Recogemos los datos del jugador:
        persona = (Persona) getIntent().getExtras().get("JUGADOR");
        //System.out.println("JUGADOR: " + persona.getNombre());

        // >>> BASE DE DATOS:

        // Utilizamos un objeto "SQLiteDatabase" para conectarnos a la base de datos de la aplicación:
        b_datos = this.openOrCreateDatabase("GymkanaApp", MODE_PRIVATE, null);

        // ARRAY - Lugares:
        lugares = new ArrayList<Lugar>();

        // Obtenemos los datos almacenados en la base de datos:

        Cursor c = b_datos.rawQuery("SELECT * FROM LUGARES;", null);

        if (c.getCount() >= 1) {

            c.moveToFirst();
            do {

                System.out.println("RECUPERAR: " + c.getString(c.getColumnIndex("ID")));
                System.out.println(c.getString(c.getColumnIndex("NOMBRE")));
                System.out.println(c.getDouble(c.getColumnIndex("LATITUD")));
                System.out.println(c.getDouble(c.getColumnIndex("LONGITUD")));

                // Recuperamos la información del lugar:
                Lugar x = new Lugar(c.getString(c.getColumnIndex("NOMBRE")),
                        c.getDouble(c.getColumnIndex("LATITUD")),
                        c.getDouble(c.getColumnIndex("LONGITUD")),
                        c.getString(c.getColumnIndex("PISTA")));

                // Añadimos el lugar a nuestro "ArrayList":
                lugares.add(x);

            }
            while (c.moveToNext());

        }

        // LUGAR:
        indice = 0;
        lugar = lugares.get(0);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        // Comprobamos la ubicación:
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        // Comprobamos si el dispositivo tiene la ubicación activada:
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            System.out.println("-------------------- MAPS: Comprobar GPS");

            // GPS - CONECTADO:

            //Miramos si tiene los permisos necesarios:
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                System.out.println("-------------------- MAPAS: OnMapReady");
                mMap.setMyLocationEnabled(true);

                // Añadimos un marcador en la posición de inicio:
                //LatLng inicio = new LatLng(posicionActual.getLatitude(), posicionActual.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(inicio).title("INICIO"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(inicio));

                return;

            }
            else{
                // Si no tenemos permisos, mostramos un mensaje y cerramos la aplicación:
                // PERMISOS DENEGADOS:
                String text = getResources().getString(R.string.permisos_denegados);
                Spannable centeredText = new SpannableString(text);
                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                Toast.makeText(this, centeredText, Toast.LENGTH_SHORT).show();
                // Cerramos la aplicación:
                finish();
            }
        }
        else{
            // UBICACIÓN - NO CONECTADO:
            // Si la ubicación no está activada, mostramos un mensaje:
            String text = getResources().getString(R.string.cx_noGPS);
            Spannable centeredText = new SpannableString(text);
            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            Toast.makeText(this, centeredText, Toast.LENGTH_SHORT).show();
            // Cerramos la aplicación:
            finish();
        }

    }

    // Inflamos el layout para mostrar los items del menú:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // El método ".inflate()" nos permite inflar el layout del menú y crear los items del mismo; para ello,
        // pasamos como parámetros tanto el layout con los items como el menú a inflar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Configuramos las opciones del menú:
    @Override
    public boolean onOptionsItemSelected(MenuItem opcion_menu) {

        // Almacenamos el ID de la opción selecionada:
        int opcion = opcion_menu.getItemId();

        // COMPROBAR:
        if (opcion == R.id.opt_comprobar) {

            // Comprobamos la ubicación:
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            // Comprobamos si el dispositivo tiene la ubicación activada:
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

                System.out.println("-------------------- MAPS: Comprobar GPS");

                // UBICACIÓN - CONECTADO:

                //Miramos si tiene los permisos necesarios:
                if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                    System.out.println("-------------------- GPS: Comprobar posición");

                    //Recojemos la última posición conocida por el GPS:
                    Location posicionActual = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));


                    // Comprobamos que la posición no sea "NULL":
                    if (posicionActual != null){
                        // POSICION ACTUAL:
                        System.out.println(posicionActual.getLatitude());
                        System.out.println(posicionActual.getLongitude());

                        //Recojemos los datos del puntos:
                        Location posicionJuego = new Location("Punto del juego");
                        posicionJuego.setLatitude(lugares.get(indice).getLatitud());
                        posicionJuego.setLongitude(lugares.get(indice).getLongitud());

                        //Miramos cuanta distancia hay entre los dos puntos:
                        float distance = posicionActual.distanceTo(posicionJuego);

                        //Si hay menos de 2 metros:
                        if (distance<2){

                            // UBICACIÓN CORRECTA:
                            // Si es la última ubicación, mostramos un mensaje y almacenamos los datos del juego:
                            if (indice == lugares.size() - 1){

                                // Construimos el mensaje:
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                // TÍTULO:
                                String titulo = getResources().getString(R.string.fin_titulo);
                                Spannable centeredTitulo = new SpannableString(titulo);
                                centeredTitulo.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, titulo.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                builder.setTitle(centeredTitulo);
                                // MENSAJE:
                                String texto = getResources().getString(R.string.fin_mensaje);
                                Spannable centeredTexto = new SpannableString(texto);
                                centeredTexto.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, texto.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                builder.setMessage(centeredTexto);
                                // BOTÓN POSITIVO:
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        dialog.cancel();
                                    }
                                });

                                // Mostramos el mensaje:
                                AlertDialog mensaje = builder.create();
                                mensaje.show();

                                // Bloqueamos los botones:
                                findViewById(R.id.opt_comprobar).setEnabled(false);
                                findViewById(R.id.opt_pista).setEnabled(false);

                                // Almacenamos los datos del juego:
                                // >>>>> PARTE DE JONATHAN

                            }
                            // Si no es la última ubicación, mostramos un mensaje y avanzamos a la ubicación siguiente:
                            else{
                                // Mensaje:
                                String text = getResources().getString(R.string.ub_correcta);
                                Spannable centeredText = new SpannableString(text);
                                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                Toast.makeText(MapsActivity.this, centeredText, Toast.LENGTH_SHORT ).show();
                                // Lugar:
                                indice = indice + 1;
                                lugar = lugares.get(indice);
                            }
                        }
                        //Si hay más de 2 metros:
                        else{
                            // UBICACIÓN INCORRECTA:
                            // Mensaje:
                            String text = getResources().getString(R.string.ub_incorrecta);
                            Spannable centeredText = new SpannableString(text);
                            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            Toast.makeText(MapsActivity.this, centeredText, Toast.LENGTH_SHORT ).show();
                        }
                    }
                    else{
                        // MENSAJE:
                        String text = getResources().getString(R.string.ps_error);
                        Spannable centeredText = new SpannableString(text);
                        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        Toast.makeText(this, centeredText, Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    // Si no tenemos permisos, mostramos un mensaje:
                    // PERMISOS DENEGADOS:
                    String text = getResources().getString(R.string.permisos_denegados);
                    Spannable centeredText = new SpannableString(text);
                    centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    Toast.makeText(this, centeredText, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                // UBICACIÓN - NO CONECTADO:
                // Si la ubicación no está activada, mostramos un mensaje:
                String text = getResources().getString(R.string.cx_noGPS);
                Spannable centeredText = new SpannableString(text);
                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                Toast.makeText(this, centeredText, Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        // PISTA:
        if(opcion==R.id.opt_pista){

            // Construimos el mensaje:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // TÍTULO:
            String titulo = getResources().getString(R.string.d_titulo);
            Spannable centeredTitulo = new SpannableString(titulo);
            centeredTitulo.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, titulo.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setTitle(centeredTitulo);
            // MENSAJE:
            String texto = lugar.getPista();
            Spannable centeredTexto = new SpannableString(texto);
            centeredTexto.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0, texto.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setMessage(centeredTexto);
            // BOTÓN POSITIVO:
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    dialog.cancel();
                }
            });

            // Mostramos el mensaje:
            AlertDialog mensaje = builder.create();
            mensaje.show();

            return true;
        }

        return false;

    }

}
