package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ArrayList<ConnectionHandler> connections;
	private ServerSocket server;
	public boolean done;
	private ExecutorService pool;

	public Server() {
		connections = new ArrayList<>();
		done = false;
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(9999);
			pool = Executors.newCachedThreadPool();
			while (!done) {
				Socket client = server.accept();
				ConnectionHandler handler = new ConnectionHandler(client);
				connections.add(handler);
				pool.execute(handler);
			}
		} catch (Exception e) {
			e.printStackTrace();
			shutdown();
		}
	}

	public void broadcast(String message) {
		for (ConnectionHandler ch : connections) {
			if (ch != null) {
				ch.sendMessage(message);
			}
		}
	}

	public void shutdown() {
		try {
			done = true;
			if (!server.isClosed()) {
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (ConnectionHandler ch : connections) {
			ch.shutdown();
		}
	}

	class ConnectionHandler implements Runnable {

		private Socket client;
		private BufferedReader in;
		private PrintWriter out;

		private String nickname;

		public ConnectionHandler(Socket client) {
			this.client = client; // Asignado correctamente
		}

		@Override
		public void run() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out.println("Por favor inserte un nickname:");
				nickname = in.readLine();
				System.out.println(nickname + " ha sido conectado");
				broadcast(nickname + " ha empezado el chat");
				String message;

				while ((message = in.readLine()) != null) {
					if (message.startsWith("/nick")) {
						String[] messageSplit = message.split(" ", 2);
						if (messageSplit.length == 2) {
							broadcast(nickname + " se ha renombrado a " + messageSplit[1]); // Espacio añadido
							System.out.println(nickname + " se ha renombrado a " + messageSplit[1]); // Espacio añadido
							nickname = messageSplit[1];
							out.println("Se ha cambiado el nickname a " + nickname); // Espacio añadido
						} else {
							out.println("¡No se proporcionó un nickname!"); // Espacio añadido
						}
					} else if (message.startsWith("/quit")) {
						broadcast(nickname + " ha salido del chat!"); // Espacio añadido
						shutdown();
					} else {
						broadcast(nickname + ": " + message);
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
				if (client != null && !client.isClosed()) { // Verificado que client no sea null
					client.close();
				}
			} catch (Exception e) {
				// Ignorar esto
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
}
