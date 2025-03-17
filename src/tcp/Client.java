package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private String serverIP;
	private int serverPort;
	private boolean running;

	public Client(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		running = true;
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(serverIP, serverPort);
				PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

			System.out.println("Conectado a " + serverIP + ":" + serverPort);
			System.out.println("Escribe mensajes para enviarlos. Escribe '!EXIT' para salir.");

			// Hilo para recibir mensajes del servidor
			Thread listenerThread = new Thread(() -> {
				try {
					String incomingMessage;
					while ((incomingMessage = in.readLine()) != null) {
						System.out.println("Servidor: " + incomingMessage);
					}
				} catch (Exception e) {
					System.out.println("Error al recibir mensajes del servidor.");
				}
			});
			listenerThread.start();

			// Bucle para enviar mensajes al servidor
			while (running) {
				String message = console.readLine();
				if (message.equalsIgnoreCase("!EXIT")) {
					running = false;
					out.println("!EXIT"); // Enviar comando de salida al servidor
					break;
				}
				out.println(message);
			}

		} catch (Exception e) {
			System.out.println("Error en la conexión: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		Client client = new Client("127.0.0.1", 9000);
		new Thread(client).start();
	}
}
