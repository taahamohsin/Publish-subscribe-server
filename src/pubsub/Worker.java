package pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import pubsub.Message;
import pubsub.Subscriber;

public class Worker {

	// To store the topic-to-subscriber mappings
	HashMap<String, ArrayList<Subscriber>> topSubList = new HashMap<String, ArrayList<Subscriber>>();
	// to store the published messages
	ConcurrentLinkedQueue<Message> msgQueue = new ConcurrentLinkedQueue<Message>();
	// stores list of available topics
	ArrayList<String> topList=new ArrayList<String>();
	
	// Adds a message ot the message queue
	public void insertMessage(Message msg) {
		msgQueue.add(msg);
	}

	// Retrieves the mapping of topics to subscribers
	public HashMap<String, ArrayList<Subscriber>> fetchSubscribers() {
		return this.topSubList;
	}

	// Adds a new subscriber for a specific topic
	public void insertSubscriber(Subscriber sub, String topic) {
		ArrayList<Subscriber> tmp;
		if (topSubList.containsKey(topic)) { // if the topic already exists
			tmp = topSubList.get(topic);
			tmp.add(sub); // add to the existing arraylist
			topSubList.put(topic, tmp); // put arraylist back into the hashmap
		} else { // if it does not already exist (is this needed?)
			tmp = new ArrayList<Subscriber>(); // create arraylist to store subscriber list
			tmp.add(sub);
			topSubList.put(topic, tmp); // insert new arraylist into the hashmap
		}
	}

	// removes an existing subscriber from the list of subscribers for a given topic
	public void deleteSubscriber(Subscriber sub, String topic) {
		if (topSubList.containsKey(topic)) { // if the given topic actually exists
			ArrayList<Subscriber> tmp = topSubList.get(topic); // retrieve arraylist of subsctibers
			tmp.remove(sub); 
			topSubList.put(topic, tmp); // remove relevant subscriber and insert arraylist back into hashmap
		} else { // output error message if the given topic does not exist
			System.out.println("There is no such subscriber for that topic!");
		}
	}

	// retrieves all messages for a given subscriber at the point when it is called
	public void fetchMessagesForSpecificSub(Subscriber sub, String topic) {
		boolean empty = this.msgQueue.isEmpty();
		if (!empty) {
			while (!empty) {
				Message tmp = msgQueue.remove(); // remove and store front-most message
				if (tmp.fetchTopic().equalsIgnoreCase(topic)) { // if the message topic matches the 
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

	// broadcasts messages currently in the message queue to the relevant subscribers
	// TODO: add code to send messages via TCP
	public void broadcastMessages() {
		boolean empty = this.msgQueue.isEmpty();
		if (!empty) {
			while (!empty) {
				Message tmp = this.msgQueue.remove(); // store front-most message of queue
				String top = tmp.fetchTopic(); 
				ArrayList<Subscriber> subList = topSubList.get(top); // fetch all subscribers of the topic
				
				// Eventually add code to send out messages instead
				for (Subscriber sub : subList) {
					ArrayList<Message> messages = sub.fetchMessages(); // get all messages for the subscriber
					messages.add(tmp); // add the front-most message of the queue to the list
					sub.setMessages(messages); 
				}
			}
		} else {
			System.out.println("No received messages to display.");
		}
	}

}
