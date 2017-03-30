package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 8080;

		// Create a socket connection to the server
		Socket s = null;

		try {
			s = new Socket(host, port);

			// Catch UnknownHostException, close the socket and exit
		} catch (UnknownHostException e) {

			System.out.println("Host not found");
			System.exit(-1);

			// Catch IOException, close the socket and exit
		} catch (IOException e) {

			System.out.println("Cannot connect");
			System.exit(-1);

		} // end outer try-catch

		String name = "Kenny";

		BufferedReader read = null;
		PrintStream ps = null;
		try {
			ps = new PrintStream(s.getOutputStream());
			read = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println(read.readLine());
			System.out.println(read.readLine());
			ps.print(name);
			System.out.println(name);
		} catch (IOException e) {
			System.out.println("Error reading from the server");


			// Make sure to that the socket is eventually closed
			if (s != null) {
				try {
					s.close();
				} catch (IOException ignored) {
				}
			}

		}
	}

}
