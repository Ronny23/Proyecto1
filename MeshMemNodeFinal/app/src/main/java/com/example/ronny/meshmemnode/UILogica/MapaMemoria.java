package com.example.ronny.meshmemnode.UILogica;

//Importaciones
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

import com.example.ronny.meshmemnode.R;

/**
 * Clase que se encarga de mostrar la actividad que muestra el mapa de memoria, este se muestra
 * sobre un TableLayout
 */
public class MapaMemoria extends Activity {
    TableLayout tabla;

    /**
     * Metodo que crea el mapa de memoria
     *
     * @param savedInstanceState instancia donde se ejecuta
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapamemoria);
        Button color = (Button) findViewById(R.id.color);
        Button log = (Button) findViewById(R.id.logMensages);
        log.setOnClickListener(buttonlogMensagesOnClickListener);
        color.setOnClickListener(colorOnListener);
        Button informacion = (Button) findViewById(R.id.info);
        informacion.setOnClickListener(infoOnListener);
        desplegar();
    }

    /**
     * Se ejecuta al presionar el boton de informacion, realiza el cambio de pantalla
     */
    View.OnClickListener buttonlogMensagesOnClickListener=
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent CambioPantalla = new Intent(MapaMemoria.this, Log_Mensages.class);
                    startActivity(CambioPantalla);
                }
            };

    public boolean onKeyDown (int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    /**
     * Se encarga de actualizar el tableLayout que representa el mapa de memoria
     */
    public void desplegar() {
        tabla = (TableLayout) findViewById(R.id.lista);
        TextView textView;
        for (int i = 0; i < MainActivity.MemoryList.size(); i++) {
            TableRow row = new TableRow(getBaseContext());
            textView = new TextView(getBaseContext());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(100, 30, 300, 30);
            if (!(MainActivity.MemoryList.getNode(i).get_id().equals(""))) {
                textView.setBackgroundColor(RED);
                textView.setText("Nodo: " + String.valueOf(MainActivity.MemoryList.getNode(i).get_id())
                        + "  Value: " + String.valueOf(MainActivity.MemoryList.getNode(i).getValue()));
            } else {
                textView.setBackgroundColor(BLACK);
                textView.setText("Vacio");
            }
            textView.setTextColor(WHITE);
            row.addView(textView);
            tabla.addView(row);
        }
    }

    /**
     * Actualiza el mapa de memoria al presionar el boton Refrescar
     */
    View.OnClickListener colorOnListener =
            new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(View v) {
                    tabla.removeAllViews();
                    desplegar();
                }
            };

    /**
     * Muestra la actividad de informacion al presionar el boton informacion
     */
    View.OnClickListener infoOnListener =
            new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(View v) {

                    Intent ListSong = new Intent(MapaMemoria.this, Informacion.class);
                    startActivity(ListSong);
                }
            };
}