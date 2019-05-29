package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Client {
	
	//static List<myFrame> framesList = new ArrayList<myFrame>();//lista wszystkich okienek i ich widocznosci
	static Map<JFrame, Boolean> framesMap = new HashMap<JFrame, Boolean>();
	static Map<String, Boolean> friendsMap = new HashMap<String, Boolean>();
	static LoginFrame loginFrame;
	static MainFrame mainFrame;
	static IpFrame ipFrame;
	static Map<String, MessageFrame> messageFrames = new HashMap<String, MessageFrame>();
	public static String myName = null;
	public static ExecutorService exec = Executors.newSingleThreadExecutor();
	public static ScheduledExecutorService scheduler;
	
	public static Color krasnyj = new Color(255,70,70);
	public static Color galubyj = new Color(80,80,255);
	
	public static String ip;
	public static Socket socket;
	static BufferedWriter writer;
	static BufferedReader reader;
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				try {
					InetAddress host = InetAddress.getLocalHost();
					ip = host.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}				
				loginFrame = null;
				mainFrame = null;				
				ipFrame = new IpFrame();						
			}
		});				
	}
	
	public static void initialize() {
		try {
			socket = new Socket(ip, 44242);
			//socket = new Socket("10.68.16.164", 44242);
			//socket = new Socket(ip, 44242);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Client.exec.execute(Client.loginFrame);
			framesMap.put(loginFrame, true);
			Client.setVisibleFrames();
			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Serwer niedostępny pod tym adresem", null, JOptionPane.ERROR_MESSAGE);
			ipFrame = new IpFrame();
		} //jest localhost, ale trzeba bedzie zmienic na jakis inny
		
		
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
	   System.out.println("Otrzymuję odpowiedz:\n" + response); //debugging
	   return response;
   }
	
	public static void send (String message) throws Exception{//tylko wysyla       
		   writer.write(message);
		   writer.flush();
		   System.out.println("Wysyłam wiadomość:\n" + message);//debugging
	   }
	
	public static void updateFriendsBox() {//odświeża combo box ze znajomymi
    	mainFrame.getGui().getChooseFriend().removeAllItems();
    	String status = null;
    	String item = null;
    	for(String key : Client.friendsMap.keySet()) {
    		if(Client.friendsMap.get(key)) status = " : aktywny";
    		else status = " : nieaktywny";
    		item = key + status;
    		mainFrame.getGui().getChooseFriend().addItem(item);
    	}
    }
	
	public static void updateFriendsMap(String [] lines) {
		Client.friendsMap.clear();
		for(int i = 2;i < lines.length ;i+=2) {
			
			Client.friendsMap.put(lines[i], new Boolean(lines[i+1]));//umieszcza otrzymaną listę z serwera w liście w pamięci
								//nazwa uzytkownika //true lub false					
		}
		Client.updateFriendsBox();//jak zrobi update listy, to potem uaktualnia combo box ze znajomymi
	}
	
	//chyba działa
	public static void setVisibleFrames() {//ustawia widocznosc okienek - wywolywana przez action listenery
		for (Map.Entry<JFrame,Boolean> entry : framesMap.entrySet()) {
			entry.getKey().setVisible(entry.getValue());//ustawia wszystkim okienkom taką widoczność jaką mają
		}
	}
	public static void restartSocket () {//zamyka socketa i otwiera nowego
		try {
			Client.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Client.initialize();
	}
	
	public static String createMessage (String indicator, String content) {		
		String message = indicator + "\r\n" +  
				content + "\r\n" +
				"~$end&\r\n";
		return message;
	}
	
	public static String createMessage (String indicator,String command, String content) {		
		String message = indicator + "\r\n" + 
				command + "\r\n" + 
				content + "\r\n" +
				"~$end&\r\n";
		return message;
	}	
}
