package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private boolean done;

	@Override
	public void run() {
		try {
			client = new Socket("127.0.0.1", 9999);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			InputHandler inHandler = new InputHandler(this);
			Thread t = new Thread(inHandler);
			t.start();

			String inMessage;
			while ((inMessage = in.readLine()) != null) {
				System.out.println(inMessage);
			}
		} catch (Exception e) {
			System.out.println("Error en el cliente: " + e.getMessage());
			e.printStackTrace();
		} finally {
			shutdown(); // Asegurar que shutdown se llama siempre, pero con las verificaciones previas
		}
	}

	public void shutdown() {
		done = true;
		try {
			if (in != null) { // Verificar que in no sea null antes de cerrarlo
				in.close();
			}
			if (out != null) { // Verificar que out no sea null antes de cerrarlo
				out.close();
			}
			if (client != null && !client.isClosed()) { // Verificar que client no sea null
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isDone() {
		return done;
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

	public void sendMessage(String message) {
		if (out != null) { // Verifica que out no sea null antes de usarlo
			out.println(message);
		}
	}
}
