package pubsub;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static final String  HOST = "localhost";
	public static final int PORT = 8888;

	public static void main(String[] args) {

		try (Socket s = new Socket(HOST, PORT);
				BufferedReader read = new BufferedReader
						(new InputStreamReader(s.getInputStream()));
				PrintStream ps = new PrintStream(s.getOutputStream());
				Scanner sc = new Scanner(System.in)) {
			int length = Integer.parseInt(read.readLine());
			String topics[] = new String[length];
			System.out.println("Here are the available topics:");
			System.out.println("------------------------------");
			for (int i = 0; i < length; i++) {
				String line = read.readLine();
				System.out.printf("Option %d: %s\n", i, line);
				
				//store the topic in the array too
				topics[i] = line;
			}
//			while (true) {
//			System.out.println("\nEnter words to be echoed");
//			String line = sc.nextLine();
//			System.out.println("string received: " + line);
//				ps.println(line);
//				line = read.readLine();
//				System.out.println(line);
//			}
			
		} catch (IOException e) {
			System.out.println("Error reading from the server");
		}
	}

}
