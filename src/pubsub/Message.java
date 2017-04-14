package pubsub;

public class Message {
	
	private String pLoad; // payload for the message
	private String topic; // the topic to which it pertains
	
	// Default constructor for the Message class
	public Message(){		
	}
	
	// Alternate constructor for the Message class
	public Message(String pLoad, String topic){
		this.pLoad=pLoad;
		this.topic=topic;
	}
	
	/* Accessor methods for the Message class */
	
	// Retrieves the value of the pLoad field
	public String fetchPLoad(){
		return this.pLoad;
	}
	
	// Retrieves the value of the topic field
	public String fetchTopic(){
		return this.topic;
	}

	/* Mutator methods for the Message class*/
	
	// Alters the value of the payload field
	public void setPLoad(String pLoad){
		this.pLoad=pLoad;
	}
	
	// Alters the value of the topic field
	public void setTopic(String topic){
		this.topic=topic;
	}
	
}
	


