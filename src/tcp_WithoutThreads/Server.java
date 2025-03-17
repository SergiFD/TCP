package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(9000)) {
			System.out.println("Servidor iniciado en el puerto 9000. Esperando conexión de un cliente...");

			try (Socket clientSocket = serverSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

				System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

				// Se crean los manejadores de conexión y entrada
				ConnectionHandler connectionHandler = new ConnectionHandler(in);
				InputHandler inputHandler = new InputHandler(out, console);

				// Bucle principal del servidor
				while (true) {
					// 1️⃣ Leer mensaje del cliente
					String clientMessage = connectionHandler.readMessage();
					if (clientMessage == null || clientMessage.equalsIgnoreCase("!EXIT")) {
						System.out.println("El cliente se ha desconectado.");
						break; // Salimos del bucle si el cliente cierra la conexión
					}
					System.out.println("Cliente: " + clientMessage);

					// 2️⃣ Leer mensaje desde la consola del servidor y enviarlo al cliente
					String serverMessage = inputHandler.readInput();
					if (serverMessage.equalsIgnoreCase("!EXIT")) {
						out.println("!EXIT");
						break; // Salimos si el servidor decide cerrar la conexión
					}
					out.println(serverMessage);
				}
			}

		} catch (Exception e) {
			System.out.println("Error en el servidor: " + e.getMessage());
		}

		System.out.println("Servidor cerrado.");
	}
}
