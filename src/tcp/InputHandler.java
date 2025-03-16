package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputHandler implements Runnable {

	private Client client;

	public InputHandler(Client client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
			while (!client.isDone()) {
				String message = inReader.readLine();
				if (message.equals("/quit")) {
					inReader.close();
					client.shutdown();
					break;
				} else {
					client.sendMessage(message);
				}
			}
		} catch (Exception e) {
			client.shutdown();
		}
	}
}
