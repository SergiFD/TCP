package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class InputHandler {

	private PrintWriter out;
	private BufferedReader userInput;

	public InputHandler(PrintWriter out, BufferedReader userInput) {
		this.out = out;
		this.userInput = userInput;
	}

	public void handleInput() {
		try {
			String message;
			while ((message = userInput.readLine()) != null) {
				out.println(message); // Enviar mensaje al servidor

				if (message.equalsIgnoreCase("!EXIT")) {
					System.out.println("Saliendo del chat...");
					break; // Salir del bucle
				}
			}
		} catch (IOException e) {
			System.out.println("Error al leer la entrada del usuario: " + e.getMessage());
		}
	}
}
