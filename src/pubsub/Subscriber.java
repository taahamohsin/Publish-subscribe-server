package pubsub;

import pubsub.Message;
import java.util.ArrayList;
import java.util.Iterator;

public class Subscriber {
	
	private ArrayList<Message> Messages=new ArrayList<Message>(); // to store all received messages
	private long thread_id;
	
	/* Accessor methods */
	
	// Accessor method for received messages
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
}
