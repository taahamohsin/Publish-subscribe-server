package pubsub;

import pubsub.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Subscriber extends Thread {
	
	private ArrayList<Message> Messages=new ArrayList<Message>(); // to store all received messages
	private long thread_id;
	private Socket socket;
	//TODO: maybe use ArrayList instead
	private String[] topics;
	private ArrayList<String> myTopics = new ArrayList<String>();
	
	
	/* Constructor */
	public Subscriber(Socket s, long id, String[] list) {
		this.socket = s;
		this.thread_id = id;
		this.setTopics(list);
	}
	/* Accessor methods */
	
	public String[] getTopics() {
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
	
	/* Mutator Methods */
	
	// Mutator method for received messages
	public void setMessages(ArrayList<Message> Messages){
		this.Messages=Messages;
	}
	
	// Mutator method for thread ids
	public void setID(long id){
		this.thread_id=id;
	}
	
	
	public void setTopics(String[] topics) {
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
		try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			out.println(topics.length);
			for (int i = 0; i < topics.length; i ++) {
				out.println(topics[i]);
			}
//			String line = null;
//			while ((line =in.readLine()) != null) {
//				System.out.println("Received: " + line);
//				out.println(line.toUpperCase());
//			}

			
		} catch (IOException e) {
			System.out.println("Error in Subscriber.run()");
		}
		
		System.out.println("Thread " + this.thread_id + " finished");
	}
	
	
	
}
