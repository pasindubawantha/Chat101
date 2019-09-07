package com.pasindubawantha.Chat101.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.text.StringEscapeUtils;

import com.pasindubawantha.Chat101.client.ClientThread;
import com.pasindubawantha.Chat101.server.Server;


public class Client implements ActionListener
{
	Socket socket;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	
	
	JButton sendButton, logoutButton,loginButton, exitButton;
	JFrame chatWindow;
	JTextArea txtBroadcast;
	JTextArea txtMessage;
	JList<String> usersList;
	
	final static String SEND_BUTTON_LABEL = "Send";
	final static String LOGIN_BUTTON_LABEL = "Login";
	final static String LOGOUT_BUTTON_LABEL = "Logout";
	final static String EXIT_BUTTON_LABEL = "Exit";
	
	public Client()
	{
	  	displayGUI();
	}

	public static void main(String []args)
	{
		new Client();
	}
	
	public void displayGUI()
	{
		chatWindow = new JFrame();
		txtBroadcast = new JTextArea(5,30);
		txtBroadcast.setEditable(false);
		txtMessage = new JTextArea(2,20);
		usersList = new JList();
		
		sendButton = new JButton(SEND_BUTTON_LABEL);
		logoutButton = new JButton(LOGOUT_BUTTON_LABEL);
		loginButton = new JButton(LOGIN_BUTTON_LABEL);
		exitButton = new JButton(EXIT_BUTTON_LABEL);
		
		JPanel center1 = new JPanel();
		center1.setLayout(new BorderLayout());
		center1.add(new JLabel("Broad Cast messages from all online users",JLabel.CENTER),"North");
		center1.add(new JScrollPane(txtBroadcast),"Center");
		
		JPanel south1=new JPanel();
		south1.setLayout(new FlowLayout());
		south1.add(new JScrollPane(txtMessage));
		south1.add(sendButton);
		
		JPanel south2=new JPanel();
		south2.setLayout(new FlowLayout());
		south2.add(loginButton);
		south2.add(logoutButton);
		south2.add(exitButton);
		
		JPanel south=new JPanel();
		south.setLayout(new GridLayout(2,1));
		south.add(south1);
		south.add(south2);
		
		JPanel east=new JPanel();
		east.setLayout(new BorderLayout());
		east.add(new JLabel("Online Users",JLabel.CENTER),"East");
		east.add(new JScrollPane(usersList),"South");
		
		chatWindow.add(east,"East");
		
		chatWindow.add(center1,"Center");
		chatWindow.add(south,"South");
		
		chatWindow.pack();
		chatWindow.setTitle("Chat101 [Login]");
		chatWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		chatWindow.setVisible(true);
		sendButton.addActionListener(this);
		logoutButton.addActionListener(this);
		loginButton.addActionListener(this);
		exitButton.addActionListener(this);
		logoutButton.setEnabled(false);
		loginButton.setEnabled(true);
		txtMessage.addFocusListener(new FocusAdapter() {public void focusGained(FocusEvent fe){txtMessage.selectAll();}});
		
		chatWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				if(socket!=null) {
				JOptionPane.showMessageDialog(chatWindow,"Logged out","Exit",JOptionPane.INFORMATION_MESSAGE);
				logoutSession();
				}
				System.exit(0);
			}
		});
	
	}
	
	public void actionPerformed(ActionEvent actionEvent)
	{
		JButton actionEventSource = (JButton)actionEvent.getSource();
		
		switch (actionEventSource.getText()) {
		case SEND_BUTTON_LABEL: {
			if (socket == null) {JOptionPane.showMessageDialog(chatWindow,"Login with a username"); return;}
			try{
				String sanatizedMessage = StringEscapeUtils.escapeJava(txtMessage.getText()); // Sanatize user input for java
				dataOutputStream.writeUTF(sanatizedMessage); 
				txtMessage.setText("");
			     }
			catch(Exception excp){txtBroadcast.append("\nsend button click :"+excp);}
			}
			break;
		
		case LOGIN_BUTTON_LABEL: {
			String username = JOptionPane.showInputDialog(chatWindow,"Enter a username: ");
			if(username!=null) {
				this.clientChat(username); 
				}
			}
			break;
		
		case LOGOUT_BUTTON_LABEL: {
			if(socket!=null) {
				logoutSession();
				}
			}
			break;
	
		case EXIT_BUTTON_LABEL: {
			if(socket!=null) {
				JOptionPane.showMessageDialog(chatWindow,"Logging out.","Exit",JOptionPane.INFORMATION_MESSAGE);
				logoutSession();
				}
			System.exit(0);
			}
			break;

		default:
			break;
		}
	}

	public void logoutSession()
	{
		if(socket == null) return;
		try{
			dataOutputStream.writeUTF(Server.LOGOUT_MESSAGE);
			Thread.sleep(500);
			socket = null;
			
			logoutButton.setEnabled(false);
			loginButton.setEnabled(true);
			chatWindow.setTitle("Chat101 [Login]");
		}
		catch(Exception e){txtBroadcast.append("\n Client logoutSession Method"+e);}
		
		
	}
	
	public void clientChat(String username)
	{
	try{
	     socket = new Socket(InetAddress.getLocalHost(), Server.PORT);
	     dataInputStream = new DataInputStream(socket.getInputStream());
	     dataOutputStream = new DataOutputStream(socket.getOutputStream());
	     Runnable clientThreadRunnable = new ClientThread(dataInputStream,this);
	     Thread clientThread = new Thread(clientThreadRunnable);
	     clientThread.start();
	     dataOutputStream.writeUTF(username);
	     chatWindow.setTitle("Chat101 [" + username + "]");
	     logoutButton.setEnabled(true);
	     loginButton.setEnabled(false);
	    }
	catch(Exception e){txtBroadcast.append("\nError Connecting to server " +e);}
	
	}
}
