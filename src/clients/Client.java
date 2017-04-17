package clients;

import java.io.*;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 8080;

		String name = "Kenny";

		try (Socket s = new Socket(host, port);
				BufferedReader read = new BufferedReader
						(new InputStreamReader(s.getInputStream()));
				PrintStream ps = new PrintStream(s.getOutputStream())) {
			
			System.out.println(read.readLine() + read.readLine());
			ps.print(name);
			System.out.println(name);
			
		} catch (IOException e) {
			System.out.println("Error reading from the server");
		}
	}

}
