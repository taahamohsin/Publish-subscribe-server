package pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import pubsub.Message;
import pubsub.Subscriber;

public class Worker {
	HashMap<String, ArrayList<Subscriber>> subTopList=new HashMap<String, ArrayList<Subscriber>>();
	ConcurrentLinkedDeque<Message> msgQueue=new ConcurrentLinkedDeque<Message>();
	
	public void insertMessage(Message msg){
		msgQueue.add(msg);
	}
	
	public HashMap<String, ArrayList<Subscriber>> fetchSubscribers(){
		return this.subTopList;
	}
	
	public void insertSubscriber(Subscriber sub, String topic){
		ArrayList<Subscriber> tmp;
		if(subTopList.containsKey(topic)){
			tmp=subTopList.get(topic);
			tmp.add(sub);
			subTopList.put(topic, tmp);
		}
		else{
			tmp=new ArrayList<Subscriber>();
			tmp.add(sub);
			subTopList.put(topic,tmp);
		}
	}
	
	public void deleteSubscriber(Subscriber sub, String topic){
		if(subTopList.containsKey(topic)){
			ArrayList<Subscriber> tmp=subTopList.get(topic);
			tmp.remove(sub);
			subTopList.put(topic, tmp);
		}
		else{
			System.out.println("There is no such subscriber for that topic!");
		}
	}
	
	public void fetchMessages(Subscriber sub, String topic){
		boolean empty=this.msgQueue.isEmpty();
		if(!empty){
			while(!empty){
				Message tmp=msgQueue.remove();
				if(tmp.fetchTopic().equalsIgnoreCase(topic)){
					ArrayList<Subscriber> list=subTopList.get(topic);
					for(Subscriber sub2: list){
						if(sub2.equals(sub)){
							ArrayList<Message> messages=sub.fetchMessages();
							messages.add(tmp);
							sub.setMessages(messages);
						}
					}
				}
			}
		}
		else{
			System.out.println("The publisher did not have any messages enqueued.");
		}
	}
	
	public void broadcastMessages(){
		boolean empty=this.msgQueue.isEmpty();
		if(!empty){
			while(!empty){
				Message tmp=this.msgQueue.remove();
				String top=tmp.fetchTopic();
				ArrayList<Subscriber> subList=subTopList.get(top);
				for(Subscriber sub: subList){
					ArrayList<Message> messages=sub.fetchMessages();
					messages.add(tmp);
					sub.setMessages(messages);
				}
				
			}
		}
		else{
			System.out.println("No received messages to display.");
		}
	}
	
}
