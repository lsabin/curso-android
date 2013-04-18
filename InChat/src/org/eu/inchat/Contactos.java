package org.eu.inchat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.util.Log;

public class Contactos {
	
	public static List<Contacto> obtieneContactosReales(Context context) {

		List<Contacto> contactItemList = new ArrayList<Contacto>();
		
		
		String[] projection = new String[] {Data._ID, //0
				Data.DISPLAY_NAME, //1
				Phone.NUMBER, 		//2
				Data.CONTACT_ID, 	//3
				Phone.TYPE, 		//4
				Phone.LABEL,		//5
				Data.PHOTO_THUMBNAIL_URI}; //6
		
		Cursor c = context.getContentResolver().query(
				Data.CONTENT_URI,
				projection,
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", null,
				Data.DISPLAY_NAME);
		int count = c.getCount();
		boolean b = c.moveToFirst();
		String[] columnNames = c.getColumnNames();
		int displayNameColIndex = c.getColumnIndex("display_name");
		int idColIndex = c.getColumnIndex("_id");
		
		int contactIdColIndex =c.getColumnIndex("contact_id");
		int col2Index = c.getColumnIndex(columnNames[2]);
		int col3Index = c.getColumnIndex(columnNames[3]);
		int col4Index = c.getColumnIndex(columnNames[4]);
		int colPhotoIndex = c.getColumnIndex(columnNames[6]);
		
		for (int i = 0; i < count; i++) {
			String displayName = c.getString(displayNameColIndex);
			String phoneNumber = c.getString(col2Index);
			int contactId = c.getInt(col3Index);
			String phoneType = c.getString(col4Index);
			long _id = c.getLong(idColIndex);
			
			//String uriPhoto = c.getString(colPhotoIndex);
			//Log.i("LeeContactos", "Photo uri: " + uriPhoto);
			
			
			// contactItem.mContactId = contactId;
			
			byte[] fotoBytes = getPhoto(context, contactId);
			
			
			Contacto contactItem = new Contacto();
			
			
			contactItem.setIcono(fotoBytes);
			
			//Limpia los numeros de telefono de caracteres
			phoneNumber = phoneNumber.replaceAll("-", "");
			phoneNumber = phoneNumber.replaceAll(" ", "");
			
			contactItem.setNumeroTelefono(phoneNumber);
			
			contactItem.setNombre(displayName);
			contactItemList.add(contactItem);
			boolean b2 = c.moveToNext();
		}
		c.close();
		
		return contactItemList;

	}
	
	
	public static byte[] getPhoto(Context context, int contactId) {
	    Uri contactPhotoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	    

	    // contactPhotoUri --> content://com.android.contacts/contacts/1557
	    

	    InputStream photoDataStream = Contacts.openContactPhotoInputStream(context.getContentResolver(),contactPhotoUri); // <-- always null
	    Bitmap photo = BitmapFactory.decodeStream(photoDataStream);
	    
	    Log.i("Contactos", "Uri photo: " + contactPhotoUri.toString() + ", bitmap: " + photo);
	    
	    if (photo != null) {
	    	
	    	ByteArrayOutputStream bout = new ByteArrayOutputStream(photo.getByteCount());
	    	photo.compress(Bitmap.CompressFormat.JPEG, 100, bout);
	    	
	    	return bout.toByteArray();
	    	
	    } else {
	    	return new byte[0];
	    }
	    
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
