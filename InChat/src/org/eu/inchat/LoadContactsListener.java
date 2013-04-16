package org.eu.inchat;

import java.util.List;

import org.eu.inchat.model.Contacto;

public interface LoadContactsListener {
	void comienzaCargaContactos();
	void onContactsLoaded(List<Contacto> contacts);
}
