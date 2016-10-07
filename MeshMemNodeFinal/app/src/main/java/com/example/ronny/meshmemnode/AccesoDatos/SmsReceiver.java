package com.example.ronny.meshmemnode.AccesoDatos;
//Importaciones
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.ronny.meshmemnode.UILogica.MainActivity;

import org.json.JSONException;
import java.io.UnsupportedEncodingException;

/**
 * Clase SmsReceiver, clase que extiende de BroadcastReceiver, se ejecuta cuando el celular recibe
 * un mensaje y lo envia a la Actividad MainActivity para que este sea interpretado
 */
public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    /**
     * Metodo que recibe el mensaje y lo envia a la actividad para ser interpretado
     * @param context variable necesaria para capturar el evento
     * @param intent variable necesaria para capturar el evento
     */
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String smsContacto="";
            for (Object sm : sms != null ? sms : new Object[0]) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);
                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();
                smsContacto += address;
                smsMessageStr += smsBody;
            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

            MainActivity.NodeThread inst = MainActivity.instance();
            try {inst.SmsReceived(smsMessageStr,smsContacto);}
            catch (UnsupportedEncodingException | JSONException e) {e.printStackTrace();}
        }
    }
}