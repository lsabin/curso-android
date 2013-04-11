package org.eu.inchat.model;

import java.util.Date;
import java.util.List;

import android.net.Uri;

public class Contacto {
	
	private String nombre;
	private String numeroTelefono;
	private Date ultimaConexion;
	private Uri iconoContacto;
	private String mensajeEstado;
	private List<Mensaje> mensajes;
	
	public Contacto(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	public Contacto(String nombre, String numeroTelefono, Uri iconoContacto) {
		super();
		this.nombre = nombre;
		this.numeroTelefono = numeroTelefono;
		this.iconoContacto = iconoContacto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public Date getUltimaConexion() {
		return ultimaConexion;
	}

	public void setUltimaConexion(Date ultimaConexion) {
		this.ultimaConexion = ultimaConexion;
	}

	public Uri getIconoContacto() {
		return iconoContacto;
	}

	public void setIconoContacto(Uri iconoContacto) {
		this.iconoContacto = iconoContacto;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public String getMensajeEstado() {
		return mensajeEstado;
	}

	public void setMensajeEstado(String mensajeEstado) {
		this.mensajeEstado = mensajeEstado;
	}
	
}
