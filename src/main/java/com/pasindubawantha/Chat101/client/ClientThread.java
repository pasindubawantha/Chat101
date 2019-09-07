package com.pasindubawantha.Chat101.client;

import java.io.DataInputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import com.pasindubawantha.Chat101.client.Client;
import com.pasindubawantha.Chat101.server.Server;

class ClientThread implements Runnable
{
	DataInputStream dataInputStream;
	Client client;
	
	ClientThread(DataInputStream dataInputStream,Client client)
	{
		this.dataInputStream = dataInputStream;
		this.client = client;
	}

	public void run()
	{
		String message = "";
		do {
			try {
				message = dataInputStream.readUTF();
				if(message.startsWith(Server.UPDATE_USERS))
					updateUsersList(message);
				else if(message.equals(Server.LOGOUT_MESSAGE))
					break;
				else
					client.txtBroadcast.append("\n"+message);
				int lineOffset = client.txtBroadcast.getLineStartOffset(client.txtBroadcast.getLineCount()-1);
				client.txtBroadcast.setCaretPosition(lineOffset);
				}
			catch(Exception e){client.txtBroadcast.append("\nClientThread run : "+e);}
		} while(true);
	}

	public void updateUsersList(String usernameList)
	{
		Vector<String> usernameListVector = new Vector<String>();
		
		usernameList = usernameList.replace("[","");
		usernameList = usernameList.replace("]","");
		usernameList = usernameList.replace(Server.UPDATE_USERS,"");
		StringTokenizer userListTokenized = new StringTokenizer(usernameList,",");
		
		while(userListTokenized.hasMoreTokens()) {
			String username = userListTokenized.nextToken();
			usernameListVector.add(username);
		}
		
		client.usersList.setListData(usernameListVector);
	}
}
