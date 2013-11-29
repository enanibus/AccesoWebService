package com.example.accesowebservice;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class Consulta extends Activity {
	private Bundle extras;
	private ArrayList<Registro> ArrayListRegistro;
	private int indice = 0;
	private EditText dni;
	private EditText nombre;
	private EditText apellidos;
	private EditText direccion;
	private EditText telefono;
	private EditText equipo;
	private TextView resultado;
	private ImageButton primero;
	private ImageButton anterior;
	private ImageButton siguiente;
	private ImageButton ultimo;
	
	private int NUMREG;
	private String registros;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta);

		extras = getIntent().getExtras();
		NUMREG = extras.getInt("NUMREG");
		registros = extras.getString("registros");

		dni = (EditText) findViewById(R.id.editTextConsultaDNI);
		nombre = (EditText) findViewById(R.id.editTextConsultaNombre);
		apellidos = (EditText) findViewById(R.id.editTextConsultaApellidos);
		direccion = (EditText) findViewById(R.id.editTextConsultaDireccion);
		telefono = (EditText) findViewById(R.id.editTextConsultaTelefono);
		equipo = (EditText) findViewById(R.id.editTextConsultaEquipo);
		resultado = (TextView) findViewById(R.id.textViewConsultaResultado);
		primero = (ImageButton) findViewById(R.id.imageButtonConsultaPrimero);
		anterior = (ImageButton) findViewById(R.id.imageButtonConsultaAnterior);
		siguiente = (ImageButton) findViewById(R.id.imageButtonConsultaSiguiente);
		ultimo = (ImageButton) findViewById(R.id.imageButtonConsultaUltimo);
		
		if (NUMREG == 1) {
			primero.setClickable(false);
			anterior.setClickable(false);
			siguiente.setClickable(false);
			ultimo.setClickable(false);
		}

		try {
			parseJSONRegistros(registros);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void parseJSONRegistros(String queryJSON) throws JSONException {

		JSONArray jsonArray = new JSONArray(queryJSON);
		ArrayListRegistro = new ArrayList<Registro>();

		for (int i = 1; i <= NUMREG; i++) {
			Registro r = new Registro();
			r.setDni(jsonArray.getJSONObject(i).getString("DNI"));
			r.setNombre(jsonArray.getJSONObject(i).getString("Nombre"));
			r.setApellidos(jsonArray.getJSONObject(i).getString("Apellidos"));
			r.setDireccion(jsonArray.getJSONObject(i).getString("Direccion"));
			r.setTelefono(jsonArray.getJSONObject(i).getString("Telefono"));
			r.setEquipo(jsonArray.getJSONObject(i).getString("Equipo"));
			ArrayListRegistro.add(r);
		}

		mostrarRegistro(indice);
	}

	public void consultaPrimero(View v) {
		indice = 0;
		mostrarRegistro(indice);

	}

	public void consultaUltimo(View v) {
		indice = ArrayListRegistro.size() - 1;
		mostrarRegistro(indice);
	}

	public void consultaAnterior(View v) {
		if (indice > 0) {
			indice--;
			mostrarRegistro(indice);
		}
	}

	public void consultaSiguiente(View v) {
		if (indice < ArrayListRegistro.size() - 1) {
			indice++;
			mostrarRegistro(indice);
		}
	}

	private void mostrarRegistro(int indice) {

		dni.setText(ArrayListRegistro.get(indice).getDni());
		nombre.setText(ArrayListRegistro.get(indice).getNombre());
		apellidos.setText(ArrayListRegistro.get(indice).getApellidos());
		direccion.setText(ArrayListRegistro.get(indice).getDireccion());
		telefono.setText(ArrayListRegistro.get(indice).getTelefono());
		equipo.setText(ArrayListRegistro.get(indice).getEquipo());
		resultado.setText(getString(R.string.registro) + " " + (indice + 1)
				+ " " + getString(R.string.de) + " "
				+ Integer.toString(ArrayListRegistro.size()));
	}
	
	
    @Override
	public void onBackPressed() {
    	int consulta = 1;
    	
		Intent i = new Intent();
		i.putExtra("CONSULTA", consulta);
   		setResult(RESULT_OK, i);
		finish();
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
		getMenuInflater().inflate(R.menu.consulta, menu);
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
