# Publish-subscribe-server

Contributors: Yongrui (Kenny) Wang and Taaha Bin Mohsin

This repository contains the source code for a publish-subscribe server. The model consists of a broker and one or more subscribers, with subscribers able to communicate with the broker and subscribe to communications based on topic names. The MQTT protocol provides a good approximate model for this. TCP is the primary protocol utilized in the project, in order to avoid dealing with acknowledgments and the resulting complexity. 

Subscribers have the following options:
<ol>
<li> Publish a message to a specific topic </li>
<li> Subscribe to a topic by entering input in the following format: "Subscribe: |topicname|" </li>
<li> Unsubscribe from a topic that they are presently subscribed to by entering input in the following format: "Unsubscribe: |topicname|" </li>
<li> View the list of topics they are currently subscribed to by entering the following phrase: "View Topics"
</ol>

For option (2), if the topic does not already exist, the topic gets created and added to the list of available topics.
For option (3), if the subscriber is not subscribed to the given topic, or the topic does not exist, the subscriber is sent an error message.

The server has the following potential actions:
<ol>
<li>List all topics</li>
<li>Broadcast a system message to all subscribers</li>
<li>Post to a topic</li>
</ol>
The server side requires the user to input a number corresponding to their choice of action, and proceed appropriately to perform the requested action.
