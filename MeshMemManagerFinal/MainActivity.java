package com.example.ronny.meshmemmanager.UILogica;

//Importaciones

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ronny.meshmemmanager.AccesoDatos.Json;
import com.example.ronny.meshmemmanager.EstructurasDatos.Arreglo;
import com.example.ronny.meshmemmanager.EstructurasDatos.LinkedList;
import com.example.ronny.meshmemmanager.EstructurasDatos.Lista;
import com.example.ronny.meshmemmanager.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.WHITE;

/**
 * Clase que crea el mapa de memoria y mantiene la comunicacion con los clientes y con los nodos
 */
public class MainActivity extends Activity {
    public static LinkedList<SocketClienteThread> LDmaestros;
    TextView info, infoip, msg;
    Button buttonMostrar, logMensages;
    String message = "";
    Lista<String> Ltoken;
    ServerSocket serverSocket;
    TableLayout lista;
    public static Lista<String> LPmapa;
    public static String Msjrecibido="";

    /**
     * Metodo que instancia los objetos principales y crea el mapa de memoria
     * @param savedInstanceState instancia donde se ejecuta
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        buttonMostrar = (Button) findViewById(R.id.btMostarInfo);
        logMensages = (Button) findViewById(R.id.btLogMensages);
        buttonMostrar.setOnClickListener(buttonMostrarOnClickListener);
        logMensages.setOnClickListener(buttonlogMensagesOnClickListener);
        Ltoken = new Lista<>();
        LDmaestros = new LinkedList<>();
        LPmapa = new Lista<>();
        infoip.setText(getIpAddress());
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
        desplegar();
        GarbageCollector garbageCollector= new GarbageCollector();
        garbageCollector.start();
    }

    /**
     * Se ejecuta al presionar el boton de informacion, realiza el cambio de pantalla
     */
    View.OnClickListener buttonMostrarOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent CambioPantalla = new Intent(MainActivity.this, MostrarInfo.class);
                    startActivity(CambioPantalla);
                }
            };

    /**
     * Se ejecuta al presionar el boton de informacion, realiza el cambio de pantalla
     */
    View.OnClickListener buttonlogMensagesOnClickListener=
            new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent Cambio = new Intent(MainActivity.this,   Log_Mensages.class);
                    startActivity(Cambio);
                }
            };

    /**
     * Se ejecuta al cerrar la aplicacion, cierra el servidor.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serverSocket != null) {
            try {serverSocket.close();} catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Actualiza los bytes libres en un nodo
     * @param valor valor a modificar en los bytes
     * @param id id del nodo que se va a modificar
     */
    public void actualizarBytes(int valor, int id){
        for (int i=0; i<LDmaestros.size(); i++){
            if(LDmaestros.get(i).id==id){
                LDmaestros.get(i).bytesDisponibles+=valor;
                break;
            }
        }
    }

    /**
     * Obtiene la direccion IP del telefono
     * @return retorna la direccion Ip obtenida
     */
    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Direccion IP: "
                                + inetAddress.getHostAddress() + "\n";
                    }
                }
            }

        } catch (SocketException e) {e.printStackTrace();ip+="Something Wrong! "+ e.toString()+"\n";}
        return ip;
    }

    /**
     * Se ejecuta cuando un nodo se desconecta,si el nodo es maestro y no esta sincronizado
     * borrado los bloques de memoria, si el maestro tiene esclavo mantiene al esclavo en el
     * lugar del maestro
     * @param pSocket socket desconectado
     */
    public void caido(Socket pSocket){
        for(int i= 0;i<LDmaestros.size();i++){
            if(LDmaestros.get(i).hostThreadSocket==pSocket){
                if (LDmaestros.get(i).esMaestro && !LDmaestros.get(i).sincronizado){
                    for(int j=0;j<LPmapa.getCant() ;j++){
                        if (String.valueOf(LDmaestros.get(i).id).equals(LPmapa.Elemento(j)
                                .getId())){
                            LPmapa.eliminarElemento(j);
                            j--;
                        }
                    }
                }
                else if(LDmaestros.get(i).esMaestro && LDmaestros.get(i).sincronizado){
                    int idEsclavo=0;
                    for(int k=0; k<LDmaestros.size();k++){
                        if(LDmaestros.get(k).telefonoSincronizacion.equals
                                (LDmaestros.get(i).telefono)){
                            idEsclavo=LDmaestros.get(k).id;
                            break;
                        }
                    }
                    for(int j=0;j<LPmapa.getCant() ;j++){
                        if (String.valueOf(LDmaestros.get(i).id).equals
                                (LPmapa.Elemento(j).getId())){
                            LPmapa.Elemento(j).setId(String.valueOf(idEsclavo));
                            j--;
                        }
                    }
                }
                LDmaestros.delete(i);
                break;
            }
        }
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lista.removeAllViews();
                desplegar();
            }
        });
    }
    /**
     * Crea un UUID que representa el id del bloque de memoria
     * @return UUID creado
     */
    public String uuid()
    {
        String result = java.util.UUID.randomUUID().toString();
        result.replaceAll("-", "");
        result = result.substring(24, 32);
        return result;
    }

    /**
     * Actualiza y despliega el mapa de memoria
     */
    public void desplegar() {
        lista = (TableLayout) findViewById(R.id.lista);
        TextView textView;
        for(int i=0;i<LPmapa.getCant();i++) {
            TableRow row = new TableRow(getBaseContext());
            textView = new TextView(getBaseContext());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(60, 30, 300, 30);
            if(LPmapa.Elemento(i).getInfo().equals("Vacio")){
                textView.setBackgroundColor(GRAY);
                textView.setText("Nodo: "+LPmapa.Elemento(i).getId()+
                        "  Estado: "+ LPmapa.Elemento(i).getInfo()+
                        "  Ref: "+ 0);
            }else {
                textView.setBackgroundColor(GREEN);
                textView.setText("Nodo: "+LPmapa.Elemento(i).getId()+
                        "  Estado: "+ LPmapa.Elemento(i).getInfo()+
                        "  Ref: "+ LPmapa.Elemento(i).getReferecias());
            }
            textView.setTextColor(WHITE);
            row.addView(textView);
            lista.addView(row);
        }
    }

    /**
     * Utilizado para enviar informacion a los nodos
     * @param send string que contiene la informacion a enviar
     * @param ps socket por donde se va a enviar la informacion
     * @param json instancia de json necesaria para leer la respuesta recibida
     * @return string con la respuesta a lo enviado
     * @throws IOException
     * @throws JSONException
     */
    public String sends (String send,Socket ps,Json json) throws IOException, JSONException {
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(ps.getOutputStream()));
            wr.write(send);
            wr.flush();
            wr.write("\n");
            wr.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Msjrecibido += "\n" + inputLine;
                return json.readJsonStream(inputLine, null);
            }
        }catch (java.net.SocketException e){
                caido(ps);
            }
        return null;
    }

    /**
     * Obtiene el indice de un bloque de memoria dentro de un nodo
     * @param index indice del nodo dentro de la lista LPmapa
     * @param pId id del nodo donde buscar
     * @return el indice obtenido
     */
    public int buscarIndex(int index,int pId){
        int indexNodo=-1;
        for(int i=0;i<=index;i++){
            if(Integer.parseInt(LPmapa.Elemento(i).getId())==pId) {
                indexNodo++;
            }
        }
        return indexNodo;
    }

    /**
     * Obtiene el socket de comunicacion de un nodo
     * @param pId id del nodo
     * @return socket obtenido
     */
    public Socket obtenerSocket(int pId){
        for(int i=0;i<LDmaestros.size();i++){
            if(LDmaestros.get(i).id==pId){
                return LDmaestros.get(i).hostThreadSocket;
            }
        }
        return null;
    }

    /**
     * Metodo que realiza Burping entre todos los nodos
     * @param pJason instancia de la clase json
     * @throws JSONException
     * @throws IOException
     */
    public void burping(Json pJason) throws JSONException, IOException {
        int vacios;
        for(int i=0;i<LPmapa.getCant();i++){
            if(LPmapa.Elemento(i).getInfo().equals("Vacio")){
                vacios=i;
                for(int j=(i+1);j<LPmapa.getCant();j++){
                    if(!LPmapa.Elemento(j).getInfo().equals("Vacio")){
                        int index=buscarIndex(j, Integer.parseInt(LPmapa.Elemento(j).getId()));
                        Arreglo<String> datos=new Arreglo<>();
                        datos.add(String.valueOf(index));
                        String value="";
                        value=sends(pJason.crearJson(datos,11),obtenerSocket
                                (Integer.parseInt(LPmapa.Elemento(j).getId())),pJason);
                        datos=new Arreglo<>();
                        int index2=buscarIndex(vacios, Integer.parseInt
                                (LPmapa.Elemento(vacios).getId()));
                        datos.add(String.valueOf(index2));
                        datos.add(LPmapa.Elemento(j).getInfo());
                        datos.add(value);
                        sends(pJason.crearJson(datos,7),obtenerSocket(Integer.parseInt
                                (LPmapa.Elemento(vacios).getId())),pJason);
                        LPmapa.Elemento(vacios).setInfo(LPmapa.Elemento(j).getInfo());
                        LPmapa.Elemento(vacios).setReferecias(2);
                        LPmapa.Elemento(j).setInfo("Vacio");
                        LPmapa.Elemento(j).setReferecias(-1);
                        vacios++;
                    }
                }
                break;
            }
        }
    }

    /**
     * Clase de tipo thread
     * Metodo que realiza el borrado de los bloques de memoria que no tienen referencias, es un
     * metodo que se ejecuta cada cierto tiempo
     */
    private class GarbageCollector extends Thread {
        @Override
        public void run() {
            Json json=new Json(null);
            boolean bandera;
            while(true){
                bandera=false;
                for(int i=0;i<LPmapa.getCant();i++){
                    if(LPmapa.Elemento(i).getReferecias()==0){
                        actualizarBytes(4, Integer.parseInt(LPmapa.Elemento(i).getId()));
                        LPmapa.Elemento(i).setInfo("Vacio");
                        int index=buscarIndex(i, Integer.parseInt(LPmapa.Elemento(i).getId()));
                        Arreglo<String> datos=new Arreglo<>();
                        datos.add(String.valueOf(index));
                        datos.add("");
                        datos.add("");
                        bandera=true;
                        try {
                            sends(json.crearJson(datos,7),obtenerSocket(Integer.parseInt
                                    (LPmapa.Elemento(i).getId())),json);
                        } catch (IOException | JSONException e) {e.printStackTrace();}
                        LPmapa.Elemento(i).setReferecias(-1);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lista.removeAllViews();
                                desplegar();
                            }
                        });
                    }
                }
                try {
                    Thread.sleep(30000);} catch (InterruptedException e) {e.printStackTrace();}
                if (bandera) {
                    try {
                        burping(json);
                    } catch (JSONException | IOException e) {e.printStackTrace();}
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lista.removeAllViews();
                            desplegar();
                        }
                    });
                }
            }
        }
    }

    /**
     * Clase que crea un hilo que se mantiene aceptando nuevas conexiones
     */
    private class SocketServerThread extends Thread {
        static final int SocketServerPORT = 8080;
        int idMaestro = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        info.setText("Escuchando en el puerto: " + serverSocket.getLocalPort());
                    }
                });
                while (true) {
                    Socket socket = serverSocket.accept();
                    idMaestro++;

                    byte[] data = String.valueOf(idMaestro).getBytes("UTF-8");
                    String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                    Ltoken.insertarInicio(base64);

                    SocketClienteThread socketServerReplyThread = new SocketClienteThread(
                            socket, idMaestro,socket.getInetAddress().toString(),socket.getPort());
                    socketServerReplyThread.start();
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * Clase que mantiene la comunicacion con los clientes y nodos y contiene los metodos necesarios
     */
    public class SocketClienteThread extends Thread {
        Socket hostThreadSocket;
        public int id;
        private Json json;
        String ipNode;
        int puerto;
        boolean bandera = true;
        String telefono;
        String telefonoSincronizacion;
        int bytesTotales;
        int bytesDisponibles;
        boolean esMaestro=false;
        boolean sincronizado=false;

        SocketClienteThread(Socket socket, int pId, String pIpnode, int pPuerto) {
            json=new Json(this);
            hostThreadSocket = socket;
            id = pId;
            ipNode=pIpnode;
            puerto=pPuerto;
        }

        /**
         * Metodo que mantiene activa la comunicacion
         */
        @Override
        public void run() {
            try {
                String inputLine;
                BufferedReader in = new BufferedReader(new InputStreamReader
                        (hostThreadSocket.getInputStream()));
                while((inputLine = in.readLine()) != null){
                    Msjrecibido+="\n" + inputLine;
                    String enviar="";
                    try {
                        Arreglo<String> datos = new Arreglo<>();
                        datos.add(String.valueOf(id));
                        enviar=json.readJsonStream(inputLine, datos);
                    } catch (JSONException e) {e.printStackTrace();}
                    BufferedWriter wr= new BufferedWriter(new OutputStreamWriter
                            (hostThreadSocket.getOutputStream()));
                    wr.write(enviar);
                    wr.flush();
                    wr.write("\n");
                    wr.flush();
                    if (!bandera)break;
                }
            } catch (IOException e) {e.printStackTrace();message += "ERROR"+ e.toString()+"\n";}
        }

        /**
         * Agrega un nuevo maestro o esclavo a la lista, y determina si va a ser maestro o esclavo
         * en caso de ser esclavo les agrega los numeros de telefono para que se puedan sincronizar
         * @param pDatos arreglo que contiene la informacion necesaria
         * @return boolean que indica si es maestro
         * @throws JSONException
         * @throws IOException
         */
        public boolean agregarMaestro(Arreglo<String> pDatos) throws JSONException, IOException {
            bytesTotales= Integer.parseInt(pDatos.elemento(0));
            bytesDisponibles=bytesTotales;
            telefono=pDatos.elemento(1);
            if(!verificarEsclavo(bytesTotales)){
                esMaestro=true;
                for (int i = 0; i < Integer.parseInt(pDatos.elemento(0)) / 4; i++) {
                    LPmapa.insertarFinal("Vacio", String.valueOf(id));
                }
            }
            else{
                for(int i=0; i< LDmaestros.size();i++){
                    if(LDmaestros.get(i).esMaestro && !LDmaestros.get(i).sincronizado && LDmaestros.get(i).bytesTotales==bytesTotales){
                        Arreglo<String> datos= new Arreglo<>();
                        datos.add(LDmaestros.get(i).telefono);
                        send(json.crearJson(datos, 12), this.hostThreadSocket);
                        datos= new Arreglo<>();
                        datos.add(this.telefono);
                        this.sincronizado=true;
                        LDmaestros.get(i).sincronizado=true;
                        LDmaestros.get(i).telefonoSincronizacion=this.telefono;
                        this.telefonoSincronizacion=LDmaestros.get(i).telefono;
                        send(json.crearJson(datos,12),LDmaestros.get(i).hostThreadSocket);
                        break;
                    }
                }
            }

            LDmaestros.add(this);
            this.bandera=false;
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lista.removeAllViews();
                    desplegar();
                }
            });
            return esMaestro;
        }

        /**
         * Metodo utilizado para el envio de informacion
         * @param send informacion a enviar
         * @param ps socket por el que se va a enviar
         * @return respuesta recibida
         * @throws IOException
         */
        public String send (String send,Socket ps) throws IOException {
            try {
            BufferedWriter wr= new BufferedWriter(new OutputStreamWriter(ps.getOutputStream()));
            wr.write(send);
            wr.flush();
            wr.write("\n");
            wr.flush();
                BufferedReader in = new BufferedReader(new InputStreamReader(ps.getInputStream()));
                String inputLine;
                try {
                    Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
                while ((inputLine = in.readLine()) != null) {
                    Msjrecibido+="\n" + inputLine;
                    String enviar;
                    try {
                        Arreglo<String> datos = new Arreglo<>();
                        datos.add(String.valueOf(id));
                        enviar = json.readJsonStream(inputLine, datos);
                        return enviar;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (java.net.SocketException e){
                        caido(ps);
                    }
                }
            }
            catch (java.net.SocketException e){
                caido(ps);
            }
            return null;
        }

        /**
         * Metodo utilizado para guardar informacion
         * @param pDatos arreglo con toda la informacion necesaria
         * @return id del bloque de memoria creado
         * @throws IOException
         * @throws JSONException
         */
        public String xMalloc(Arreglo<String> pDatos) throws IOException, JSONException {
            int size= Integer.parseInt(pDatos.elemento(0))/4;
            for(int i=0;i<LPmapa.getCant();i++){
                if(LPmapa.Elemento(i).getInfo().equals("Vacio")){
                    if (size==1){
                        return guardar(i,size,pDatos.elemento(1));
                    }
                    for(int j=i+1;j<=(i+size) && i+size<=LPmapa.getCant();j++){
                        if(!LPmapa.Elemento(j).getInfo().equals("Vacio")){
                            break;
                        }
                        if(j==(i+size-1)){
                            return guardar(i,size,pDatos.elemento(1));
                        }
                    }
                }
            }
            return "FULL";
        }

        /**
         * Le informa al nodo para que guarde la informacion
         * @param pPrimerNodo primer nodo dentro de la lista LPmapa donde se va a guardar
         * @param pSize cantidad de nodos necesarios
         * @param pValue valor a guardar
         * @return id del bloque de memoria
         * @throws IOException
         * @throws JSONException
         */
        public String guardar(int pPrimerNodo,int pSize,String pValue)
                throws IOException, JSONException {
            String uuid=uuid();
            int split=0;
            for(int i=0;i<pSize;i++){
                int index=buscarIndex(pPrimerNodo+i, Integer.parseInt
                        (LPmapa.Elemento(pPrimerNodo + i).getId()));
                Arreglo<String> datos=new Arreglo<>();
                datos.add(String.valueOf(index));
                datos.add(uuid);
                if(!pValue.equals("")){
                    datos.add(pValue.substring(split, split+4));
                    split+=4;
                }
                else{datos.add(pValue);}
                LPmapa.Elemento(pPrimerNodo+i).setInfo(uuid);
                LPmapa.Elemento(pPrimerNodo+i).setReferecias(2);
                actualizarBytes(-4, Integer.parseInt(LPmapa.Elemento(pPrimerNodo + i).getId()));
                send(json.crearJson(datos,7),obtenerSocket(Integer.parseInt
                        (LPmapa.Elemento(pPrimerNodo + i).getId())));
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lista.removeAllViews();
                    desplegar();
                }
            });
            return uuid;
        }

        /**
         * Reduce el conteo de referenciaas de un bloque de memoria
         * @param pId id del bloque de memoria
         */
        public void xFree(String pId){
            for(int i=0;i<LPmapa.getCant();i++){
                if(LPmapa.Elemento(i).getInfo().equals(pId)){
                    while(LPmapa.Elemento(i).getInfo().equals(pId)){
                        LPmapa.Elemento(i).setReferecias(-1);
                        i++;
                    }
                    break;
                }
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lista.removeAllViews();
                    desplegar();
                }
            });
        }

        /**
         * Aumenta el conteo de referenciaas de un bloque de memoria
         * @param pId id del bloque de memoria
         */
        public void asignar(String pId){
            for(int i=0;i<LPmapa.getCant();i++){
                if(LPmapa.Elemento(i).getInfo().equals(pId)) {
                    while (LPmapa.Elemento(i).getInfo().equals(pId)){
                        LPmapa.Elemento(i).setReferecias(1);
                        i++;
                    }
                    break;
                }
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lista.removeAllViews();
                    desplegar();
                }
            });
        }

        /**
         * Devuelve un dato almacenado
         * @param pUuid id del bloque de memoria donde esta el dato
         * @ret
         * urn dato almacenado
         * @throws JSONException
         * @throws IOException
         */
        public String recuperarDato(String pUuid) throws JSONException, IOException {
            String dato="";
            for(int i=0;i<LPmapa.getCant();i++){
                if(LPmapa.Elemento(i).getInfo().equals(pUuid)){
                    while(i<LPmapa.getCant() && LPmapa.Elemento(i).getInfo().equals(pUuid)){
                        Arreglo<String> datos=new Arreglo<>();
                        datos.add(String.valueOf(buscarIndex(i, Integer.parseInt
                                (LPmapa.Elemento(i).getId()))));
                        dato+=send(json.crearJson(datos,8),obtenerSocket(Integer.parseInt
                                (LPmapa.Elemento(i).getId())));
                        try {
                            Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
                        i++;
                    }
                    byte[] decod = Base64.decode(dato, Base64.DEFAULT);
                    return new String(decod, "UTF-8");
                }
            }
            return "";
        }

        /**
         * Verifca que los nodos esten en funcionamiento
         *
         * @throws JSONException
         * @throws IOException
         */
        public void ping() throws JSONException, IOException {
            for(int i =0; i<LDmaestros.size();i++){
                LDmaestros.get(i).hostThreadSocket.setKeepAlive(true);
                send(json.crearJson(null,14),LDmaestros.get(i).hostThreadSocket);
            }
        }

        /**
         * Assigna un valor a un bloque de memoria
         * @param pDatos arreglo que contiene la informacion necesaria
         * @throws JSONException
         * @throws IOException
         */
        public void xAssign(Arreglo<String> pDatos) throws JSONException, IOException {
            int split=0;
            for(int i=0;i<LPmapa.getCant();i++){
                if(LPmapa.Elemento(i).getInfo().equals(pDatos.elemento(0))){
                    while(LPmapa.Elemento(i).getInfo().equals(pDatos.elemento(0))){
                        int index=buscarIndex(i, Integer.parseInt(LPmapa.Elemento(i).getId()));
                        Arreglo<String> datos=new Arreglo<>();
                        datos.add(String.valueOf(index));
                        datos.add(pDatos.elemento(1).substring(split, split+4));
                        split+=4;
                        send(json.crearJson(datos,9),obtenerSocket(Integer.parseInt
                                (LPmapa.Elemento(i).getId())));
                        i++;
                    }
                    break;
                }
            }
        }
    }

    public boolean verificarEsclavo(int size){
        for(int i=0; i<LDmaestros.size(); i++){
            if(LDmaestros.get(i).esMaestro && !LDmaestros.get(i).sincronizado
                    && LDmaestros.get(i).bytesTotales==size){
                return true;
            }
        }
        return false;
    }
}