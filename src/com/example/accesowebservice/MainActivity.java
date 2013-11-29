package com.example.accesowebservice;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private EditText dni;
	private Actividad actividad;
	private final int CONSULTA = 1;
	private final int INSERCION = 2;
	private final int MODIFICACION = 3;
	private final int BORRADO = 4;
	private ProgressDialog pDialog;
	// url para consultas
	private static final String url_query = "http://miw18.calamar.eui.upm.es/webservice/webService.query.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dni = (EditText) findViewById(R.id.editTextMainDNI);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void consulta(View v) {
		actividad = Actividad.CONSULTA;
		BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI", dni
				.getText().toString());
		new QueryDNI().execute(nameValuePairs);
	}

	private void consultar(JSONArray queryJSON) throws JSONException {
		int NUMREG = queryJSON.getJSONObject(0).getInt("NUMREG");

		if (NUMREG == 0) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errorRegistroNoExistente),
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(this, Consulta.class);
			i.putExtra("NUMREG", NUMREG);
			i.putExtra("registros", queryJSON.toString());
			startActivityForResult(i, CONSULTA);
		}
	}

	public void insercion(View v) {
		if (!dni.getText().toString().equals("")) {
			actividad = Actividad.INSERCION;
			BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI",
					dni.getText().toString());
			new QueryDNI().execute(nameValuePairs);
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errorDebeIntroducirUnDNI),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void insertar(JSONArray queryJSON) throws JSONException {
		int NUMREG = queryJSON.getJSONObject(0).getInt("NUMREG");

		if (NUMREG == 0) {
			Intent i = new Intent(this, Insercion.class);
			i.putExtra("dni", dni.getText().toString());
			startActivityForResult(i, INSERCION);
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errorRegistroExistente),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void modificacion(View v) {
		if (!dni.getText().toString().equals("")) {
			actividad = Actividad.MODIFICACION;
			BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI",
					dni.getText().toString());
			new QueryDNI().execute(nameValuePairs);
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errorDebeIntroducirUnDNI),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void modificar(JSONArray queryJSON) throws JSONException {
		int NUMREG = queryJSON.getJSONObject(0).getInt("NUMREG");

		if (NUMREG == 0) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errorRegistroNoExistente),
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(this, Modificacion.class);
			i.putExtra("registro", queryJSON.toString());
			startActivityForResult(i, MODIFICACION);
		}
	}

	public void borrado(View v) {
		if (!dni.getText().toString().equals("")) {
			actividad = Actividad.BORRADO;
			BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI",
					dni.getText().toString());
			new QueryDNI().execute(nameValuePairs);
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errorDebeIntroducirUnDNI),
					Toast.LENGTH_SHORT).show();
		}		
	}

	private void borrar(JSONArray queryJSON) throws JSONException {
		int NUMREG = queryJSON.getJSONObject(0).getInt("NUMREG");

		if (NUMREG == 0) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.errorRegistroNoExistente),
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(this, Borrado.class);
			i.putExtra("registro", queryJSON.toString());
			startActivityForResult(i, BORRADO);
		}
	}
	
	private void clear() {
		dni.setText("");
	}
		
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)	{
    	if (requestCode == CONSULTA) {
    		if (resultCode == RESULT_OK) {
    	        Bundle extras = data.getExtras();
    	        if(extras != null) {
    	        	int consulta = extras.getInt("CONSULTA");
    	        	if (consulta == 1) {
    	        		Toast.makeText(this, getString(R.string.consultaFinalizada), Toast.LENGTH_SHORT).show();
    	        		clear();
    	        	}
    	        }
    		}
		}
    	if (requestCode == INSERCION) {
    		if (resultCode == RESULT_OK) {
    	        Bundle extras = data.getExtras();
    	        if(extras != null) {
    	        	int NUMREG = extras.getInt("NUMREG");
    	        	if (NUMREG == 1) {
    	        		Toast.makeText(this, getString(R.string.insercionRealizada), Toast.LENGTH_SHORT).show();
    	        		clear();
    	        	}
    	        	else{
    	        		Toast.makeText(this, getString(R.string.insercionCancelada), Toast.LENGTH_SHORT).show();
    	        		clear();
    	        	}
    	        }
    		} else {
    			Toast.makeText(this, getString(R.string.insercionCancelada), Toast.LENGTH_SHORT).show();
    			clear();
    		}
		}
    	if (requestCode == MODIFICACION) {
    		if (resultCode == RESULT_OK) {
    	        Bundle extras = data.getExtras();
    	        if(extras != null) {
    	        	int NUMREG = extras.getInt("NUMREG");
    	        	if (NUMREG == 1) {
    	        		Toast.makeText(this, getString(R.string.actualizacionRealizada), Toast.LENGTH_SHORT).show();
    	        		clear();
    	        	}
    	        	else{
    	        		Toast.makeText(this, getString(R.string.actualizacionCancelada), Toast.LENGTH_SHORT).show();
    	        		clear();
    	        	}
    	        }
    		} else {
    			Toast.makeText(this, getString(R.string.actualizacionCancelada), Toast.LENGTH_SHORT).show();
    			clear();
    		}
		}
    	if (requestCode == BORRADO) {
    		if (resultCode == RESULT_OK) {
    	        Bundle extras = data.getExtras();
    	        if(extras != null) {
    	        	int NUMREG = extras.getInt("NUMREG");
    	        	if (NUMREG == 1) {
    	        		Toast.makeText(this, getString(R.string.borradoRealizado), Toast.LENGTH_SHORT).show();
    	        		clear();
    	        	}
    	        	else{
    	        		Toast.makeText(this, getString(R.string.borradoCancelado), Toast.LENGTH_SHORT).show();
    	        		clear();
    	        	}
    	        }
    		} else {
    			Toast.makeText(this, getString(R.string.borradoCancelado), Toast.LENGTH_SHORT).show();
    			clear();
    		}
		} 
	}

	/**
	 * Background Async Task to Query
	 * */
	class QueryDNI extends AsyncTask<BasicNameValuePair, Void, HttpResponse> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Obtaining info
		 * */
		protected HttpResponse doInBackground(BasicNameValuePair... params) {
			HttpResponse response = null;
			AndroidHttpClient httpclient = null;
			// controlar las conexiones Httpclient, da error si no se cierran
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					params.length);
			for (int i = 0; i < params.length; i++) {
				nameValuePairs.add(params[i]);
			}

			try {
				httpclient = AndroidHttpClient
						.newInstance("AndroidHttpClient");
				HttpPost httppost = new HttpPost(url_query);
				// enviar los parámetros como post
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
			} catch (Exception e) {
				Log.e(getString(R.string.app_name), R.string.errorHTTP + ": "
						+ e.toString());
			}
			
        	if (httpclient != null) {
				httpclient.close();
			}
					
			return response;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(HttpResponse response) {
			String message = "";

			// dismiss the dialog once done
			pDialog.dismiss();

			if (response != null) {
				int responseCode = response.getStatusLine().getStatusCode();
				String responseMessage = response.getStatusLine()
						.getReasonPhrase();

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String responseString;
					try {
						responseString = EntityUtils.toString(entity);
						message = responseString;
						JSONArray queryJSON = new JSONArray(message);
						if (actividad == Actividad.CONSULTA) {
							consultar(queryJSON);
						} else if (actividad == Actividad.INSERCION) {
							insertar(queryJSON);
						} else if (actividad == Actividad.MODIFICACION) {
							modificar(queryJSON);
						} else if (actividad == Actividad.BORRADO) {
							borrar(queryJSON);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					message = responseCode + ": " + responseMessage;
					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), R.string.errorConexion,
						Toast.LENGTH_LONG).show();
			}
		}

	}
}
