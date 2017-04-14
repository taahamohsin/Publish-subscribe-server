package pubsub;

import pubsub.Message;
import java.util.ArrayList;
import java.util.Iterator;

public class Subscriber {

	private ArrayList<Message> Messages=new ArrayList<Message>();
	
	public ArrayList<Message> getMessages(){
		return Messages;
	} 
	
	public void setMessages(ArrayList<Message> Messages){
		this.Messages=Messages;
	}

	public void print(){
		Iterator iter=Messages.iterator();
		while(iter.hasNext()){
			System.out.println("Topic: ");
		}
	}
}
