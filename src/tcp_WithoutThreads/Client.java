package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		try (Socket client = new Socket("127.0.0.1", 9999);
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

			System.out.println("Conectado al servidor.");
			System.out.println(in.readLine()); // Mensaje de bienvenida

			// Usamos InputHandler para leer la entrada del usuario sin hilos
			InputHandler inputHandler = new InputHandler(out, userInput);
			inputHandler.handleInput(); // Procesar la entrada del usuario

			// Mostrar mensajes del servidor
			String serverMessage;
			while ((serverMessage = in.readLine()) != null) {
				System.out.println("Servidor dice: " + serverMessage);
			}

		} catch (Exception e) {
			System.out.println("Error en el cliente: " + e.getMessage());
		}
	}
}
