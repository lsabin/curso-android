package org.eu.inchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eu.inchat.adapters.InChatAdapter;
import org.eu.inchat.fragments.FragmentPreferencias;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class InChatActivity extends ListActivity implements
		ReadMessagesListener {

	private List<Contacto> historico;

	private OnClickListener listenerNuevoChat = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(getClass().getName(), "Inicia activity contactos....");

			nuevoChat();

		}
	};

	private void nuevoChat() {

		Intent i = new Intent(this, ContactosActivity.class);
		startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_chat);

		initButtons();

		// Contactos.creaContactos(getApplicationContext());

		historico = initHistory();

		initListView(historico);

	}

	private void initButtons() {
		ImageButton botonAddChat = (ImageButton) findViewById(R.id.imageAddChat);
		botonAddChat.setOnClickListener(listenerNuevoChat);
	}

	private List<Contacto> initHistory() {
		List<Contacto> contactos = new ArrayList<Contacto>();
		List<Mensaje> mensajes = new ArrayList<Mensaje>();

		/*
		 * for (int i = 1; i < 11; i++) { mensajes.add(new Mensaje("Mensaje " +
		 * i,new Date(),false,"x"));
		 * 
		 * }
		 */

		// contactos = creaContactosPrueba();
		contactos = obtieneContactosReales();

		for (Contacto contacto : contactos) {
			contacto.setUltimaConexion(new Date());
		}

		creaMensajesPrueba(contactos);
		obtieneMensajesRecibidos(contactos);

		contactos = depuraContactos(contactos);

		return contactos;
	}

	private void obtieneMensajesRecibidos(List<Contacto> contactos) {

	}

	private void initListView(List<Contacto> historico) {

		/*
		 * setListAdapter(new ArrayAdapter<String>(getApplicationContext(),
		 * android.R.layout.activity_list_item, historico));
		 */

		setListAdapter(new InChatAdapter(getApplicationContext(),
				R.layout.activity_inchat_listitem, historico));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.in_chat, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Log.d(getClass().getName(),
				"Chat seleccionado: " + historico.get(position));

		abreChatContacto(position);

	}

	private void abreChatContacto(int position) {

		Contacto chat = historico.get(position);

		Log.d(getClass().getName(), chat.getNumeroTelefono());

		// Pasar el id del chat a la activity para que recupere los datos de la
		// conversación

		Intent i = new Intent(this, ChatActivity.class);
		i.putExtra("contacto", chat);
		startActivity(i);
	}

	private List<Mensaje> creaMensajesPrueba(List<Contacto> contactos) {

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

		for (int i = 0; i < conversacion1.length; i++) {
			conversacion.add(conversacion1[i]);
		}

		for (Contacto contacto : contactos) {

			String numeroTelefono = contacto.getNumeroTelefono();
			numeroTelefono = numeroTelefono.replaceAll("-", "");
			numeroTelefono = numeroTelefono.replaceAll(" ", "");

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

		return null;
	}

	private List<Contacto> creaContactosPrueba() {
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

	private List<Contacto> obtieneContactosReales() {

		return Contactos.obtieneContactosReales(getApplicationContext());

	}

	private List<Contacto> depuraContactos(List<Contacto> contactos) {

		List<Contacto> contactosConMensaje = new ArrayList<Contacto>();

		for (Contacto contacto : contactos) {
			if (!contacto.getMensajes().isEmpty()) {
				contactosConMensaje.add(contacto);
			}

		}

		return contactosConMensaje;

	}

	@Override
	public void onMessagesReceived(List<Mensaje> result) {

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				//Lanzar activity de preferencias
				Intent intent = new Intent(this,Preferencias.class);
				startActivity(intent);

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

}
