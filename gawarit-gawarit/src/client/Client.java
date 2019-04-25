package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	private static Socket socket;

	public static void main(String[] args) throws Exception{
	       socket = new Socket(InetAddress.getLocalHost().getHostName(), 38); //jest local host, ale trzeba bêdzie zmieniæ na jakiœ nielokalny
	       BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	       bufferedWriter.write("GET / HTTP/1.0\n\n");
	       bufferedWriter.flush();
	       BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	       String line = reader.readLine();
	       while (line!=null){
	           System.out.println(line);
	           System.out.flush();
	           line = reader.readLine();
	       }
	   }
	
}
