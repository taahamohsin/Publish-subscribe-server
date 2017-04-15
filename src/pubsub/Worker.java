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
