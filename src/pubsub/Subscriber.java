package pubsub;

import pubsub.Message;
import pubsub.Content;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Subscriber extends Thread {

	private ArrayList<Message> Messages = new ArrayList<Message>(); // to store
																	// all
																	// received
																	// messages
	private long thread_id;
	// private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private ArrayList<String> topics; // available topics

	private HashMap<String, Integer> myTopicMap = new HashMap<String, Integer>(); // selected
																					// topics

	/* Constructor */
	public Subscriber(Socket s, long id) {
		// this.socket = s;
		this.thread_id = id;
		this.topics = new ArrayList<String>(Worker.topSubMap.keySet());
		try {
			this.writer = new PrintWriter(s.getOutputStream(), true);
			this.reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Accessor methods */

	public ArrayList<String> getTopics() {
		return this.topics;
	}

	// Accessor method for received messages
	public ArrayList<Message> fetchMessages() {
		return Messages;
	}

	// Accessor method for thread ids
	public long fetchID() {
		return this.thread_id;
	}

	public void pushMessage(String message) {
		System.out.println("Hey were here");
		this.writer.println(message);

	}

	/* Mutator Methods */

	// Mutator method for received messages
	public void setMessages(ArrayList<Message> Messages) {
		this.Messages = Messages;
	}

	// Mutator method for thread ids
	public void setID(long id) {
		this.thread_id = id;
	}

	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}

	public void addTopic(String topic) {
		if (myTopicMap.containsKey(topic)) {
			writer.println("System Warning");
			writer.println("You are already subscribed to: " + topic);
		} else {
			writer.println("System Message");
			writer.println("You subscribed to: " + topic);
			myTopicMap.put(topic, 0);
		}
		if (!Worker.topSubMap.containsKey(topic)) {
			Worker.topSubMap.put(topic, new Message());
		}

	}

	public void removeTopic(String topic) {
		if (myTopicMap.containsKey(topic)) {
			myTopicMap.remove(topic);
			writer.println("System Message");
			writer.println("You unsubscribed: " + topic);
		} else {
			writer.println("System Warning");
			writer.println("You're not subscribed to: " + topic);
		}
	}

	public void run() {
		System.out.println("Subscriber on Thread " + this.thread_id + " started");
		try {
			writer.println(topics.size());
			for (int i = 0; i < topics.size(); i++) {
				writer.println(topics.get(i));
			}

			// read the reply
			int length = Integer.parseInt(reader.readLine());
			System.out.println("\nClient " + this.thread_id + " selected " + length + " topics");
			System.out.println("------------------------");
			for (int i = 0; i < length; i++) {
				String topic = reader.readLine();
				System.out.println(topic);
				myTopicMap.put(topic, 0);
				Worker.topSubMap.get(topic).subscribed++;
			}

			Scanner sc = new Scanner(System.in);
			// now start transmitting the messages
			while (true) {

				if (reader.ready()) {
					String line = reader.readLine();
					System.out.println("FROM CLIENT " + this.thread_id + ": " + line);
					if (line.contains("Subscribe:")) {
						String[] arr = line.split(": ");
						addTopic(arr[1]);
					} else if (line.contains("View Topics")) {
						writer.println("Topic List");
						writer.println(myTopicMap.keySet().toString());
					} else if (line.contains("Unsubscribe:")) {
						String[] arr = line.split(": ");
						removeTopic(arr[1]);
					} else if (line.contains("All Topics")) {
						writer.println("List of All Topics");
						writer.println(Worker.topSubMap.keySet().toString());
					} else if (line.contains("Post")) {
						String[] arr = line.split(" ");
						if (arr.length < 3) {
							continue;
						}
						Content c = new Content(arr[1].substring(0, arr[1].length() - 2), arr[3]);
						System.out.println("New Message received from Client" + this.thread_id);
						// System.out.println(c.toString());
						Worker.topSubMap.get(c.fetchTopic()).content.add(c);
					}
					Worker.printSelections();
				}

				for (String topic : myTopicMap.keySet()) {
					ArrayList<Content> contentArr = Worker.topSubMap.get(topic).content;
					int myIndex = myTopicMap.get(topic);
					if (contentArr.size() > myIndex) {
						Content m = contentArr.get(myIndex);
						myTopicMap.put(topic, myIndex + 1);
						writer.println(m.fetchTopic());
						writer.println(m.fetchPLoad());
					}

				}
			}

		} catch (IOException e) {
			System.out.println("Error in Subscriber.run()");
		}

		System.out.println("Thread " + this.thread_id + " finished");
	}

}
