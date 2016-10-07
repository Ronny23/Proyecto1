package com.example.ronny.meshmemnode.AccesoDatos;

//Importaciones

import com.example.ronny.meshmemnode.EstructurasDatos.Arreglo;
import com.example.ronny.meshmemnode.UILogica.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

/**
 * Clase que se encarga de parsear y desparsear Json
 */
public class JSON {

    MainActivity.NodeThread socket;

    /**
     * Constructor
     * @param pSocket instancia de la clase NodeThread
     */
    public JSON(MainActivity.NodeThread pSocket){
        socket=pSocket;
    }

    /**
     * Metodo que se encarga de crear un objeto Json segun lo solicitado
     * @param opcion indica el json que se debe crear
     * @param datos Arreglo que contiene la informacion a introducir en el json
     * @return retorna un estring que representa el json creado
     * @throws JSONException
     */
    public String crearJson(int opcion, Arreglo<String> datos) throws JSONException {
        JSONObject jsonEnv = new JSONObject();
        if(opcion ==1 ){
            jsonEnv.put("Actividad", "agregarMaestro");
            jsonEnv.put("bytesT", datos.elemento(0));
            jsonEnv.put("telefono", datos.elemento(1));
            return jsonEnv.toString();
        }
        else if (opcion==2){
            jsonEnv.put("Actividad", "dato");
            jsonEnv.put("data", datos.elemento(0));
            return jsonEnv.toString();
        }
        else if(opcion==3){
            jsonEnv.put("Actividad", "OK");
            return jsonEnv.toString();
        }
        else if(opcion==4){
            jsonEnv.put("Actividad", "extraido");
            jsonEnv.put("dato", datos.elemento(0));
            return jsonEnv.toString();
        }
        else if(opcion==5){
            jsonEnv.put("index",datos.elemento(1));
            jsonEnv.put("Actividad","guardar");
            jsonEnv.put("value",datos.elemento(2));
            jsonEnv.put("uuid",datos.elemento(0));
            return jsonEnv.toString();
        }
        return "";
    }

    /**
     * Metodo que desparsea e interpreta el json recibido, ademas que genera una respuesta segun
     * lo recibido
     * @param in string recibido en formato json
     * @return retorna un json que se genera como respuesta
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public String readJsonStream(String in) throws JSONException, UnsupportedEncodingException {
        JSONObject obj = new JSONObject(in);
        String actividad1 = obj.getString("Actividad");
        switch (actividad1) {
            case "conectado":
                socket.identificador = obj.getString("id");
                if (obj.getString("labor").equals("true")) {
                    socket.esMaestro = true;
                }
                return crearJson(3,null);
            case "guardar":
                Arreglo<String> datos = new Arreglo<>();
                datos.add(obj.getString("index"));
                datos.add(obj.getString("uuid"));
                datos.add(obj.getString("value"));
                socket.guardar(datos);
                return crearJson(3, datos);
            case "recuperardato":
                Arreglo<String> dato2 = new Arreglo<>();
                dato2.add(socket.recuperarDato(obj.getString("index")));
                return crearJson(2, dato2);
            case "xAssign":
                Arreglo<String> dat = new Arreglo<>();
                dat.add(obj.getString("index"));
                dat.add(obj.getString("value"));
                socket.xAssign(dat);
                return crearJson(3, dat);
            case "extraer":
                Arreglo<String> dat2 = new Arreglo<>();
                dat2.add(socket.extraerDato(obj.getString("index")));
                return crearJson(4, dat2);
            case "agregarTelefono":
                socket.numeroTelefono="+506"+obj.getString("telefono");
                socket.tieneEsclavo=true;
                socket.Sincronizar();
                return crearJson(3, null);
            case "ping":
                return crearJson(3, null);
        }
        return null;
    }

    /**
     * Verifica que el mensaje no sea un mensaje de ping para sincronizarlo al esclavo
     * @param in string en formato json que se recibe del manager
     * @return retorna boolean que indica se la actividad es ping
     * @throws JSONException
     */
    public boolean verificar(String in) throws JSONException {
        JSONObject obj = new JSONObject(in);
        String actividad = obj.getString("Actividad");
        return actividad.equals("ping") || actividad.equals("recuperardato");
    }

    public Boolean iniciar(String in) throws JSONException, UnsupportedEncodingException {
        JSONObject obj = new JSONObject(in);
        String actividad1 = obj.getString("Actividad");
        return actividad1.equals("conectado");
    }
}