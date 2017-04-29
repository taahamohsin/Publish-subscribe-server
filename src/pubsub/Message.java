package pubsub;

import java.util.ArrayList;
import pubsub.Content;

public class Message {
	
	
	public int subscribed;
	public ArrayList<Content> content;
	
	public Message() {
		this.subscribed = 0;
		this.content = new ArrayList<Content> ();
	}
	
}
