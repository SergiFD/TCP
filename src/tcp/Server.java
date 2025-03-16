package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private ArrayList<ConnectionHandler> connections;
	private ServerSocket server;
	private boolean done;
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
				ConnectionHandler handler = new ConnectionHandler(client, this);
				connections.add(handler);
				pool.execute(handler);
			}
		} catch (IOException e) {
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

	public void removeConnection(ConnectionHandler handler) {
		connections.remove(handler);
	}

	public void shutdown() {
		done = true;
		try {
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

	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
}
