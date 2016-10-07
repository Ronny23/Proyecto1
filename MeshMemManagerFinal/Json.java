package com.example.ronny.meshmemmanager.AccesoDatos;

//Importaciones

import android.util.Base64;

import com.example.ronny.meshmemmanager.EstructurasDatos.Arreglo;
import com.example.ronny.meshmemmanager.UILogica.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Clase que se encarga de parsear y desparsear Json, asi como de interpretar el json recibido
 */
public class Json {

    MainActivity.SocketClienteThread socket;

    /**
     * Constructor
     * @param psocket Instancia de la clase SocketClienteThread
     */
    public Json(MainActivity.SocketClienteThread psocket){
         socket=psocket;
     }

    /**
     * Desparsea json y lo interpreta segun lo recibido
     * @param in string que contiene el json recibido
     * @param pDatos arreglo que contiene algunos datos adicionales necesarios
     * @return return un json como respuesta a lo recibido
     * @throws JSONException
     * @throws IOException
     */
    public String readJsonStream(String in, Arreglo<String> pDatos)
            throws JSONException, IOException {
        JSONObject obj = new JSONObject(in);
        String actividad1 = obj.getString("Actividad");
        if (!actividad1.equals("OK") && !actividad1.equals("extraido") &&
                !actividad1.equals("dato")){socket.ping();}

        switch (actividad1) {
            case "solicitarToken":
                byte[] data = String.valueOf(pDatos.elemento(0)).getBytes("UTF-8");
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                Arreglo<String> arreglo = new Arreglo<>();
                arreglo.add(base64);
                return crearJson(arreglo,1);
            case "agregarMaestro": {
                Arreglo<String> datos = new Arreglo<>();
                datos.add(obj.getString("bytesT"));
                datos.add(obj.getString("telefono"));
                boolean x = socket.agregarMaestro(datos);
                Arreglo<String> datos2 = new Arreglo<>();
                datos2.add(Boolean.toString(x));
                datos2.add(String.valueOf(socket.id));
                return crearJson(datos2,2);
            }
            case "xMalloc": {
                Arreglo<String> datos = new Arreglo<>();
                datos.add(obj.getString("size"));
                datos.add(obj.getString("value"));
                String uuid=socket.xMalloc(datos);
                Arreglo<String> datos2 = new Arreglo<>();
                datos2.add(uuid);
                return crearJson(datos2,4);
            }
            case "desreferenciar":
                Arreglo<String> info2 = new Arreglo<>();
                info2.add(socket.recuperarDato(obj.getString("id")));
                return crearJson(info2,6);
            case "dato":
                return obj.getString("data");
            case "xAssign":
                Arreglo<String> dat = new Arreglo<>();
                dat.add(obj.getString("id"));
                dat.add(obj.getString("value"));
                dat.add(obj.getString("size"));
                socket.xAssign(dat);
                return crearJson(dat,10);
            case "xFree":
                socket.xFree(obj.getString("id"));
                return crearJson(null,10);
            case "asignar":
                socket.asignar(obj.getString("id"));
                return crearJson(null,10);
            case "extraido":
                return obj.getString("dato");
        }
        return null;
    }

    /**
     * Se encarga de crear un json con los datos necesarios segun lo solicitado
     * @param info arreglo que contiene la informacion necsaria para crear el json
     * @param opcion especifica el json a crear
     * @return retorna un string que representa el json creado
     * @throws JSONException
     */
    public String crearJson(Arreglo<String> info, int opcion) throws JSONException {
        if (opcion==1) {
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("token", info.elemento(0));
            jsonEnv.put("Actividad", "token");
            return jsonEnv.toString();
        }
        else if(opcion==2){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("Actividad", "conectado");
            jsonEnv.put("labor", info.elemento(0));
            jsonEnv.put("id", info.elemento(1));
            return jsonEnv.toString();
        }
        else if(opcion==3){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("size",info.elemento(0));
            jsonEnv.put("value",info.elemento(1));
            jsonEnv.put("Actividad","reservarMemoria");
            return jsonEnv.toString();
        }
        else if(opcion==4){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("id",info.elemento(0));
            jsonEnv.put("Actividad","reservado");
            return jsonEnv.toString();
        }

        else if(opcion==6){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("dato",info.elemento(0));
            jsonEnv.put("Actividad","retornarDato");
            return jsonEnv.toString();
        }
        else if(opcion==7){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("index",info.elemento(0));
            jsonEnv.put("Actividad","guardar");
            jsonEnv.put("value",info.elemento(2));
            if (info.elemento(1).equals("Vacio")){jsonEnv.put("uuid","");}
            else{jsonEnv.put("uuid",info.elemento(1));}
            return jsonEnv.toString();
        }
        else if(opcion==8){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("index",info.elemento(0));
            jsonEnv.put("Actividad","recuperardato");
            return jsonEnv.toString();
        }
        else if(opcion==9){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("index",info.elemento(0));
            jsonEnv.put("value",info.elemento(1));
            jsonEnv.put("Actividad","xAssign");
            return jsonEnv.toString();
        }
        else if(opcion==10){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("Actividad","OK");
            return jsonEnv.toString();
        }
        else if(opcion==11){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("index",info.elemento(0));
            jsonEnv.put("Actividad","extraer");
            return jsonEnv.toString();
        }

        else if(opcion==12){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("telefono",info.elemento(0));
            jsonEnv.put("Actividad","agregarTelefono");
            return jsonEnv.toString();
        }

        else if(opcion==13){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("Actividad", "conectado");
            jsonEnv.put("labor", info.elemento(0));
            jsonEnv.put("id", info.elemento(1));
            jsonEnv.put("telefono", info.elemento(2));
            return jsonEnv.toString();
        }

        else if(opcion==14){
            JSONObject jsonEnv = new JSONObject();
            jsonEnv.put("Actividad", "ping");
            return jsonEnv.toString();
        }
        return "";
    }
}