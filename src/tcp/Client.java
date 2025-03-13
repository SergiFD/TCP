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
			client = new Socket("127.0.0.1", 9999); // Cambiado a la variable de instancia
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			InputHandler inHandler = new InputHandler();
			Thread t = new Thread(inHandler);
			t.start();

			String inMessage;
			while ((inMessage = in.readLine()) != null) {
				System.out.println(inMessage);
			}
		} catch (Exception e) {
			shutdown();
		}
	}

	public void shutdown() {
		done = true;
		try {
			in.close();
			out.close();
			if (client != null && !client.isClosed()) { // Verificado que client no sea null
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace(); // Manejo de excepciones
		}
	}

	class InputHandler implements Runnable {

		@Override
		public void run() {
			try {
				BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
				while (!done) {
					String message = inReader.readLine();
					if (message.equals("/quit")) {
						inReader.close();
						shutdown();
					} else {
						out.println(message);
					}
				}
			} catch (Exception e) {
				shutdown();
			}
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}
}
