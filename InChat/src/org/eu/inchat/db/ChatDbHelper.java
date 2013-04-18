package org.eu.inchat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDbHelper extends SQLiteOpenHelper {

	  public static final String TABLE_CONTACTS = "CONTACTOS";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_TELEFONO = "telefono";
	  public static final String COLUMN_NOMBRE = "nombre";
	  public static final String COLUMN_ESTADO = "estado";
	  public static final String COLUMN_ICONO = "icono";
	  
	  public static final String TABLE_MENSAJES = "MENSAJES";
	  public static final String COLUMN_TEXTO_MENSAJES = "texto";
	  public static final String COLUMN_DESTINO = "destino";
	  public static final String COLUMN_FECHA = "fecha";
	  public static final String COLUMN_MENSAJE_LOCAL = "local";
	  	    

	  private static final String DATABASE_NAME = "chat.db";
	  private static final int DATABASE_VERSION = 2;

	  
	  private static final String TABLE_CONTACTS_CREATE = "create table "
		      + TABLE_CONTACTS + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " 
		      + COLUMN_TELEFONO + " text not null, " 
		      + COLUMN_NOMBRE + " text not null, " 
		      + COLUMN_ESTADO + " text, "
		      + COLUMN_ICONO + " blob "
		      		+ ");";
	  
	  private static final String TABLE_MENSAJES_CREATE = "create table "
		      + TABLE_MENSAJES + "(" 
			  + COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_TEXTO_MENSAJES + " text not null, " 
		      + COLUMN_DESTINO + " text not null, " 
		      + COLUMN_FECHA + " text not null, "
		      + COLUMN_MENSAJE_LOCAL + " integer not null "
		      		+ ");";
	  

	  public ChatDbHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(getClass().getName(), "Creando base de datos " + DATABASE_NAME + " con version " + DATABASE_VERSION);
		db.execSQL(TABLE_CONTACTS_CREATE);
		db.execSQL(TABLE_MENSAJES_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENSAJES);
	    onCreate(db);
	}
	  
	  

}
