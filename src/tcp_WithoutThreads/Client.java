package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		try (Socket socket = new Socket("127.0.0.1", 9000);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

			System.out.println("Conectado al servidor en 127.0.0.1:9000");
			System.out.println("Escribe un mensaje para enviarlo. Escribe '!EXIT' para salir.");

			// Se crean los manejadores de conexión y entrada
			ConnectionHandler connectionHandler = new ConnectionHandler(in);
			InputHandler inputHandler = new InputHandler(out, console);

			// Bucle principal del cliente
			while (true) {
				// 1️⃣ Leer entrada del usuario desde la consola
				String clientMessage = inputHandler.readInput();
				if (clientMessage.equalsIgnoreCase("!EXIT")) {
					out.println("!EXIT");
					break; // Si el cliente decide salir, cerramos la conexión
				}
				out.println(clientMessage);

				// 2️⃣ Leer respuesta del servidor
				String serverMessage = connectionHandler.readMessage();
				if (serverMessage == null || serverMessage.equalsIgnoreCase("/quit")) {
					System.out.println("El servidor se ha desconectado.");
					break; // Salimos si el servidor cierra la conexión
				}
				System.out.println("Servidor: " + serverMessage);
			}

		} catch (Exception e) {
			System.out.println("Error en el cliente: " + e.getMessage());
		}

		System.out.println("Cliente cerrado.");
	}
}
