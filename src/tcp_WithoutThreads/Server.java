package tcp_WithoutThreads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(9999)) {
			System.out.println("Servidor iniciado. Esperando conexión...");

			while (true) { // El servidor siempre está esperando conexiones
				try (Socket clientSocket = serverSocket.accept();
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

					System.out.println("Cliente conectado.");

					// Usamos ConnectionHandler para manejar la conexión
					ConnectionHandler handler = new ConnectionHandler(clientSocket, out, in);
					handler.handleConnection(); // Procesar la conexión sin hilos
				} catch (Exception e) {
					System.out.println("Error con el cliente: " + e.getMessage());
				}
				System.out.println("Esperando nuevo cliente...");
			}

		} catch (Exception e) {
			System.out.println("Error en el servidor: " + e.getMessage());
		}
	}
}
