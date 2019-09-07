package com.pasindubawantha.Chat101.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

class ServerThread implements Runnable
{
	Socket socket;
	ArrayList<Socket> socketArrayList;
	ArrayList<String> usersArrayList;
	String username;

	ServerThread (Socket socket, ArrayList<Socket> socketArrayList,ArrayList<String> usersArrayList)
	{
		this.socket=socket;
		this.socketArrayList = socketArrayList;
		this.usersArrayList = usersArrayList;
		try {
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			username = dataInputStream.readUTF();
			
			// Add user to group
			socketArrayList.add(socket);
			usersArrayList.add(username);
			System.out.println("User " + username + " logged in !");
			broadcast("----- "+ username+" Logged in at "+(new Date())+" -----");
			sendNewUserList();
			}
		catch(Exception e){System.err.println("ServerThread constructor  " + e);}
	}

	public void run()
	{
		String message;
		try {
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			do {
				message = dataInputStream.readUTF();
				
				// break loop if user logs out
				if(message.toLowerCase().equals(Server.LOGOUT_MESSAGE)) break;
				// send message
				broadcast(username + " ~ " + message);
				}
			while(true);
			
			// acknowlage logout and remove user
			DataOutputStream dataOutputStream =new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeUTF(Server.LOGOUT_MESSAGE);
			dataOutputStream.flush();
			
			usersArrayList.remove(username);
			broadcast("----- "+username+" Logged out at "+(new Date())+" -----");
			sendNewUserList();
			socketArrayList.remove(socket);
			socket.close();
		
		   }
		catch(Exception e){System.out.println("ServerThread Run"+e);}
	}

	public void sendNewUserList()
	{
		broadcast(Server.UPDATE_USERS+usersArrayList.toString());
	
	}

	public void broadcast(String message)	
	{
		System.out.println("Boradcasting : " + message);
		Iterator<Socket> i = socketArrayList.iterator();
		while(i.hasNext())
		{
		try{
			Socket socket = i.next();
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeUTF(message);
			dataOutputStream.flush();
		   }
		catch(Exception e){System.err.println("TellEveryOne "+e);}
		}
		System.out.println("Boradcasting done !");
	}
}