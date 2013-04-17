package org.eu.inchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

public class Contactos {
	
	public static List<Contacto> obtieneContactosReales(Context context) {

		List<Contacto> contactItemList = new ArrayList<Contacto>();
		
		Cursor c = context.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER,
						Data.CONTACT_ID, Phone.TYPE, Phone.LABEL },
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", null,
				Data.DISPLAY_NAME);
		int count = c.getCount();
		boolean b = c.moveToFirst();
		String[] columnNames = c.getColumnNames();
		int displayNameColIndex = c.getColumnIndex("display_name");
		int idColIndex = c.getColumnIndex("_id");
		
		// int contactIdColIndex =
		c.getColumnIndex("contact_id");
		int col2Index = c.getColumnIndex(columnNames[2]);
		int col3Index = c.getColumnIndex(columnNames[3]);
		int col4Index = c.getColumnIndex(columnNames[4]);
		
		for (int i = 0; i < count; i++) {
			String displayName = c.getString(displayNameColIndex);
			String phoneNumber = c.getString(col2Index);
			int contactId = c.getInt(col3Index);
			String phoneType = c.getString(col4Index);
			long _id = c.getLong(idColIndex);
			Contacto contactItem = new Contacto();
			
			//Limpia los numeros de telefono de caracteres
			phoneNumber = phoneNumber.replaceAll("-", "");
			phoneNumber = phoneNumber.replaceAll(" ", "");
			contactItem.setNumeroTelefono(phoneNumber);
			// contactItem.mContactId = contactId;
			contactItem.setNombre(displayName);
			contactItemList.add(contactItem);
			boolean b2 = c.moveToNext();
		}
		c.close();
		
		return contactItemList;

	}	
	
	public static void creaContactos(Context context) {
		
		List<Contacto> contactos = creaContactosPrueba();
		
		int i = 0;
		for(Contacto contacto : contactos) {
			
			i++;
			ContentValues values = new ContentValues();
			
	        values.put(Data.RAW_CONTACT_ID, i);
	        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
	        values.put(Phone.NUMBER, contacto.getNumeroTelefono());
	        values.put(Phone.TYPE, Phone.TYPE_CUSTOM);
	        values.put(Phone.DISPLAY_NAME, contacto.getNombre());
	        Uri dataUri = context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);		
		}
	}
	
	private static List<Contacto> creaContactosPrueba() {
		List<Contacto> contactos = new ArrayList<Contacto>(10);

		Contacto[] arrayContactos = new Contacto[] {
				new Contacto("Toto", "1", null),
				new Contacto("Jaimito", "3", null),
				new Contacto("Jorgito", "4", null),
				new Contacto("Juanito", "5", null),
				new Contacto("Tintin", "6", null),
				new Contacto("Milu", "7", null),
				new Contacto("Haddock", "8", null),
				new Contacto("Tornasol", "9", null),
				new Contacto("Castafiore", "10", null) };

		return Arrays.asList(arrayContactos);
	}
	
	private static List<Mensaje> creaMensajesPrueba(List<Contacto> contactos) {
		
		List<Mensaje> conversacion = new ArrayList<Mensaje>();		

		// Crear mensajes para un par de contactos de prueba
		Mensaje[] conversacion1 = new Mensaje[] {
				new Mensaje("Hola", new Date(new Date().getTime() + 1), true,
						"1"),
				new Mensaje("Hola", new Date(new Date().getTime() + 1), false,
						"1"),
				new Mensaje("Que tal?", new Date(new Date().getTime() + 1),
						true, "1"),
				new Mensaje("Bien", new Date(new Date().getTime() + 1), false,
						"1"),
				new Mensaje("Y tú?", new Date(new Date().getTime() + 1), false,
						"1"),
				new Mensaje(
						"Pues muy bien porque bla bla bla bla bla  bla bla",
						new Date(new Date().getTime() + 1), true, "1"), };
		
		for(int i = 0; i < conversacion1.length; i++) {
			conversacion.add(conversacion1[i]);
		}
		

		for (Contacto contacto : contactos) {
			
			String numeroTelefono = contacto.getNumeroTelefono();
			numeroTelefono = numeroTelefono.replaceAll("-","");
			numeroTelefono = numeroTelefono.replaceAll(" ","");
			
			if ("986710903".equals(numeroTelefono)) {
				contacto.setMensajes(conversacion);
			} else if ("622098721".equals(numeroTelefono)) {
				contacto.setMensajes(conversacion);
			} else if ("981344287".equals(numeroTelefono)) {
				contacto.setMensajes(conversacion);
			} else {
				contacto.setMensajes(new ArrayList<Mensaje>());
			}

		}

		return conversacion;
	}	

}
