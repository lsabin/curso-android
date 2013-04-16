package org.eu.inchat.server;

public class PruebaSever {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String host = "host.sun.corenetworks.es";
		int puerto = 1234;

		MessageSender sender = new MessageSender(host, puerto);
		
		String mensajeProtocolo = sender.createMessage("9001", "9010", "el mensaje");
		
		try {
			sender.sendMessage(mensajeProtocolo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
