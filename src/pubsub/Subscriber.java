package pubsub;

import pubsub.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Subscriber extends Thread {
	
	public static final String[] LIST = {"Topic1", "Topic2","Topic3","Topic4","Topic5"};
	
	private ArrayList<Message> Messages=new ArrayList<Message>(); // to store all received messages
	private long thread_id;
	private Socket socket;
	
	
	/* Constructor */
	public Subscriber(Socket s, long id) {
		this.socket = s;
		this.thread_id = id;
	}
	/* Accessor methods */
	
	// Accessor method for re ceived messages
	public ArrayList<Message> fetchMessages(){
		return Messages;
	} 
	
	public long fetchID(){
		return this.thread_id;
	} 
	
	/* Mutator Methods */
	
	// Mutator method for received messages
	public void setMessages(ArrayList<Message> Messages){
		this.Messages=Messages;
	}
	
	public void setID(long id){
		this.thread_id=id;
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
			out.println(LIST.length);
			for (int i = 0; i < LIST.length; i ++) {
				out.println(LIST[i]);
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
