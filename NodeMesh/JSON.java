package com.example.androidclient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ronny on 11/09/16.
 */
public class JSON {
    /**----------------------MODIFICAR-----------------------*/
    @SuppressWarnings("unchecked")
    public JSONObject crearJson(int n) throws JSONException {
        JSONObject jsonEnv = new JSONObject();


        String g=String.valueOf(n);

        //System.out.println(g);
        //System.out.println(g2);
        jsonEnv.put("cantidadBytes", g);
        return jsonEnv;
    }
}
