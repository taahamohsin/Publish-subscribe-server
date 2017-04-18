package pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import pubsub.Message;
import pubsub.Subscriber;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker {

	// To store the topic-to-subscriber mappings
	HashMap<String, ArrayList<Subscriber>> topSubList = new HashMap<String, ArrayList<Subscriber>>();
	// to store the published messages
	ConcurrentLinkedQueue<Message> msgQueue = new ConcurrentLinkedQueue<Message>();
	// stores list of available topics
	ArrayList<String> topList=new ArrayList<String>();
	//TODO: maybe use ArrayList instead
	public static String[] list = {"Topic 1", "Topic 2","Topic 3","Topic 4","Topic 5"};
	
	private ServerSocket ss;
	
	/* Constructor */
	public Worker(int port) {
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
				
				Subscriber s = new Subscriber(socket, id, list);
				id++;
				s.start();
			} catch (IOException e) {
				System.out.println("Error in Worker connect");
			}
			
		}
	}
	
	public void insertMessage(Message msg) {
		msgQueue.add(msg);
	}

	public HashMap<String, ArrayList<Subscriber>> fetchSubscribers() {
		return this.topSubList;
	}

	public void insertSubscriber(Subscriber sub, String topic) {
		ArrayList<Subscriber> tmp;
		if (topSubList.containsKey(topic)) {
			tmp = topSubList.get(topic);
			tmp.add(sub);
			topSubList.put(topic, tmp);
		} else {
			tmp = new ArrayList<Subscriber>();
			tmp.add(sub);
			topSubList.put(topic, tmp);
		}
	}

	public void deleteSubscriber(Subscriber sub, String topic) {
		if (topSubList.containsKey(topic)) {
			ArrayList<Subscriber> tmp = topSubList.get(topic);
			tmp.remove(sub);
			topSubList.put(topic, tmp);
		} else {
			System.out.println("There is no such subscriber for that topic!");
		}
	}

	public void fetchMessagesForSpecificSub(Subscriber sub, String topic) {
		boolean empty = this.msgQueue.isEmpty();
		if (!empty) {
			while (!empty) {
				Message tmp = msgQueue.remove();
				if (tmp.fetchTopic().equalsIgnoreCase(topic)) {
					ArrayList<Subscriber> list = topSubList.get(topic);
					for (Subscriber sub2 : list) {
						if (sub2.equals(sub)) {
							ArrayList<Message> messages = sub.fetchMessages();
							messages.add(tmp);
							sub.setMessages(messages);
						}
					}
				}
			}
		} else {
			System.out.println("The publisher did not have any messages enqueued.");
		}
	}

	public void broadcastMessages() {
		boolean empty = this.msgQueue.isEmpty();
		if (!empty) {
			while (!empty) {
				Message tmp = this.msgQueue.remove();
				String top = tmp.fetchTopic();
				ArrayList<Subscriber> subList = topSubList.get(top);
				for (Subscriber sub : subList) {
					ArrayList<Message> messages = sub.fetchMessages();
					messages.add(tmp);
					sub.setMessages(messages);
				}
			}
		} else {
			System.out.println("No received messages to display.");
		}
	}

}
