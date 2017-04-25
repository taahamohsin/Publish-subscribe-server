package pubsub;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	public static final String  HOST = "localhost";
	public static final int PORT = 8888;

	public static void main(String[] args) {

		try (Socket s = new Socket(HOST, PORT);
				BufferedReader read = new BufferedReader
						(new InputStreamReader(s.getInputStream()));
				PrintStream ps = new PrintStream(s.getOutputStream())) {
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
			ArrayList<String> list = selectTopics(topics);

			System.out.println(list.toString());
			ps.println(list.size());
			for (int i = 0; i < list.size(); i ++) {
				ps.println(list.get(i));
			}
			
			//Now we could just start listening
			//every transmission will have 1) the topic and 2) the content
			do {
				String topic = read.readLine();
				String content = read.readLine();
				System.out.printf("\nNew content in [%s]\n", topic);
				System.out.println(content);
			} while (read.readLine() != null);

		} catch (IOException e) {
			System.out.println("Error reading from the server");
		}
	}

	public static ArrayList<String> selectTopics(String[] topics) {
		System.out.println("\nEnter the number(s) of your selected topics, "
				+ "separated by commas or spaces");
		boolean done = false;
		ArrayList<String> list = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);

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
		sc.close();
		System.out.println(list.toString());
		return list;

	}

}
