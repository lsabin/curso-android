package org.eu.inchat.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class InChatAdapter extends ArrayAdapter<Contacto> {
	
	private List<Contacto> items;
	private Context context;
	private LayoutInflater inflater;

	public InChatAdapter(Context context, int textViewResourceId,
			List<Contacto> items) {
		super(context, textViewResourceId, items);

		//Depura los contactos para que solo se muestren en el historial
		//los que tienen mensajes	
		this.items = depuraContactos(items);
		
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
		
		String textoUltimoMensaje = null;
		if (!mensajes.isEmpty()) {
			Mensaje ultimoMensaje = mensajes.get(mensajes.size()-1);
			textoUltimoMensaje = ultimoMensaje.getTextoMensaje();
		} else {
			textoUltimoMensaje = "";
		}
			
		holder.contactName.setText(contacto.getNombre());
		holder.lastLine.setText(textoUltimoMensaje);
		
		Date ultimaConexion = contacto.getUltimaConexion();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		holder.lastDate.setText(sdf.format(ultimaConexion));
		
		if (contacto.getIcono() != null && contacto.getIcono().length > 0) {
			Bitmap bm = BitmapFactory.decodeByteArray(contacto.getIcono(), 0, contacto.getIcono().length);
			holder.contactIcon.setImageBitmap(bm);
		} else {
			holder.contactIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));	
		}
		
		
		
		return convertView;
		
	}
	
	
	private List<Contacto> depuraContactos(List<Contacto> contactos) {
		
		List<Contacto> contactosConMensaje = new ArrayList<Contacto>();
		
		for(Contacto contacto : contactos) {
			if(!contacto.getMensajes().isEmpty()) {
				contactosConMensaje.add(contacto);
			}
			
		}
		
		return contactosConMensaje;
		
	}	
	
	
	
	

}
