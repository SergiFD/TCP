package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private boolean running;
	private Server server;
	private Thread inputThread;

	public ConnectionHandler(Socket client, Server server) {
		this.client = client;
		this.server = server;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			running = true;

			// Permite al servidor escribir al cliente
			inputThread = new Thread(new InputHandler(out));
			inputThread.start();

		} catch (Exception e) {
			System.out.println("Error inicializando la conexión: " + e.getMessage());
			running = false;
		}
	}

	@Override
	public void run() {
		try {
			String message;
			while (running && (message = in.readLine()) != null) {
				System.out.println("Cliente: " + message);

				if (message.equalsIgnoreCase("!EXIT")) {
					System.out.println("Cliente desconectado.");
					shutdown();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Cliente desconectado inesperadamente.");
		} finally {
			shutdown();
		}
	}

	public void shutdown() {
		running = false;
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (client != null && !client.isClosed())
				client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		server.stopServer(); // Cierra el servidor si el cliente se desconecta
	}
}
