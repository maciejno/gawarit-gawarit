package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Client {
	
	//static List<myFrame> framesList = new ArrayList<myFrame>();//lista wszystkich okienek i ich widocznosci
	static Map<JFrame, Boolean> framesMap = new HashMap<JFrame, Boolean>();
	static Map<String, Boolean> friendsMap = new HashMap<String, Boolean>();
	static LoginFrame loginFrame;
	static MainFrame mainFrame;
	static Map<String, MessageFrame> messageFrames = new HashMap<String, MessageFrame>();
	public static String myName = null;
	public static ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public static Socket socket;
	static BufferedWriter writer;
	static BufferedReader reader;
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loginFrame = null;
				mainFrame = null;
				try {
					loginFrame = new LoginFrame();
					
				} catch (LineUnavailableException | IOException e) {					
					e.printStackTrace();
				}		
				//loginFrame.setVisible(true);
				initialize();
				setVisibleFrames();		
				
			}
		});				
	}
	
	public static void initialize() {
		try {
			//socket = new Socket(InetAddress.getLocalHost().getHostName(), 44242);
			socket = new Socket("10.68.16.164", 44242);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
		} catch (IOException e) {
			e.printStackTrace();
		} //jest localhost, ale trzeba bedzie zmienic na jakis inny
		
		framesMap.put(loginFrame, true);
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
	   System.out.println("response:" + response); //debugging
	   return response;
   }
	
	public static void send (String message) throws Exception{//tylko wysyla       
		   writer.write(message);
		   writer.flush();
		   System.out.println("message:" + message);//debugging
	   }
	
	public static void updateFriendsBox() {//odświeża combo box ze znajomymi
    	mainFrame.getGui().getChooseFriend().removeAllItems();
    	String status = null;
    	String item = null;
    	for(String key : Client.friendsMap.keySet()) {
    		if(Client.friendsMap.get(key)) status = " on";
    		else status = " off";
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

	public static String [] listen() {//zwraca to co wysłuchał - nickname od którego idzie + wiadomość
		String username = null;
		String message = "";
		String[] envelope =  new String[2];
		while(true) {
			try {
				System.out.println("czekam aż coś przyjdzie");//DEBUGGING
	            String line = reader.readLine();
	            System.out.println("czytam");//DEBUGGING
	            if(line.equals("~$message&")) {
	            	System.out.println("jakaś wiadomość");//DEBUGGING
	                line = reader.readLine();   
	                username = line;
	                line = reader.readLine();
	                while(!line.equals("~$end&")) {
	                	System.out.println(line);//DEBUGGING
	                	message = message + line + "\r\n";
	                	line = reader.readLine();
	                }
	                 envelope [0] = username;
		             envelope [1] = message;       
		             return envelope;
	            }else if (line.equals("~$instr&")) {
	            		            
	            }	         
	        }catch (IOException e) {
	            e.printStackTrace();
	          return null;
	        }     
	    }
	}
}
