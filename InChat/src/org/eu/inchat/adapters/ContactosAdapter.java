package org.eu.inchat.adapters;

import java.util.List;

import org.eu.inchat.R;
import org.eu.inchat.model.Contacto;
import org.eu.inchat.model.Mensaje;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
			holder.contactIcon = (ImageView) convertView.findViewById(R.id.contact_icono);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Contacto contacto = items.get(position);
		
		List<Mensaje> mensajes = contacto.getMensajes();
		
		holder.contactName.setText(contacto.getNombre());
		holder.mensajeEstado.setText(contacto.getMensajeEstado());
		
		if(contacto.getIcono() != null && contacto.getIcono().length > 0) {
			Bitmap bm = BitmapFactory.decodeByteArray(contacto.getIcono(), 0, contacto.getIcono().length);
			holder.contactIcon.setImageBitmap(bm);
		} else {
			holder.contactIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));	
		}
		
		
		
		return convertView;
		
	}
	
	
	
	

}
