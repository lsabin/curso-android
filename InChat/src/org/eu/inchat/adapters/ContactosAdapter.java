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

public class ContactosAdapter extends ArrayAdapter<Contacto> {
	
	private List<Contacto> items;
	private Context context;
	private LayoutInflater inflater;

	public ContactosAdapter(Context context, int textViewResourceId,
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
		public TextView mensajeEstado;
		public ImageView contactIcon;
		
		
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_item, null);
			
			holder = new ViewHolder();
			
			holder.contactName = (TextView) convertView.findViewById(R.id.contact_textContactName);
			holder.mensajeEstado = (TextView) convertView.findViewById(R.id.contact_textMensajeEstado);
			holder.contactIcon = (ImageView) convertView.findViewById(R.id.imageIconContact);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Contacto contacto = items.get(position);
		
		List<Mensaje> mensajes = contacto.getMensajes();
		
		holder.contactName.setText(contacto.getNombre());
		holder.mensajeEstado.setText(contacto.getMensajeEstado());
		holder.contactIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
		
		
		return convertView;
		
	}
	
	
	
	

}
