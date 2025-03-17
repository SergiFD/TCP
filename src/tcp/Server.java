package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ServerSocket server;
	private boolean running;
	private ExecutorService pool;
	private ConnectionHandler connection;

	public Server(int port) {
		try {
			server = new ServerSocket(port);
			running = true;
			pool = Executors.newCachedThreadPool();
		} catch (IOException e) {
			System.out.println("Error iniciando el servidor: " + e.getMessage());
			running = false;
		}
	}

	@Override
	public void run() {
		System.out.println("Servidor esperando conexiones en el puerto " + server.getLocalPort());

		while (running) {
			try {
				Socket client = server.accept();
				System.out.println("Cliente conectado: " + client.getInetAddress());

				// Crear y manejar una conexión (uno a uno)
				connection = new ConnectionHandler(client, this);
				pool.execute(connection);

			} catch (IOException e) {
				System.out.println("Error en la conexión: " + e.getMessage());
			}
		}
	}

	public void stopServer() {
		running = false;
		try {
			if (server != null)
				server.close();
			pool.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server(9000);
		new Thread(server).start();
	}
}
