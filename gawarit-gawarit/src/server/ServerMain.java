package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {

    private static Vector<Connection> connections =new Vector<Connection>();

    public static void main(String[] args) throws Exception{

        ServerSocket serverSocket = new ServerSocket(44242);
        System.out.println("Uruchomiono Serwer.");
        while (true) {
            final Socket socket = serverSocket.accept();
            System.out.println("Nowa proba polaczenia...");
            BufferedReader reciever= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter transmitter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Connection connect = new Connection(socket, reciever, transmitter);
            Thread thread = new Thread(connect);
            connections.add(connect);
            thread.start();
        }
    }

}


