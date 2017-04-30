package pubsub;

import java.util.ArrayList;
import java.util.Scanner;

import pubsub.Message;
import pubsub.Subscriber;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class Worker {

	// To store the topic-to-subscriber mappings
	protected static ConcurrentHashMap<String, Message> topSubMap = new ConcurrentHashMap<String, Message>();

	// stores list of available topics
	//ArrayList<String> topList = new ArrayList<String>();
	// TODO: maybe use ArrayList instead
	private static String[] topics = { "Topic A", "Topic B", "Topic C", "Topic D", "Topic E" };

	private static ArrayList<Socket> socketList = new ArrayList<Socket>();

	private ServerSocket ss;


	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() {		
				Worker worker = new Worker(8888);
				worker.connect();
			}
		}).start();


		while (true) {
			System.out.println(socketList.size());
			int selected = askIntent();
			switch (selected) {
			case 1:
				System.out.println(topSubMap.keySet().toString());
				break;
			case 2:
				broadcast();
				break;
			case 3:
				pushTopic();
				break;
			}

		}

	}

	/* Constructor */
	public Worker(int port) {
		for (String topic : this.topics) {
			//topList.add(topic);
			topSubMap.put(topic, new Message());
		}
		try {
			this.ss = new ServerSocket(port);
			System.out.println("Worker Socket connected to port " + port);
		} catch (IOException e) {
			System.out.println("Error in Worker Constructor");
			System.exit(1);
		}

	}

	public void connect() {
		int id = 0;
		while (true) {
			System.out.println("Worker waiting for client to connect");
			try {
				Socket socket = ss.accept();
				socketList.add(socket);
				System.out.println("Client connected from " + socket.getInetAddress());

				Subscriber s = new Subscriber(socket, id);
				id++;
				s.start();
			} catch (IOException e) {
				System.out.println("Error in Worker connect");
			}


		}

	}

	public static void broadcast() {
		System.out.println("Enter the message to be broadcasted");
		Scanner sc = new Scanner (System.in);
		String line = sc.nextLine();
		if (line == "") return;
		for (Socket socket : socketList) {
			try {
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				if (line != null) {
					pw.println("System Broadcast");
					pw.println(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void pushTopic() {
		System.out.println("Enter the name of the desired topic, here are the available ones:");
		System.out.println(topSubMap.keySet().toString());
		Scanner sc = new Scanner(System.in);
		String topic = sc.nextLine();
		if (topic !="") {
			System.out.println("Enter the message");
			String message = sc.nextLine();
			if (message != "") {
				Content c = new Content(topic, message);
				topSubMap.get(topic).content.add(c);
				System.out.println("New message added: " + c.toString());
			}
		} else {
			System.out.println("Topic Not Found");
		}
	}


	public static int askIntent() {
		Scanner sc = new Scanner (System.in);
		System.out.println("\nPlease select by entering a number:");
		System.out.println("1: List all topics");
		System.out.println("2: Broadcast to all subscribers");
		System.out.println("3: Push to a topic");
		int selection = 0;
		boolean selected = false;
		do {

			selection = sc.nextInt();
			if (selection >=1 && selection <= 3) {
				selected = true;	
			} else {
				System.out.println("Not valid: try again");
			}

		} while (!selected);

		return selection;

	}

}
