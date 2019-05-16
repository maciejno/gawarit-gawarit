package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Client {
	
	static List<myFrame> framesList = new ArrayList<myFrame>();//lista wszystkich okienek i ich widocznosci
	
	static JFrame loginFrame, mainFrame;
	
	private static Socket socket;
	static BufferedWriter writer;
	static BufferedReader reader;
	
	//konstruktor
	public Client(){
		try {
			socket = new Socket(InetAddress.getLocalHost().getHostName(), 44242);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //jest local host, ale trzeba b�dzie zmieni� na jaki� nielokalny
		
		framesList.add(new myFrame(loginFrame,true));
		framesList.add(new myFrame(mainFrame,false));
	}

	public static String communicate (String message) throws Exception{//wysyla i odbiera z serwera
       String response = "";	   
	   writer.write(message);
	   writer.flush();
	   
	   String line = reader.readLine();
	   while (line != "~$end&"){
		   response += line;
		   response += "\r\n";
	       //System.out.println("response:" + response); //debugging
	       line = reader.readLine();
	   }
	   System.out.println("response:" + response); //debugging
	   return response;
   }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loginFrame = null;
				mainFrame = null;
				try {
					loginFrame = new LoginFrame();
					mainFrame = new MainFrame();
				} catch (LineUnavailableException | IOException e) {					
					e.printStackTrace();
				}		
				//loginFrame.setVisible(true);
				setVisibleFrames();		
				System.out.println("Hej hej");
			}
		});				
	}
	
	//coś to nie działa tak jak bym chciał
	public static void setVisibleFrames() {//ustawia widocznosc okienek - wywolywana przez action listenery
		for (int i = 0; i < framesList.size(); i++) {
			framesList.get(i).getFrame().setVisible( framesList.get(i).getVisibility() );
		}
	}
	
}
