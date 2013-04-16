package org.eu.inchat.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eu.inchat.R;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatAdapter extends ArrayAdapter<Mensaje> {
	
	private List<Mensaje> items;
	private Context context;
	private LayoutInflater inflater;

	public ChatAdapter(Context context, int textViewResourceId,
			List<Mensaje> items) {
		super(context, textViewResourceId, items);

		
		this.items = items;
		this.context = context;
		
		
		
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Mensaje getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/* Clase auxiliar para contener los elementos de la view */
	public static class ViewHolder {
		public TextView mensajeLocal;
		public TextView fechaLocal;
		
		public TextView mensajeRemoto;
		public TextView fechaRemoto;
		
		public LinearLayout layoutLocal;
		public LinearLayout layoutRemoto;
		
		
		
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_item, null);
			
			holder = new ViewHolder();
			
			holder.mensajeLocal = (TextView) convertView.findViewById(R.id.chat_textMensajeLocal);
			holder.fechaLocal = (TextView) convertView.findViewById(R.id.chat_fechaLocal);
			
			holder.mensajeRemoto = (TextView) convertView.findViewById(R.id.chat_textMensajeRemoto);
			holder.fechaRemoto = (TextView) convertView.findViewById(R.id.chat_fechaRemoto);
			
			holder.layoutLocal = (LinearLayout) convertView.findViewById(R.id.izquierda);
			holder.layoutRemoto = (LinearLayout) convertView.findViewById(R.id.derecha);
			
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Mensaje mensaje = items.get(position);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		if (mensaje.isLocal()) {
			holder.mensajeLocal.setText(mensaje.getTextoMensaje());
			Date fechaMensaje = mensaje.getTimestampMensaje();
			holder.fechaLocal.setText(sdf.format(fechaMensaje));
			holder.layoutLocal.setVisibility(View.VISIBLE);
			holder.layoutRemoto.setVisibility(View.GONE);
		} else {
			holder.mensajeRemoto.setText(mensaje.getTextoMensaje());
			Date fechaMensaje = mensaje.getTimestampMensaje();
			holder.fechaRemoto.setText(sdf.format(fechaMensaje));
			holder.layoutLocal.setVisibility(View.GONE);
			holder.layoutRemoto.setVisibility(View.VISIBLE);
		}
		
		
		return convertView;
		
	}
	

}
