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

public class Borrado extends Activity {
	private static final String url_delete = "http://miw18.calamar.eui.upm.es/webservice/webService.delete.php";
	private Bundle extras;
	private EditText dni;
	private EditText nombre;
	private EditText apellidos;
	private EditText direccion;
	private EditText telefono;
	private EditText equipo;
	private ProgressDialog pDialog;
	private String registro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrado);

		extras = getIntent().getExtras();
		registro = extras.getString("registro");		
		
		dni = (EditText) findViewById(R.id.editTextBorradoDNI);
		nombre = (EditText) findViewById(R.id.editTextBorradoNombre);
		apellidos = (EditText) findViewById(R.id.editTextBorradoApellidos);
		direccion = (EditText) findViewById(R.id.editTextBorradoDireccion);
		telefono = (EditText) findViewById(R.id.editTextBorradoTelefono);
		equipo = (EditText) findViewById(R.id.editTextBorradoEquipo);
		
		JSONArray jsonArray;

		try {
			jsonArray = new JSONArray(registro);
			dni.setText(jsonArray.getJSONObject(1).getString("DNI"));
			nombre.setText(jsonArray.getJSONObject(1).getString("Nombre"));
			apellidos.setText(jsonArray.getJSONObject(1).getString("Apellidos"));
			direccion.setText(jsonArray.getJSONObject(1).getString("Direccion"));
			telefono.setText(jsonArray.getJSONObject(1).getString("Telefono"));
			equipo.setText(jsonArray.getJSONObject(1).getString("Equipo"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	public void delete(View v) {

		BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI", dni
				.getText().toString());

		new DeleteDNI().execute(nameValuePairs);
	}

	/**
	 * Background Async Task to Insert
	 * */
	class DeleteDNI extends AsyncTask<BasicNameValuePair, Void, HttpResponse> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Borrado.this);
			pDialog.setMessage(getString(R.string.progress_delete));
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
				HttpPost httppost = new HttpPost(url_delete);
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
						JSONArray deleteJSON = new JSONArray(message);
						// retornar extra a la actividad principal
						retornar(deleteJSON);
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
	
	private void retornar(JSONArray deleteJSON) throws JSONException {

		Intent i = new Intent();
		i.putExtra("NUMREG", deleteJSON.getJSONObject(0).getInt("NUMREG"));
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
		getMenuInflater().inflate(R.menu.borrado, menu);
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
