package pubsub;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	public static final String  HOST = "localhost";
	public static final int PORT = 8888;

	static class Writer extends Thread {

		private PrintStream ps;
		private BufferedReader read;

		public Writer(PrintStream ps, BufferedReader read) {
			this.ps = ps;
			this.read = read;
		}

		public void run() {
			System.out.println("Writer Thread started");
			while (true) {
				try {
					String line = read.readLine();
					if (line != null) {
						System.out.println("Printed to Socket " + line);
						ps.println(line);
					}
						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			//System.out.println("Writer Thread finished");
		}
	}

	static class Listener extends Thread {

		private BufferedReader read;

		public Listener(BufferedReader r) {
			this.read = r;
		}

		public void run() {
			System.out.println("Listener Thread started");
			try {
				while (true) {
					String topic = read.readLine();
					String content = read.readLine();
					if (topic != null && content != null ) {
						System.out.printf("\nNew content in [%s]\n", topic);
						System.out.println(content);	
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
	}

	public static void main(String[] args) {

		try (Socket s = new Socket(HOST, PORT);
				BufferedReader read = new BufferedReader
						(new InputStreamReader(s.getInputStream()));
				PrintStream ps = new PrintStream(s.getOutputStream());
				Scanner sc = new Scanner(System.in);) {
			int length = Integer.parseInt(read.readLine());
			String topics[] = new String[length];


			System.out.println("\nHere are the available topics:");
			System.out.println("------------------------------");
			for (int i = 0; i < length; i++) {
				String line = read.readLine();
				System.out.printf("Option %d: %s\n", i, line);

				//store the topic in the array too
				topics[i] = line;
			}


			//sending the responses back
			ArrayList<String> list = selectTopics(topics, sc);

			System.out.println(list.toString());
			ps.println(list.size());
			for (int i = 0; i < list.size(); i ++) {
				ps.println(list.get(i));
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			//Start up the other thread
			Writer w = new Writer(ps, in);
			w.start();

			Listener l = new Listener(read);
			l.start();

			w.join();
			l.join();


			//Now we could just start listening
			//every transmission will have 1) the topic and 2) the content
			//			do {
			//				String topic = read.readLine();
			//				String content = read.readLine();
			//				System.out.printf("\nNew content in [%s]\n", topic);
			//				System.out.println(content);
			//			} while (read.readLine() != null);

		} catch (IOException | InterruptedException e) {
			System.out.println("Error reading from the server");
		}
	}

	public static ArrayList<String> selectTopics(String[] topics, Scanner sc) {
		System.out.println("\nEnter the number(s) of your selected topics, "
				+ "separated by commas or spaces");
		boolean done = false;
		ArrayList<String> list = new ArrayList<String>();

		while (!done) {

			String selected = sc.nextLine();
			selected = selected.replaceAll(" ", ",");
			String[] numbers = selected.split(",");
			try {
				for (String str : numbers) {
					if (str.length() != 0) {
						int index = Integer.parseInt(str);
						list.add(topics[index]);
					}

				}

				done = true;
			} catch (NumberFormatException e1) {
				list.clear();
				System.out.println("Input contains non-digits. Try again.");
			} catch (ArrayIndexOutOfBoundsException | NoSuchElementException e2) {
				list.clear();
				System.out.println("Input contains invalid numbers. Try again.");
			}

		}
		//sc.close();
		System.out.println(list.toString());
		return list;

	}

}
