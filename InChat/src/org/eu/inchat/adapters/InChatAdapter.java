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
import android.widget.TextView;

public class InChatAdapter extends ArrayAdapter<Contacto> {
	
	private List<Contacto> items;
	private Context context;
	private LayoutInflater inflater;

	public InChatAdapter(Context context, int textViewResourceId,
			List<Contacto> items) {
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
	public Contacto getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/* Clase auxiliar para contener los elementos de la view */
	public static class ViewHolder {
		public TextView contactName;
		public TextView lastLine;
		public TextView lastDate;
		public ImageView contactIcon;
		
		
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_inchat_listitem, null);
			
			holder = new ViewHolder();
			
			holder.contactName = (TextView) convertView.findViewById(R.id.textContactName);
			holder.lastLine = (TextView) convertView.findViewById(R.id.textLastChatLine);
			holder.lastDate = (TextView) convertView.findViewById(R.id.textLastChatDate);
			holder.contactIcon = (ImageView) convertView.findViewById(R.id.imageIconContact);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		Contacto contacto = items.get(position);
		List<Mensaje> mensajes = contacto.getMensajes();
		
		Mensaje ultimoMensaje = mensajes.get(mensajes.size()-1);
		
		holder.contactName.setText(contacto.getNombre());
		holder.lastLine.setText(ultimoMensaje.getTextoMensaje());
		
		Date ultimaConexion = contacto.getUltimaConexion();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.format(ultimaConexion);
		holder.lastDate.setText(sdf.format(ultimaConexion));
		
		holder.contactIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
		
		return convertView;
		
	}
	
	
	
	

}