package com.example.ronny.meshmemnode.UILogica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ronny.meshmemnode.AccesoDatos.JSON;
import com.example.ronny.meshmemnode.EstructurasDatos.Arreglo;
import com.example.ronny.meshmemnode.EstructurasDatos.LinkedList;
import com.example.ronny.meshmemnode.R;

import org.json.JSONException;

/**
 * Actividad principal, crea la ventana que solicita la informacion para conectarse, maneja la
 * lista de memoria y se mantien escuchando al manager.
 */
public class MainActivity extends Activity {
    TextView textResponse;
    EditText editTextAddress, editTextPort, cantidadBytes,telefono;
    static int bytesTotal,bytesDisponibles;
    Button buttonConnect, buttonClear;
    static LinkedList<String> MemoryList;
    private static NodeThread inst;
    public static String Msjrecibido="";
    static NodeThread myClientTask;

    /**
     * Metodo que crea los campos necesarios para solicitar la informacion para conectarse e
     * instancia los componenetes necesarios.
     * @param savedInstanceState instancia donde se ejecuta
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MemoryList = new LinkedList<>();
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        cantidadBytes = (EditText) findViewById(R.id.cantBytes);
        telefono= (EditText) findViewById(R.id.telefono);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        textResponse = (TextView) findViewById(R.id.response);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {textResponse.setText("");}});
    }

    OnClickListener buttonConnectOnClickListener = new OnClickListener() {

        /**
         * Recolecta la informacion y se conecta al servidor, al presionar el boton conectar
         * @param arg0 -
         */
        @Override
        public void onClick(View arg0) {
            myClientTask = new NodeThread(
                    editTextAddress.getText().toString(),
                    Integer.parseInt(editTextPort.getText().toString()),
                    Integer.parseInt(cantidadBytes.getText().toString()),
                    telefono.getText().toString()
            );
            bytesTotal=Integer.parseInt(cantidadBytes.getText().toString());
            bytesDisponibles=bytesTotal;
            for (int i=0; i < bytesTotal/4;i++){
                MemoryList.add("","");
            }
            myClientTask.start();
            inst=myClientTask;
            Intent CambioPantalla = new Intent(MainActivity.this, MapaMemoria.class);
            startActivity(CambioPantalla);
        }
    };

    /**
     * Se ejecuta al cerrar la aplicacion, cierra el servidor.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            myClientTask.socket.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    /**
     * Retorna la instancia para poder ser utilizada cuando se recibe un mensaje
     * @return instancia de la clase nodeThread
     */
    public static NodeThread instance() {
        return inst;
    }

    /**
     * Clase que se mantiene escuchando al servidor y ejecuta las acciones solicitadas
     */
    public class NodeThread extends Thread {

        public Boolean esMaestro= false;
        public Boolean tieneEsclavo = false;
        public String numeroTelefono;
        String dstAddress;
        int dstPort;
        int cantidadBytes;
        String telefono;
        private JSON json;
        public String identificador="";
        Socket socket = null;

        /**
         * Constructor
         * @param addr direccion del servidor
         * @param port puesrto del servidor
         * @param canB cantidad de bytes a aportar
         * @param phone numero de telefono
         */
        NodeThread(String addr, int port, int canB, String phone) {
            telefono=phone;
            dstAddress = addr;
            dstPort = port;
            cantidadBytes = canB-canB%4;
        }

        /**
         * Metodo del thread que se mantiene escuchando la solicitudes del servidor
         */
        @Override
        public void run() {
            json = new JSON(this);
            String jsonEnv;
            try {
                socket = new Socket(dstAddress, dstPort);
                Arreglo<String> datos = new Arreglo<>();
                datos.add(String.valueOf(cantidadBytes));
                datos.add(telefono);
                jsonEnv = json.crearJson(1, datos) + "\n";
                BufferedWriter wr = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));
                wr.write(jsonEnv);
                wr.flush();
                String inputLine;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                while((inputLine = in.readLine()) != null){
                    Msjrecibido+="\n" + inputLine;
                    String enviar = json.readJsonStream(inputLine);
                    if (!json.iniciar(inputLine)){
                        enviar += "\n";
                        wr.write(enviar);
                        wr.flush();}
                    break;
                }
            } catch (IOException | JSONException e) {e.printStackTrace();}
            while (true) {
                BufferedReader in = null;
                try {
                    assert socket != null;
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (SocketException e) {} catch (IOException e) {}

                String inputLine;
                try {
                    assert in != null;
                    while((inputLine = in.readLine()) != null){
                        Msjrecibido+="\n" + inputLine;
                        String enviar;
                        try {
                            if(esMaestro && tieneEsclavo && !json.verificar(inputLine)){
                                enviarMsj(numeroTelefono,inputLine);
                            }
                            enviar=json.readJsonStream(inputLine);
                            enviar += "\n";
                            BufferedWriter wr = new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream()));
                            wr.write(enviar);
                            wr.flush();
                        } catch (java.lang.NullPointerException e) {} catch (JSONException e) {
                        }

                    }
                } catch (IOException e) {
                }catch (java.lang.NullPointerException e) {}
            }
        }

        /**
         * Metodo utilizado para sincronizar al maestro con el esclavo, se utiliza para que el
         * maestro envie un mensaje de sincronizacion al esclavo
         * @param tel numero de telefono del esclavo
         * @param sms mensaje a enviar
         */
        public void enviarMsj(String tel, String sms){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager
                    .sendTextMessage(tel,null,sms,null,null);
        }

        /**
         * Metodo que se llama cuando se recibe un mensaje, para que este sea interpretado
         * @param mensaje mensaje recibido
         * @param contacto numero del telefono de quien se recibio el mensaje
         * @throws UnsupportedEncodingException
         * @throws JSONException
         */
        public void SmsReceived(String mensaje, String contacto)
                throws UnsupportedEncodingException, JSONException {
            if(contacto.equals(numeroTelefono)){
                json.readJsonStream(mensaje);
            }
        }

        /**
         * Guarda en la lista que representa la memoria
         * @param pDatos arreglo que contiene la informacion recibida desde el manager
         */
        public void guardar(Arreglo<String> pDatos){
            MemoryList.getNode(Integer.parseInt(pDatos.elemento(0))).set_id(pDatos.elemento(1));
            MemoryList.getNode(Integer.parseInt(pDatos.elemento(0))).setValue(pDatos.elemento(2));
            if(pDatos.elemento(1).equals("")){
                bytesDisponibles+=4;
            }else {
                bytesDisponibles-=4;
            }
        }

        /**
         * Metodo que assigna un valor a un nodo de la lista que representa la memoria
         * @param pDatos arreglo que contiene la informacion recibida del manager
         *               para realizar la accion,
         */
        public void xAssign(Arreglo<String> pDatos){
            MemoryList.getNode(Integer.parseInt(pDatos.elemento(0))).setValue(pDatos.elemento(1));
        }

        /**
         * Metodo que devuelve al manager un valor guardado
         * @param pIndex indice del elemento en la lista que se desea recuperar
         * @return valor recuperado de la lista
         */
        public String recuperarDato(String pIndex){
            return MemoryList.getNode(Integer.parseInt(pIndex)).getValue();
        }

        /**
         * Se encarga de borrar un dato de la lista que representa la memoria
         * @param pIndex indice del elemento a borrar
         * @return retorna el dato que borro
         */
        public String extraerDato(String pIndex){
            String dato=MemoryList.getNode(Integer.parseInt(pIndex)).getValue();
            MemoryList.getNode(Integer.parseInt(pIndex)).setValue("");
            MemoryList.getNode(Integer.parseInt(pIndex)).set_id("");
            return dato;
        }


        public void Sincronizar() throws JSONException {
            for(int i =0; i<MemoryList.size() ;i++){
                if (!MemoryList.getNode(i).get_id().equals("")){
                    Arreglo<String> datos = new Arreglo<>();
                    datos.add(MemoryList.getNode(i).get_id());
                    datos.add(String.valueOf(i));
                    datos.add(MemoryList.getNode(i).getValue());
                    enviarMsj(this.numeroTelefono, json.crearJson(5, datos));
                }
            }
        }

    }
}