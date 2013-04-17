package org.eu.inchat.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContactsDAO {

	private SQLiteDatabase database;
	private ChatDbHelper dbHelper;

	public ContactsDAO(Context context) {
		dbHelper = new ChatDbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void openReadable() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}	

	public void close() {
		dbHelper.close();
	}

	public List<Mensaje> findMensajesByPhone(String phoneNumber) {

		List<Mensaje> mensajes = new ArrayList<Mensaje>();

		Log.d(getClass().getName(), "Busca mensajes del telefono: "
				+ phoneNumber);

		Cursor cursor = database.query(ChatDbHelper.TABLE_MENSAJES,
				new String[] { ChatDbHelper.COLUMN_TEXTO_MENSAJES, // 0
						ChatDbHelper.COLUMN_FECHA, // 1
						ChatDbHelper.COLUMN_MENSAJE_LOCAL, // 2
						ChatDbHelper.COLUMN_DESTINO, // 3
				}, ChatDbHelper.COLUMN_DESTINO + "=?",
				new String[] { phoneNumber }, null, null, null);

		int numeroResultados = cursor.getCount();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Mensaje mensaje = cursorToMensaje(cursor);
			mensajes.add(mensaje);
			cursor.moveToNext();
		}

		cursor.close();

		return mensajes;

	}
	
	public List<Contacto> findContactsByPhone(String phoneNumber) {

		List<Contacto> contactos = new ArrayList<Contacto>();

		Log.d(getClass().getName(), "Busca contactos del telefono: "
				+ phoneNumber);

		Cursor cursor = database.query(ChatDbHelper.TABLE_CONTACTS,
				new String[] { ChatDbHelper.COLUMN_NOMBRE, // 0
						ChatDbHelper.COLUMN_TELEFONO, // 1
						ChatDbHelper.COLUMN_ESTADO // 2
				}, 
				ChatDbHelper.COLUMN_TELEFONO + "=?",
				new String[] { phoneNumber }, null, null, null);

		int numeroResultados = cursor.getCount();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contacto contacto = cursorToContacto(cursor);
			contactos.add(contacto);
			cursor.moveToNext();
		}

		cursor.close();

		return contactos;

	}	
	
	public List<Contacto> findAllContacts() {

		List<Contacto> contactos = new ArrayList<Contacto>();

		Log.d(getClass().getName(), "Busca todos los contactos");

		Cursor cursor = database.query(ChatDbHelper.TABLE_CONTACTS,
				new String[] { ChatDbHelper.COLUMN_NOMBRE, // 0
						ChatDbHelper.COLUMN_TELEFONO, // 1
						ChatDbHelper.COLUMN_ESTADO // 2
				}, 
				null,
				null, null, null, null);

		int numeroResultados = cursor.getCount();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contacto contacto = cursorToContacto(cursor);
			contactos.add(contacto);
			cursor.moveToNext();
		}

		cursor.close();

		return contactos;

	}	
		
	
	
	public void creaMensaje(Mensaje mensaje, boolean local) {

		//Crea mensaje y contacto (si no existe)
		ContentValues values = new ContentValues();
		values.put(ChatDbHelper.COLUMN_TEXTO_MENSAJES, mensaje.getTextoMensaje());
		values.put(ChatDbHelper.COLUMN_DESTINO, mensaje.getContactoDestino().getNumeroTelefono());
		values.put(ChatDbHelper.COLUMN_FECHA, convierteFecha(new Date()));
		values.put(ChatDbHelper.COLUMN_MENSAJE_LOCAL, local?1:0);
		
		long insertId = database.insert(ChatDbHelper.TABLE_MENSAJES, null, values);
		
		
		List<Contacto> contactos = findContactsByPhone(mensaje.getContactoDestino().getNumeroTelefono());
		if(contactos != null && !contactos.isEmpty()) {
			//Ya existe
		} else {
			creaContacto(mensaje.getContactoDestino());
		}
		
	}
	
	
	public void creaContacto(Contacto contacto) {
		ContentValues values = new ContentValues();
		values.put(ChatDbHelper.COLUMN_NOMBRE, contacto.getNombre());
		values.put(ChatDbHelper.COLUMN_TELEFONO, contacto.getNumeroTelefono());
		
		long contactId = database.insert(ChatDbHelper.TABLE_CONTACTS,null, values);
		
	}
	
	private String convierteFecha(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(fecha);
	}

	private Mensaje cursorToMensaje(Cursor cursor) {
		Mensaje mensaje = new Mensaje();

		mensaje.setTextoMensaje(cursor.getString(0));
		mensaje.setLocal(cursor.getInt(2) != 0);
		mensaje.setUserId(cursor.getString(3));
		mensaje.setTimestampMensaje(new Date()); // TODO

		return mensaje;
	}
	
	private Contacto cursorToContacto(Cursor cursor) {
		Contacto contacto = new Contacto();
		
		contacto.setNombre(cursor.getString(0));
		contacto.setNumeroTelefono(cursor.getString(1));
		contacto.setMensajeEstado(cursor.getString(2));


		return contacto;
	}

}
