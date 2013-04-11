package org.eu.inchat.model;

import java.util.Date;

public class Mensaje {
	
	private String textoMensaje;
	private Date timestampMensaje;
	private boolean local;
	private String userId;
	
	public Mensaje(String textoMensaje, Date timestampMensaje, boolean local,
			String userId) {
		super();
		this.textoMensaje = textoMensaje;
		this.timestampMensaje = timestampMensaje;
		this.local = local;
		this.userId = userId;
	}
	public String getTextoMensaje() {
		return textoMensaje;
	}
	public void setTextoMensaje(String textoMensaje) {
		this.textoMensaje = textoMensaje;
	}
	public Date getTimestampMensaje() {
		return timestampMensaje;
	}
	public void setTimestampMensaje(Date timestampMensaje) {
		this.timestampMensaje = timestampMensaje;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}
