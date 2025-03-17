package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class InputHandler {

	private PrintWriter out; // Salida para enviar mensajes al cliente
	private BufferedReader console; // Lector de la entrada del servidor (teclado)

	public InputHandler(PrintWriter out, BufferedReader console) {
		this.out = out;
		this.console = console;
	}

	public String readInput() {
		try {
			System.out.print("Tú: ");
			return console.readLine();
		} catch (IOException e) {
			System.out.println("Error al leer entrada: " + e.getMessage());
			return "!EXIT"; // En caso de error, devolver "!EXIT" para cerrar conexión
		}
	}
}
