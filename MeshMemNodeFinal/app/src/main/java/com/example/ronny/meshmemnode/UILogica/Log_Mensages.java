package com.example.ronny.meshmemnode.UILogica;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ronny.meshmemnode.R;
import com.example.ronny.meshmemnode.UILogica.MainActivity;
import com.example.ronny.meshmemnode.UILogica.MapaMemoria;

public class Log_Mensages extends Activity {
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__mensages);
        info = (TextView) findViewById(R.id.textLogMensages);
        info.setText(MainActivity.Msjrecibido);
    }
}
