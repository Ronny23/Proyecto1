package com.example.ronny.meshmemmanager.UILogica;

//Importaciones

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ronny.meshmemmanager.R;

/**
 * Clase que crea la actividad que muestra la informacion de los nodos
 */
public class MostrarInfo extends Activity {

    private String[] sistemas;

    /**
     * Crea los componentes necesarios para mostrar la informacion
     * @param savedInstanceState instancia donde se va a mostrar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_info);
        ListView list = (ListView) findViewById(R.id.listview);
        formarArreglo();
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sistemas);
        list.setAdapter(adaptador);

    }

    /**
     * Actualiza y muestra la informacion de los nodos
     */
    public void formarArreglo(){
        sistemas = new String[MainActivity.LDmaestros.size()];
        for(int i=0;i<MainActivity.LDmaestros.size();i++){
            String str;
            str = "ID: " + MainActivity.LDmaestros.get(i).id+"\n";
            str = str + "Ip Node: " + MainActivity.LDmaestros.get(i).ipNode+"\n";
            str = str + "Puerto del Dispositivo: " + MainActivity.LDmaestros.get(i).puerto+"\n";
            str = str + "Numero del Telefono: " + MainActivity.LDmaestros.get(i).telefono+"\n";
            str = str + "Bytes Totales: " + MainActivity.LDmaestros.get(i).bytesTotales+"\n";
            str = str + "Bytes en uso: " + (MainActivity.LDmaestros.get(i).
                    bytesTotales-MainActivity.LDmaestros.get(i).bytesDisponibles);
            sistemas[i]=str;
        }
    }
}