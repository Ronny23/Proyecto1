package com.example.androidclient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
	
	TextView textResponse;
	EditText editTextAddress, editTextPort,cantidadBytes;
	Button buttonConnect, buttonClear;
	protected DataOutputStream dos;
	protected DataInputStream dis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editTextAddress = (EditText)findViewById(R.id.address);
		editTextPort = (EditText)findViewById(R.id.port);
		cantidadBytes= (EditText)findViewById(R.id.cantBytes);
		buttonConnect = (Button)findViewById(R.id.connect);
		buttonClear = (Button)findViewById(R.id.clear);
		textResponse = (TextView)findViewById(R.id.response);
		
		buttonConnect.setOnClickListener(buttonConnectOnClickListener);
		
		buttonClear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				textResponse.setText("");
			}});
	}
	
	OnClickListener buttonConnectOnClickListener = 
			new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					MyClientTask myClientTask = new MyClientTask(
							editTextAddress.getText().toString(),
							Integer.parseInt(editTextPort.getText().toString()),
							Integer.parseInt(cantidadBytes.getText().toString()));
					myClientTask.execute();
				}};

	public class MyClientTask extends AsyncTask<Void, Void, Void> {
		
		String dstAddress;
		int dstPort;
		int puertoDispositivo;
		int cantidadBytes;
		String response = "";
		private JSON json;
		
		MyClientTask(String addr, int port, int canB){
			dstAddress = addr;
			dstPort = port;
			cantidadBytes=canB;
		}

		@Override
		protected Void doInBackground(Void... arg0) {


			json=new JSON();

			Socket socket = null;
			JSONObject jsonEnv = new JSONObject();
			try {
				socket = new Socket(dstAddress, dstPort);
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());

				jsonEnv = json.crearJson(cantidadBytes);
				dos.writeUTF(jsonEnv.toString());



				response = dis.readUTF();

				dis.close();
				dos.close();


			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally{
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			textResponse.setText(response);
			super.onPostExecute(result);
		}
		
	}

}
