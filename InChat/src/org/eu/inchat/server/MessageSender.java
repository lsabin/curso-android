package org.eu.inchat.server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eu.inchat.model.Mensaje;

import android.util.Log;

public class MessageSender {

	private String host;
	private int puerto;
	
	private static final String MSG_FROM = "MSG_FROM: ";
	private static final String RCTP_TO = "RCTP_TO: ";
	private static final String TIME = "TIME: ";
	private static final String DATA = "DATA: ";
	private static final String GETMSGID = "GETMSGID: ";

	public MessageSender(String host, int puerto) {
		super();
		this.host = host;
		this.puerto = puerto;
	}



	public void sendMessage(String message) throws Exception {
		Socket socket = new Socket(host, puerto);

		InputStream inputStream = new ByteArrayInputStream(
				message.getBytes("UTF-8"));

		OutputStream socketOutputStream = socket.getOutputStream();

		byte[] buffer = new byte[1024];
		int read;
		while ((read = inputStream.read(buffer)) != -1) {
			socketOutputStream.write(buffer, 0, read);
		}
		socketOutputStream.close();
		inputStream.close();
		socket.close();

	}
	
	
	public List<Mensaje> readMessages(String usuario) {
		
		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		
		try {
			Socket socket = new Socket(host, puerto);
			DataOutputStream socketOutputStream = new DataOutputStream(
					socket.getOutputStream());
			socketOutputStream.writeUTF(GETMSGID + usuario);
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			
			do {
				String linea = dis.readUTF();
				
				Log.d(getClass().getName(), "Linea leida: " + linea);
				
				if (linea.startsWith(MSG_FROM)) {
					
				} else if (linea.startsWith(TIME)) {
					
				}
				
			} while (dis.available() > 0);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mensajes;
		
		
	}
	
	
	public String createMessage(String remitente, String receptor, String mensaje) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(creaRemitente(remitente))
			.append("\n")
			.append(creaReceptor(receptor))
			.append("\n")
			.append(creaTimestamp())
			.append("\n")
			.append(creaData(mensaje))
			.append("\n");
		
		return sb.toString();
		
	}
	
	private String creaRemitente(String remitente) {
		return MSG_FROM + remitente; 
	}
	
	private String creaReceptor(String receptor) {
		
		//Limpia la cadena del telefono
		receptor = receptor.replaceAll("-", "");
		receptor = receptor.replaceAll(" ", "");
		return RCTP_TO + receptor;
	}
	
	private String creaTimestamp() {
		return TIME + new Date().getTime();
		
	}
	
	private String creaData(String mensaje) {
		return DATA + mensaje; 
	}

}
