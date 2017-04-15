package pubsub;

import pubsub.Message;
import java.util.ArrayList;
import java.util.Iterator;

public class Subscriber {
	
	private ArrayList<Message> Messages=new ArrayList<Message>(); // to store all received messages
	
	// Accessor method for received messages
	public ArrayList<Message> fetchMessages(){
		return Messages;
	} 
	
	// Mutator method for received messages
	public void setMessages(ArrayList<Message> Messages){
		this.Messages=Messages;
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
