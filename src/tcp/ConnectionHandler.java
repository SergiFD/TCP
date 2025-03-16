package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private String nickname;
	private Server server;

	public ConnectionHandler(Socket client, Server server) {
		this.client = client;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			// Pedir nickname hasta que el usuario introduzca algo válido
			while (true) {
				out.println("Por favor inserte un nickname:");
				nickname = in.readLine();

				if (nickname == null || nickname.trim().isEmpty()) {
					out.println("El nickname no puede estar vacío. Inténtalo de nuevo.");
				} else {
					break; // Sale del bucle si el nickname es válido
				}
			}

			server.broadcast(nickname + " ha empezado el chat");

			String message;
			while ((message = in.readLine()) != null) {
				if (message.startsWith("/nick")) {
					String[] messageSplit = message.split(" ", 2);
					if (messageSplit.length == 2) {
						server.broadcast(nickname + " se ha renombrado a " + messageSplit[1]);
						nickname = messageSplit[1];
					} else {
						out.println("⚠️ ¡No se proporcionó un nickname!");
					}
				} else if (message.startsWith("!EXIT")) {
					server.broadcast(nickname + " ha salido del chat!");
					shutdown();
				} else {
					server.broadcast(nickname + ": " + message);
				}
			}
		} catch (Exception e) {
			shutdown();
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	public void shutdown() {
		try {
			in.close();
			out.close();
			if (client != null && !client.isClosed()) {
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.removeConnection(this);
		}
	}
}
