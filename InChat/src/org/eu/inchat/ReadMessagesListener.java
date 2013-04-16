package org.eu.inchat;

import java.util.List;

import org.eu.inchat.model.Mensaje;

public interface ReadMessagesListener {
	void onMessagesReceived(List<Mensaje> result);
}
