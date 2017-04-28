package pubsub;

import pubsub.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Observable;

public class Subscriber extends Thread {

	private ArrayList<Message> Messages=new ArrayList<Message>(); // to store all received messages
	private long thread_id;
	//private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	//TODO: maybe use ArrayList instead
	private ArrayList<String> topics; // available topics


	private ArrayList<String> myTopics = new ArrayList<String>(); // selected topics

	/* Constructor */
	public Subscriber(Socket s, long id, ArrayList<String> list) {
		//this.socket = s;
		this.thread_id = id;
		this.setTopics(list);
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

	// Accessor method for re ceived messages
	public ArrayList<Message> fetchMessages(){
		return Messages;
	} 

	// Accessor method for thread ids
	public long fetchID(){
		return this.thread_id;
	} 
	
	public void pushMessage(String message) {
		System.out.println("Hey were here");
		this.writer.println(message);
		
	}

	/* Mutator Methods */

	// Mutator method for received messages
	public void setMessages(ArrayList<Message> Messages){
		this.Messages=Messages;
	}

	// Mutator method for thread ids
	public void setID(long id){
		this.thread_id=id;
	}


	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}

	public void addTopic(String topic) {
		//TODO: do we want to do this? This could be potentially hard
		// in the sense that any changes made here need to be updated by the server as well
	}

	public void removeTopic(String topic) {
		//TODO: do we want to do this? This could be potentially hard
		// in the sense that any changes made here need to be updated by the server as well
	}

	public void addSubscribedTopic(String topic) {
		//TODO: push in new topic to the ArrayList myTopic
	}

	public void removeSubscribedTopic(String topic) {
		//TODO:  Remove a topic from the ArrayList myTopic
	}

	// To print all received messages to the console
	public void print(){
		Iterator<Message> iter=Messages.iterator();
		while(iter.hasNext())
		{
			Message tmp=iter.next();
			System.out.println("Topic: "+tmp.fetchTopic()+" and payload: "+
					tmp.fetchPLoad());
		}
	}

	public void run() {
		System.out.println("Subscriber on Thread " + this.thread_id + " started");
		try {
			writer.println(topics.size());
			for (int i = 0; i < topics.size(); i ++) {
				writer.println(topics.get(i));
			}

			//read the reply
			int length = Integer.parseInt(reader.readLine());
			System.out.println("\nClient " + this.thread_id + " selected " + length +" topics");
			System.out.println("------------------------");
			for (int i = 0; i < length; i++) {
				String topic = reader.readLine();
				System.out.println(topic);
				myTopics.add(topic);
			}

			Scanner sc = new Scanner(System.in);
			//now start transmitting the messages
			while (true) {

				//System.out.println(Worker.topSubMap.toString());

				if (reader.ready()) {
					String line = reader.readLine();
					System.out.println("FROM CLIENT "+ this.thread_id + ": " + line);
					if(line.contains("Topic")) {
						String[] arr = line.split(":");
						Message m = new Message(arr[0], arr[1]);
						System.out.println("New Message received from Client" + this.thread_id);
						System.out.println(m.toString());
						Worker.topSubMap.get(m.fetchTopic()).add(m);
					}
				}

				for (String topic : myTopics) {
					while (!Worker.topSubMap.get(topic).isEmpty()) {
					System.out.println("Message queue not empty");
					Message m = Worker.topSubMap.get(topic).get(0);
					Worker.topSubMap.get(topic).remove(0);
					writer.println(m);
				}
				}
				

				//				//TODO: for debugging purposes , eventually this logic should be moved to Worker
				//				System.out.println("\nAny message to add? Enter'q' to quit");
				//				String input = "";
				//				while (!input.equals("q")) {
				//					System.out.print("Topic? ");
				//					input = sc.nextLine();
				//					if (input.equals("q")) break;
				//					System.out.print("Content? ");
				//					String content = sc.nextLine();
				//					Message m = new Message(content, input);
				//					if (myTopics.indexOf(input) != -1) {
				//						Messages.add(m);
				//						System.out.println("Topic Added:" + m.toString());
				//					} else {
				//						System.out.println("Topic not added, client not interested in " + m.fetchTopic());
				//					}
				//					input = content;
				//				}	
			}


		} catch (IOException e) {
			System.out.println("Error in Subscriber.run()");
		}

		System.out.println("Thread " + this.thread_id + " finished");
	}




}
