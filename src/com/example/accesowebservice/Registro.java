package com.example.accesowebservice;

public class Registro {
	private String dni;
	private String nombre;
	private String apellidos;
	private String direccion;
	private String telefono;
	private String equipo;

	public Registro(String dni, String nombre, String apellidos,
			String direccion, String telefono, String equipo) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.telefono = telefono;
		this.equipo = equipo;
	}

	public Registro() {
		this.dni = "";
		this.nombre = "";
		this.apellidos = "";
		this.direccion = "";
		this.telefono = "";
		this.equipo = "";
	}

	public String getDni() {
		return dni;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getEquipo() {
		return equipo;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

}
