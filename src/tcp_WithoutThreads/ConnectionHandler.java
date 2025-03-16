package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private String nickname;

	public ConnectionHandler(Socket client, PrintWriter out, BufferedReader in) {
		this.client = client;
		this.out = out;
		this.in = in;
	}

	public void handleConnection() {
		try {
			out.println("Bienvenido al chat. Por favor, introduce tu nickname:");

			// Pedir nickname hasta que el usuario ingrese uno válido
			while (true) {
				nickname = in.readLine();
				if (nickname == null || nickname.trim().isEmpty()) {
					out.println("El nickname no puede estar vacío. Inténtalo de nuevo.");
				} else {
					break;
				}
			}

			System.out.println(nickname + " ha ingresado al chat.");

			String message;
			while ((message = in.readLine()) != null) {
				if (message.equalsIgnoreCase("!EXIT")) {
					System.out.println(nickname + " ha salido del chat.");
					out.println("Desconectado del chat.");
					break; // Termina la conexión
				}
				System.out.println(nickname + ": " + message);
				out.println("Servidor: Recibido -> " + message);
			}
		} catch (IOException e) {
			System.out.println("Error con la conexión: " + e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				if (client != null && !client.isClosed()) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
