package com.example.accesowebservice;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class Insercion extends Activity {
	private static final String url_insert = "http://miw18.calamar.eui.upm.es/webservice/webService.insert.php";
	private Bundle extras;
	private EditText dni;
	private EditText nombre;
	private EditText apellidos;
	private EditText direccion;
	private EditText telefono;
	private EditText equipo;
	private ProgressDialog pDialog;
	private String dniRecibido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insercion);
		
		extras = getIntent().getExtras();
		
		dniRecibido = extras.getString("dni");
		
		dni = (EditText) findViewById(R.id.editTextInsercionDNI);
		nombre = (EditText) findViewById(R.id.editTextInsercionNombre);
		apellidos = (EditText) findViewById(R.id.editTextInsercionApellidos);
		direccion = (EditText) findViewById(R.id.editTextInsercionDireccion);
		telefono = (EditText) findViewById(R.id.editTextInsercionTelefono);
		equipo = (EditText) findViewById(R.id.editTextInsercionEquipo);
		
		dni.setText(dniRecibido);

	}
	
	public void insert(View v) {
		
		// no permitir altas con algún campo vacío 
		
		if ((Auxiliar.estaVacio(nombre.getText().toString())) ||
			(Auxiliar.estaVacio(apellidos.getText().toString())) ||
			(Auxiliar.estaVacio(direccion.getText().toString())) ||
			(Auxiliar.estaVacio(telefono.getText().toString())) ||
			(Auxiliar.estaVacio(equipo.getText().toString())) ) {
			
			Toast.makeText(getApplicationContext(), R.string.errorCamposObligatorios, Toast.LENGTH_LONG).show();	
		}
		else {
			
			JSONObject dato = new JSONObject();
	
			try {
				dato.put("DNI", dni.getText().toString());
				dato.put("Nombre", nombre.getText().toString());
				dato.put("Apellidos", apellidos.getText().toString());
				dato.put("Direccion", direccion.getText().toString());
				dato.put("Telefono", telefono.getText().toString());
				dato.put("Equipo", equipo.getText().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			new InsertDNI().execute(dato.toString());
		}
	}

	/**
	 * Background Async Task to Insert
	 * */
	class InsertDNI extends AsyncTask<String, Void, HttpResponse> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Insercion.this);
			pDialog.setMessage(getString(R.string.progress_insert));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Obtaining info
		 * */
		protected HttpResponse doInBackground(String... registro) {
			HttpResponse response = null;
			AndroidHttpClient httpclient = null;
			// controlar las conexiones Httpclient, da error si no se cierran			 

			try {
				httpclient = AndroidHttpClient
						.newInstance("AndroidHttpClient");
				HttpPost httppost = new HttpPost(url_insert);
				// enviar los parámetros como objeto JSON
				httppost.setHeader("content-type", "application/json");
				StringEntity entity = new StringEntity(registro[0]);
				httppost.setEntity(entity);
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
						JSONArray insertJSON = new JSONArray(message);
						// retornar extra a la actividad principal
						retornar(insertJSON);
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
	
	private void retornar(JSONArray insertJSON) throws JSONException {

		Intent i = new Intent();
		i.putExtra("NUMREG", insertJSON.getJSONObject(0).getInt("NUMREG"));
		setResult(RESULT_OK, i);
		finish();
	}
	
    @Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.insercion, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
