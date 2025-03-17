package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class InputHandler implements Runnable {

	private PrintWriter out;
	private boolean running;

	public InputHandler(PrintWriter out) {
		this.out = out;
		this.running = true;
	}

	@Override
	public void run() {
		try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
			while (running) {
				String message = consoleReader.readLine();
				if (message.equalsIgnoreCase("!EXIT")) {
					running = false;
					break;
				}
				out.println(message); // Enviar mensaje al cliente sin prefijo "Servidor: "
			}
		} catch (Exception e) {
			System.out.println("Error en InputHandler: " + e.getMessage());
		}
	}

	public void stop() {
		running = false;
	}
}
