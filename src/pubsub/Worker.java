package pubsub;

import java.util.ArrayList;
import pubsub.Message;
import pubsub.Subscriber;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Worker {

	// To store the topic-to-subscriber mappings
	protected static ConcurrentHashMap<String, Message> topSubMap = new ConcurrentHashMap<String, Message>();

	// stores list of available topics
	ArrayList<String> topList = new ArrayList<String>();
	// TODO: maybe use ArrayList instead
	private String[] topics = { "Topic A", "Topic B", "Topic C", "Topic D", "Topic E" };

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
			
			
		}

	}
	
	/* Constructor */
	public Worker(int port) {
		for (String topic : this.topics) {
			topList.add(topic);
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
				System.out.println("Client connected from " + socket.getInetAddress());

				Subscriber s = new Subscriber(socket, id, topList);
				id++;
				s.start();
			} catch (IOException e) {
				System.out.println("Error in Worker connect");
			}
			
			
		}
		
	}

	// Adds a message of the message queue
	// public void insertMessage(Message msg) {
	// msgQueue.add(msg);
	// }

	// Retrieves the mapping of topics to subscribers
	public ConcurrentHashMap<String, Message> fetchSubscribers() {
		return Worker.topSubMap;
	}

}
