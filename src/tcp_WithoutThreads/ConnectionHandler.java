package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.IOException;

public class ConnectionHandler {

	private BufferedReader in; // Lector de mensajes del cliente

	public ConnectionHandler(BufferedReader in) {
		this.in = in;
	}

	public String readMessage() {
		try {
			return in.readLine();
		} catch (IOException e) {
			System.out.println("Error al leer mensaje del cliente: " + e.getMessage());
			return null;
		}
	}
}
