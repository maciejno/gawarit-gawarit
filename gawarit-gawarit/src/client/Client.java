package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Client {
	
	//static List<myFrame> framesList = new ArrayList<myFrame>();//lista wszystkich okienek i ich widocznosci
	static Map<JFrame, Boolean> framesMap = new HashMap<JFrame, Boolean>();
	static Map<String, Boolean> friendsMap = new HashMap<String, Boolean>();
	static LoginFrame loginFrame;
	static MainFrame mainFrame;
	
	private static Socket socket;
	static BufferedWriter writer;
	static BufferedReader reader;
	
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
				initialize();
				setVisibleFrames();		
				
			}
		});				
	}
	
	private static void initialize() {
		try {
			socket = new Socket(InetAddress.getLocalHost().getHostName(), 44242);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
		} catch (IOException e) {
			e.printStackTrace();
		} //jest local host, ale trzeba b�dzie zmieni� na jaki� nielokalny
		
		framesMap.put(loginFrame, true);
		framesMap.put(mainFrame, false);
	}

	public static String communicate (String message) throws Exception{//wysyla i odbiera z serwera
       String response = "";	       
	   writer.write(message);
	   writer.flush();
	   System.out.println("message:" + message);//debugging
	   
	   String line = reader.readLine();
	   while (!line.equals("~$end&")){
		   response += line;
		   response += "\r\n";
	       //System.out.println("response:" + response); //debugging
	       line = reader.readLine();
	   }
	   //System.out.println("response:" + response); //debugging
	   return response;
   }
	
	public static void updateFriendsMap(String [] lines) {
		Client.friendsMap.clear();
		for(int i = 2;i < lines.length ;i++) {
			
			Client.friendsMap.put(lines[i], new Boolean(lines[i+1]));//umieszcza otrzymaną listę z serwera w liście w pamięci
								//nazwa uzytkownika //true lub false					
		}		
	}
	
	//chyba działa
	public static void setVisibleFrames() {//ustawia widocznosc okienek - wywolywana przez action listenery
		for (Map.Entry<JFrame,Boolean> entry : framesMap.entrySet()) {
			entry.getKey().setVisible(entry.getValue());//ustawia wszystkim okienkom taką widoczność jaką mają
		}
	}
	
}
