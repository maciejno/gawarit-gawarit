package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Client {
	static JFrame loginFrame;
	private static Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	
	public Client() throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getLocalHost().getHostName(), 44242); //jest local host, ale trzeba b�dzie zmieni� na jaki� nielokalny
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public String communicate (String message) throws Exception{
       String response = "";	   
	   writer.write(message);
	   writer.flush();
	   
	   String line = reader.readLine();
	   while (line!=null){
		   response += line;
		   response += "\n";
	       System.out.println("response:" + response); //debugging
	       line = reader.readLine();
	   }
	   return response;
   }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loginFrame = null;
				
				try {
					loginFrame = new LoginFrame();
				} catch (LineUnavailableException | IOException e) {					
					e.printStackTrace();
				}
				loginFrame.setVisible(true);
			}
		});				
	}
	
}
