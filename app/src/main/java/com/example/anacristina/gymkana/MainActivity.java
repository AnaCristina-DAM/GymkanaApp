package com.example.anacristina.gymkana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText nombre;
    EditText edad;
    EditText localidad;
    EditText provincia;
    Button comenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = (EditText) findViewById(R.id.et_nombre);
        edad = (EditText) findViewById(R.id.et_edad);
        localidad = (EditText) findViewById(R.id.et_localidad);
        provincia = (EditText) findViewById(R.id.et_provincia);
        comenzar = (Button) findViewById(R.id.btn_comenzar);

        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Persona persona = new Persona(nombre.getText().toString(),
                        Integer.parseInt(edad.getText().toString()),
                        localidad.getText().toString(),
                        provincia.getText().toString());
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);

            }
        });
    }
}
