//package Chatbot;
import java.net.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

class Client extends JFrame
{
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	//Declare Components
	private JLabel heading = new JLabel("Client");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto",Font.PLAIN,20);
                  //constructor
	public Client() throws IOException 
	{
		System.out.println("Sending Request to Server");
		socket=new Socket("192.168.56.1",7777);
		System.out.println("Connection Done");
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
		createGUI();
		Events();
		StartReading();
		//StartWriting();
	}
	private void Events() 
	{
		messageInput.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				//System.out.println("key released"+e.getKeyCode());
				if(e.getKeyCode()==10)
				{
					//System.out.println("You pressed Enter");
					String ContentSend = messageInput.getText();
					messageArea.append("Me :"+ContentSend+"\n");
					out.println(ContentSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
			
			
		});
	}
	private void createGUI()
		{
			// gui code
			this.setTitle("Client Messanger[END]");
			this.setSize(500,500);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// coding for component
			heading.setFont(font);
			messageArea.setFont(font);
			messageInput.setFont(font);
			heading.setHorizontalAlignment(SwingConstants.CENTER);
			heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
			messageArea.setEditable(false);
			messageInput.setHorizontalAlignment(SwingConstants.CENTER);
			// Layout of frame
			this.setLayout(new BorderLayout());
			
			// Adding Components to Frame
			this.add(heading,BorderLayout.NORTH);
			JScrollPane scroll=new JScrollPane(messageArea);
			this.add(scroll,BorderLayout.CENTER);
			this.add(messageInput,BorderLayout.SOUTH);
			
			this.setVisible(true);
		}	


	//start reading method
	public void StartReading()
	{
		Runnable r1 = ()->{
			System.out.println("reader started");
			try 
		{
			while(true)
			{
				String msg = br.readLine();
				if(msg.equals("exit")) 
				{
				System.out.println("Server Terminated the chat");
				JOptionPane.showConfirmDialog(this,"Server Terminated the chat");
				messageInput.setEnabled(false);
				socket.close();
				break;
				}
				//System.out.println("Server : " + msg);
				messageArea.append("Server : " + msg+"\n");
			}
		}catch(IOException e)
			{
			System.out.println("Connection Closed");
			
			}
		};	
		new Thread(r1).start();
	}

	//start writing method
	public void StartWriting() 
	{
		Runnable r2 = ()->{
		    System.out.println("Writer Started");
		    try
		    {
		    	while(!socket.isClosed())
		    	{
		    		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		    		String content=br1.readLine();
		    		out.println(content);
		    		out.flush();
				if(content.equals("exit"))
				{
					socket.close();
					break;
				}
		    	}
		    }catch(Exception e) {
		    	//e.printStackTrace();
			System.out.println("Connection Closed");}
		};
		new Thread(r2).start();
	}
	public static void main(String[] args) 
	{
		System.out.println("This is Client..");
		try {
			new Client();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// TODO Auto-generated method stub

	}

}
