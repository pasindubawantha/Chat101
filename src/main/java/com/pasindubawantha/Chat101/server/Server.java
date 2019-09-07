package com.pasindubawantha.Chat101.server;

import java.net.*;
import java.util.*;
import com.pasindubawantha.Chat101.server.ServerThread;

public class Server
{
	ArrayList<Socket> socketArrayList=new ArrayList();
	ArrayList<String> userArrayList = new ArrayList();
	ServerSocket serverSocket;
	Socket socket;
	
	public final static int PORT=1500;
	public final static String UPDATE_USERS="updateuserslist:";
	public final static String LOGOUT_MESSAGE="@@logoutme@@:";
	
	public Server()
	{
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Chat101 Server listining " + serverSocket);
			while(true) {
				socket = serverSocket.accept();
				Runnable runnableServerThread = new ServerThread(socket, socketArrayList, userArrayList);
				Thread serverThread = new Thread(runnableServerThread);
				serverThread.start();
				}
			}
		catch(Exception e) {
			System.err.println("Server constructor error " + e);
			}
	}

	public static void main(String [] args)
	{
		new Server();
	}
}