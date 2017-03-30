// YONGRUI WANG
// CS4283 - Final Program
// Mar. 29 2017

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

	public static void main(String[] args){

		int port = 8080;
		//set up the socket
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			System.out.println("waiting for the client \n");
			while (true) {
				Socket socket = ss.accept();
				System.out.println("Socket connected from " + 
						socket.getInetAddress() + " port " + socket.getPort());

				//In-line implementation of a new Runnable
				//Reference: http://stackoverflow.com/questions/877096/how-can-i-pass-a-parameter-to-a-java-thread
				new Thread(new Runnable() {

					private Socket socket;
				

					public Runnable init(Socket socket) {
						this.socket = socket;
						return this;
					}

					@Override
					public void run() {
						System.out.println("Thread " + Thread.currentThread().getName() + " started");

						try {
							InputStream is = socket.getInputStream();
							OutputStream os = socket.getOutputStream();

							PrintWriter out = new PrintWriter(os, true); //what we need

							InputStreamReader isr = new InputStreamReader(is);
							BufferedReader in = new BufferedReader(isr); //what we need

							String line = null;
							out.println("\nWhat is your name?");
							line =in.readLine();
								out.println("\nHello, " + line + "!");
								System.out.println("Client name is: " + line);
								out.println();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("Thread " + Thread.currentThread().getName() + " finished\n");
					}



				}.init(socket)).start(); // end Thread

			} // end while loop

		} catch (IOException e) {
			System.out.println("Failed to establish connection to port");
		} finally {
			
			//close the socket
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException ignored) {}
			}
		}


	} 



}
