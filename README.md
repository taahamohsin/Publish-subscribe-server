# Publish-subscribe-server

Contributors: Yongrui (Kenny) Wang and Taaha Bin Mohsin

This repository contains the source code for a publish-subscribe server. The model consists of a broker and a client(s), with clients able to communicate with the broker and subscribe to communications based on topic names. The MQTT protocol provides a good approximate model for what we are trying to achieve. TCP is the primary protocol utilized in the project, in order to avoid dealing with acknowledgments and the resulting complexity. Possible additional features include heartbeat functionality for detecting continued connectivity with a client.
